package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("logJobTrigger")
public class EchoJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(EchoJobTrigger.class);

    @Override
    public TriggerResult triggerJob(Job job) {
        LOG.info("Job[Shard:{}, Job:{}, TriggerTime:{}, CurrentTime:{}] trigger success.", job.getJobShardId(), job.getId(), job.getTriggerTime() / 1000, System.currentTimeMillis() / 1000);
        return TriggerResult.SUCCESS;
    }
}
