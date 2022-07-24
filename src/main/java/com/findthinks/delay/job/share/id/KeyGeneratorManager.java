package com.findthinks.delay.job.share.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author YuBo
 */
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
            boolean result = false;
            try {
                result = locker.tryLock(15, TimeUnit.SECONDS);
                if (result) {
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
                }
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            } finally {
                if (result) {
                    locker.unlock();
                } else {
                    if (Thread.interrupted()) {
                        throw new RuntimeException("Interrupt to get KeyGenerator for " + key);
                    } else {
                        throw new RuntimeException("Timeout to get KeyGenerator for " + key);
                    }
                }
            }
        }
        return keyGenerator;
    }

    private class KeyGeneratorImpl implements KeyGenerator {
        private static final int FIRE_BUF_THRESHOLD_RATIO = 90;
        private final Lock  keyLocker = new ReentrantLock();
        private long fireThreshold;
        private boolean fired = false;
        private int id;
        private String key;
        private Segment cur;
        private Segment buf;

        public KeyGeneratorImpl(int id, String key, long startWith, long endWith, long incSpan) {
            this.id = id;
            this.key = key;
            this.cur = new Segment(startWith, endWith, incSpan);
            freshFireThreshold();
        }

        @Override
        public long nextId() {
            boolean result = false;
            try {
                result = keyLocker.tryLock(15L, TimeUnit.SECONDS);
                if (result) {
                    if (cur.startWith <= cur.endWith) {
                        if (!fired && (cur.endWith - cur.startWith < fireThreshold)) {
                            LOG.debug("Fire cache segment: {}", key);
                            executor.submit(new CacheSegmentJob());
                            fired = true;
                        }
                        return cur.startWith++;
                    } else {
                        if (null != buf) {
                            LOG.debug("Switch cached segment for key: {}", key);
                            cur = buf;
                            buf = null;
                            fired = false;
                            freshFireThreshold();
                            return nextId();
                        }

                        LOG.debug("Cached segment is not ready, directly generate new segment for key: {}", key);
                        cur = newSegment();
                        fired = false;
                        freshFireThreshold();
                        return nextId();
                    }
                } else {
                    throw new RuntimeException("Timeout to generate key id for " + key);
                }
            } catch (InterruptedException ignore) {
                throw new RuntimeException("Interrupt to generate key id for " + key);
            } finally {
                if (result) {
                    keyLocker.unlock();
                }
            }
        }

        private void freshFireThreshold() {
            this.fireThreshold = (this.cur.incSpan * FIRE_BUF_THRESHOLD_RATIO) / 100;
        }

        private void cacheSegment() {
            boolean ret = false;
            try {
                ret = keyLocker.tryLock(15L, TimeUnit.SECONDS);
                if (ret) {
                    buf = newSegment();
                    LOG.debug("New segment generate success. key: {}", key);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupt to buf segment for " + key);
            } finally {
                if (ret) {
                    keyLocker.unlock();
                }
            }
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

        private class CacheSegmentJob implements Runnable {
            @Override
            public void run() {
                cacheSegment();
            }
        }
    }

    private class Segment {
        private long startWith;
        private long endWith;
        private long incSpan;

        public Segment(long startWith, long endWith, long incSpan) {
            this.startWith = startWith;
            this.endWith = endWith;
            this.incSpan = incSpan;
        }
    }
}