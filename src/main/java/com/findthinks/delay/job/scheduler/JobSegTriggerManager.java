package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.entity.JobSegTrigger;
import com.findthinks.delay.job.share.repository.mapper.JobSegTriggerExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class JobSegTriggerManager implements IJobSegTriggerManager {

    @Resource
    private JobSegTriggerExtMapper jobSegTriggerExtMapper;

    @Override
    public int deleteSegTrigger(Integer jobShardId) {
        return jobSegTriggerExtMapper.deleteSegTrigger(jobShardId);
    }

    @Override
    public List<JobSegTrigger> loadSegTriggers(List<Integer> shardIds) {
        List<JobSegTrigger> jobSegTriggers = jobSegTriggerExtMapper.selectJobSegTriggers(shardIds);
        return CollectionUtils.isEmpty(jobSegTriggers) ? Collections.EMPTY_LIST : jobSegTriggers;
    }
}
