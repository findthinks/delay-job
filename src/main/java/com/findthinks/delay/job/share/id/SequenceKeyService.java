package com.findthinks.delay.job.share.id;

public interface SequenceKeyService {

    SequenceKey loadSequenceKey(String key);

    SequenceKey loadSequenceById(Integer id);

    boolean compareAndSet(Integer id, Long oldValue, Long newValue);
}