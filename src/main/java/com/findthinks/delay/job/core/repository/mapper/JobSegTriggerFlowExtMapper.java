package com.findthinks.delay.job.core.repository.mapper;

import com.findthinks.delay.job.core.repository.entity.JobSegTriggerFlow;
import java.util.List;
import java.util.Map;

public interface JobSegTriggerFlowExtMapper {

    List<JobSegTriggerFlow> loadRecentlyFlows(Map<String, Object> parameters);

    List<JobSegTriggerFlow> loadRetryFlows(Map<String, Object> parameters);

    int insertSegTriggerFlow(JobSegTriggerFlow flow);

    int compareAndSet(Map<String, Object> params);
}