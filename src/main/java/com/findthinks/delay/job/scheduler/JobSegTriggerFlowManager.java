package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.CollectionUtils;
import com.findthinks.delay.job.share.repository.entity.JobSegTriggerFlow;
import com.findthinks.delay.job.share.repository.mapper.JobSegTriggerFlowExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

@Service
public class JobSegTriggerFlowManager implements IJobSegTriggerFlowManager {

    @Resource
    private JobSegTriggerFlowExtMapper jobSegTriggerFlowExtMapper;

    @Override
    public List<JobSegTriggerFlow> loadUnCompleteSegments(Long startTime, Long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("timeStart", startTime);
        params.put("timeEnd", endTime);
        List<JobSegTriggerFlow> segments = jobSegTriggerFlowExtMapper.loadUnCompleteFlows(params);
        return CollectionUtils.isEmpty(segments) ? Collections.EMPTY_LIST : segments;
    }

    @Override
    public List<JobSegTriggerFlow> loadRetrySegments(List<Integer> shards, Long startTime, Long endTime, Integer limitSegNums) {
        if (CollectionUtils.isEmpty(shards)) {
            return Collections.EMPTY_LIST;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("shardIds", shards);
        params.put("timeStart", startTime);
        params.put("timeEnd", endTime);
        params.put("maxSegments", limitSegNums);
        return jobSegTriggerFlowExtMapper.loadRetryFlows(params);
    }

    @Override
    public boolean updateSegmentState(JobSegTriggerFlow flow, TriggerFLowState dst) {
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