package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import java.util.List;

public interface IJobSegTriggerFlowManager {

    List<JobSegTriggerFlow> loadUnCompleteSegments(Long startTime, Long endTime);

    List<JobSegTriggerFlow> loadRetrySegments(List<Integer> shards, Long startTime, Long endTime, Integer limitSegNums);

    boolean updateSegmentState(JobSegTriggerFlow flow, TriggerFLowState dst);

    int insertSegTriggerFlow(JobSegTriggerFlow flow);
}