package com.findthinks.delay.job.core.id;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YuBo
 */
public class SequenceKeyServiceImpl implements SequenceKeyService {

    private SequenceKeyRepository sequenceKeyRepository;

    public SequenceKeyServiceImpl() {
    }

    public SequenceKeyServiceImpl(SequenceKeyRepository sequenceKeyRepository) {
        this.sequenceKeyRepository = sequenceKeyRepository;
    }

    @Override
    public SequenceKey loadSequenceKey(String key) {
        return sequenceKeyRepository.loadSequenceKey(key);
    }

    @Override
    public SequenceKey loadSequenceById(Integer id) {
        return sequenceKeyRepository.loadSequenceById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean compareAndSet(Integer id, Long oldValue, Long newValue) {
        return sequenceKeyRepository.compareAndSet(newValue, oldValue, id) > 0;
    }
}