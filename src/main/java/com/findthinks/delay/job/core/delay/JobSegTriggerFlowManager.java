package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.JobSegTriggerFlow;
import com.findthinks.delay.job.core.repository.mapper.JobSegTriggerFlowExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author YuBo
 */
@Service
public class JobSegTriggerFlowManager implements IJobSegTriggerFlowManager {

    @Resource
    private JobSegTriggerFlowExtMapper jobSegTriggerFlowExtMapper;

    @Override
    public List<JobSegTriggerFlow> loadRecentlyFlows(int shards, long minTriggerTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("flows", shards);
        params.put("minTriggerTime", minTriggerTime);
        return jobSegTriggerFlowExtMapper.loadRecentlyFlows(params);
    }

    @Override
    public List<JobSegTriggerFlow> loadRetryFlows(List<Integer> shards, Long startTime, Long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("shardIds", shards);
        params.put("timeStart", startTime);
        params.put("timeEnd", endTime);
        return jobSegTriggerFlowExtMapper.loadRetryFlows(params);
    }

    @Override
    public boolean updateTaskFlowState(JobSegTriggerFlow flow, TriggerFLowState dst) {
        Map<String, Object> params = new HashMap<>();
        params.put("newState", dst.COMPLETE.getCode());
        params.put("state", flow.getState());
        params.put("id", flow.getId());
        return jobSegTriggerFlowExtMapper.compareAndSet(params) == 1;
    }

    @Override
    public int insertSegTriggerFlow(JobSegTriggerFlow flow) {
        return jobSegTriggerFlowExtMapper.insertSegTriggerFlow(flow);
    }
}