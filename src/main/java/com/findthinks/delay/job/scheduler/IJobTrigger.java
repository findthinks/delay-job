package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.Job;

import java.util.List;

public interface IJobTrigger {

    TriggerResult triggerJobs(List<Job> jobs);
}
