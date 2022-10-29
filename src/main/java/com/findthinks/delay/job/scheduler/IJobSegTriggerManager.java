package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.JobSegTrigger;
import java.util.List;

public interface IJobSegTriggerManager {

    int deleteSegTrigger(Integer jobShardId);

    List<JobSegTrigger> loadSegTriggers(List<Integer> shardIds);
}
