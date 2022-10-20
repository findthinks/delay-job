package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;

public interface IJobTrigger {

    TriggerResult trigger(Job job);
}
