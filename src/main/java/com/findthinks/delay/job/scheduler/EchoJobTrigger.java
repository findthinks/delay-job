package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("logJobTrigger")
public class EchoJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(EchoJobTrigger.class);

    @Override
    public TriggerResult triggerJob(Job job) {
        LOG.info("Job[Shard:{}, Job:{}, , CreateTime: {}, TriggerTime:{}, CurrentTime:{}] trigger success.", job.getJobShardId(), job.getId(), job.getTriggerTime() / 1000, System.currentTimeMillis() / 1000, job.getGmtCreate().getTime() / 1000);
        return TriggerResult.SUCCESS;
    }
}