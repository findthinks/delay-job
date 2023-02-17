package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("logJobTrigger")
public class EchoJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(EchoJobTrigger.class);

    @Override
    public TriggerResult trigger(Job job) {
        LOG.info("Job[outJobNo:{}] trigger success, CurrentTime:{}, TriggerTime:{}, CreateTime:{}.", job.getOutJobNo(), System.currentTimeMillis() / 1000, job.getTriggerTime() / 1000, job.getGmtCreate().getTime() / 1000);
        return TriggerResult.SUCCESS;
    }
}