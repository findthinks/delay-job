package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import java.util.List;
import java.util.Map;

public interface JobSegTriggerFlowExtMapper {

    List<JobSegTriggerFlow> loadRetryFlows(Map<String, Object> parameters);

    List<JobSegTriggerFlow> loadUnCompleteFlows(Map<String, Object> parameters);

    int insertSegTriggerFlow(JobSegTriggerFlow flow);

    int compareAndSet(Map<String, Object> params);
}