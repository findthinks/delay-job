package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import java.util.List;

/**
 * @author YuBo
 */
public interface IJobSegTriggerFlowManager {

    List<JobSegTriggerFlow> loadRecentlySegments(int shards, long minTriggerTime);

    List<JobSegTriggerFlow> loadRetrySegments(List<Integer> shards, Long startTime, Long endTime);

    boolean updateSegmentState(JobSegTriggerFlow flow, TriggerFLowState dst);

    int insertSegTriggerFlow(JobSegTriggerFlow flow);
}