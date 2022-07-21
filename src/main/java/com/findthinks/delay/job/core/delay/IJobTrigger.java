package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.Job;

public interface IJobTrigger {

    TriggerResult triggerJob(Job job);
}
