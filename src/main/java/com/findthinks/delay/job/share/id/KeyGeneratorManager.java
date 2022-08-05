package com.findthinks.delay.job.share.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
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
        private static final int FIRE_BUF_THRESHOLD_RATIO = 20;
        private final Lock keyLocker = new ReentrantLock();
        private final Lock bufLocker = new ReentrantLock();
        private volatile long fireLeftThreshold;
        private boolean fire = false;
        private final int id;
        private final String key;
        private volatile Segment cur;
        private volatile Segment buf;

        public KeyGeneratorImpl(int id, String key, long startWith, long endWith, long incSpan) {
            this.id = id;
            this.key = key;
            this.cur = new Segment(new AtomicLong(startWith), endWith, incSpan);
            freshFireThreshold();
        }

        @Override
        public long nextId() {
            if (cur.startWith.get() <= cur.endWith) {
                if (cur.endWith - cur.startWith.get() < fireLeftThreshold) {
                    if (!fire) {
                        try {
                            bufLocker.lock();
                            if (!fire) {
                                LOG.info("Fire cache segment: {}", key);
                                executor.submit(() -> cacheSegment());
                                fire = true;
                            }
                        } finally {
                            bufLocker.unlock();
                        }
                    }
                }
                return cur.startWith.getAndIncrement();
            } else {
                try {
                    keyLocker.lock();
                    if (cur.startWith.get() <= cur.endWith) {
                        return nextId();
                    }

                    if (null != buf) {
                        LOG.info("Switch cached segment for key: {}", key);
                        cur = buf;
                        buf = null;
                        fire = false;
                    } else {
                        LOG.info("Cached segment is not ready, directly generate new segment for key: {}", key);
                        cur = newSegment();
                    }
                    freshFireThreshold();
                    return nextId();
                } finally {
                    keyLocker.unlock();
                }
            }
        }

        private void freshFireThreshold() {
            this.fireLeftThreshold = (this.cur.incSpan * FIRE_BUF_THRESHOLD_RATIO) / 100;
        }

        private void cacheSegment() {
            buf = newSegment();
        }

        private Segment newSegment() {
            for (;;) {
                SequenceKey sequence = sequenceKeyService.loadSequenceById(id);
                long newStartWith = sequence.getStartWith();
                long newEndWith = newStartWith + sequence.getIncSpan();
                if (sequenceKeyService.compareAndSet(id, newStartWith, newEndWith)) {
                    return new Segment(new AtomicLong(newStartWith), newEndWith - 1, sequence.getIncSpan());
                }
            }
        }
    }

    private class Segment {
        private AtomicLong startWith;
        private volatile long endWith;
        private volatile long incSpan;

        public Segment(AtomicLong startWith, long endWith, long incSpan) {
            this.startWith = startWith;
            this.endWith = endWith;
            this.incSpan = incSpan;
        }
    }
}