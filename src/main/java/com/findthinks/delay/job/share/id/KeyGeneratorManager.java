package com.findthinks.delay.job.share.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class KeyGeneratorManager {

    private static final Logger LOG = LoggerFactory.getLogger(KeyGeneratorManager.class);

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    private final ConcurrentMap<String, KeyGenerator> keyGenerators = new ConcurrentHashMap<>();

    private Lock locker = new ReentrantLock();

    private SequenceKeyService sequenceKeyService;

    public KeyGeneratorManager() {
    }

    public KeyGeneratorManager(SequenceKeyService sequenceKeyService) {
        this.sequenceKeyService = sequenceKeyService;
    }

    public KeyGenerator getKeyGenerator(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Miss key parameter");
        }

        KeyGenerator keyGenerator = keyGenerators.get(key);
        if (keyGenerator == null) {
            try {
                locker.lock();
                // Double check
                if ((keyGenerator = keyGenerators.get(key)) == null) {
                    for (;;) { // CAS
                        SequenceKey persistentKey = sequenceKeyService.loadSequenceKey(key);
                        if (persistentKey == null) {
                            throw new RuntimeException("Unregistered sequence key generator: " + key);
                        }

                        long newStartWith = persistentKey.getStartWith();
                        long newEndWith = newStartWith + persistentKey.getIncSpan();
                        if (sequenceKeyService.compareAndSet(persistentKey.getId(), newStartWith, newEndWith)) {
                            keyGenerator = new KeyGeneratorImpl(persistentKey.getId(), key, newStartWith, newEndWith - 1, persistentKey.getIncSpan());
                            break;
                        }
                    }
                    keyGenerators.put(key, keyGenerator);
                }
            } finally {
                locker.unlock();
            }
        }
        return keyGenerator;
    }

    private class KeyGeneratorImpl implements KeyGenerator {
        private final BigDecimal FIRE_CACHE_BUF_THRESHOLD_RATIO = BigDecimal.valueOf(0.4);
        private final Object genLocker = new Object();
        private final Object cacheLocker = new Object();
        private volatile long fireThreshold;
        private volatile boolean fireCache = false;
        private final int id;
        private final String key;
        private volatile Segment cur;
        private volatile Segment buf;

        public KeyGeneratorImpl(int id, String key, long startWith, long endWith, long incSpan) {
            this.id = id;
            this.key = key;
            this.cur = new Segment(startWith, endWith, incSpan);
            this.fireThreshold = FIRE_CACHE_BUF_THRESHOLD_RATIO.multiply(BigDecimal.valueOf(incSpan)).longValue();
        }

        @Override
        public long nextId() {
            int cnt = cur.counterGetAndInc();
            if (cnt < cur.incSpan) {
                if (cnt >= fireThreshold) {
                    cacheSegment();
                }
                return cur.startWith + cnt;
            } else {
                synchronized (genLocker) {
                    if (cur.count() < cur.incSpan) {
                        return nextId();
                    }

                    if (null != buf) {
                        cur = buf;
                        buf = null;
                        fireCache = false;
                        LOG.info("Switch cached segment for key: {}, segment: {}-{}", key, cur.startWith, cur.endWith);
                    } else {
                        cur = newSegment();
                        LOG.info("Cached segment is not ready, directly generate new segment for key: {}, segment: {}-{}", key, cur.startWith, cur.endWith);
                    }
                    return nextId();
                }
            }
        }

        private void cacheSegment() {
            if (!fireCache) {
                synchronized (cacheLocker) {
                    if (!fireCache) {
                        executor.submit(() -> cacheSegmentInternal());
                        fireCache = true;
                    }
                }
            }
        }

        private void cacheSegmentInternal() {
            buf = newSegment();
            LOG.info("Trigger to cache segment.");
        }

        private Segment newSegment() {
            for (;;) {
                SequenceKey sequence = sequenceKeyService.loadSequenceById(id);
                long newStartWith = sequence.getStartWith();
                long newEndWith = newStartWith + sequence.getIncSpan();
                if (sequenceKeyService.compareAndSet(id, newStartWith, newEndWith)) {
                    return new Segment(newStartWith, newEndWith - 1, sequence.getIncSpan());
                }
            }
        }
    }

    private class Segment {
        private final long startWith;
        private final long endWith;
        private final long incSpan;
        private final AtomicInteger counter;

        public Segment(long startWith, long endWith, long incSpan) {
            this.startWith = startWith;
            this.endWith = endWith;
            this.incSpan = incSpan;
            this.counter = new AtomicInteger(0);
        }

        public int counterGetAndInc() {
            return counter.getAndIncrement();
        }

        public int count() {
            return counter.get();
        }
    }
}