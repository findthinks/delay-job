package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.mapper.JobSegTriggerExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class JobSegTriggerManager implements IJobSegTriggerManager {

    @Resource
    private JobSegTriggerExtMapper jobSegTriggerExtMapper;

    @Override
    public int deleteSegTrigger(Integer jobShardId) {
        return jobSegTriggerExtMapper.deleteSegTrigger(jobShardId);
    }
}
