package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("logJobTrigger")
public class EchoJobTrigger implements IJobTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(EchoJobTrigger.class);

    @Override
    public TriggerResult triggerJobs(List<Job> jobs) {
        LOG.info("Jobs[{}] trigger success.", jobs.toArray());
        return TriggerResult.SUCCESS;
    }
}