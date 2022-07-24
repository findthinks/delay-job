package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import java.util.List;

/**
 * @author YuBo
 */
public interface IJobSegTriggerFlowManager {

    List<JobSegTriggerFlow> loadRecentlyFlows(int shards, long minTriggerTime);

    List<JobSegTriggerFlow> loadRetryFlows(List<Integer> shards, Long startTime, Long endTime);

    boolean updateTaskFlowState(JobSegTriggerFlow flow, TriggerFLowState dst);

    int insertSegTriggerFlow(JobSegTriggerFlow flow);
}