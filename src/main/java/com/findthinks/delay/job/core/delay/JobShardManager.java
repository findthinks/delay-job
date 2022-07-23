package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.JobShard;
import com.findthinks.delay.job.core.repository.mapper.JobShardExtMapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobShardManager implements IJobShardManager {

    private static final int NORMAL_STATE = 5;

    @Resource
    private JobShardExtMapper jobShardExtMapper;

    @Override
    public int selectJobShardCount() {
        return jobShardExtMapper.selectJobShardCount();
    }

    @Override
    public List<JobShard> loadReceivingJobShards() {
        Map<String, Object> params = new HashMap<>();
        params.put("state", NORMAL_STATE);
        return jobShardExtMapper.selectJobShardByCond(params);
    }

    @Override
    public List<JobShard> loadConsumingJobShards() {
        Map<String, Object> params = new HashMap<>();
        params.put("state", NORMAL_STATE);
        return jobShardExtMapper.selectJobShardByCond(params);
    }

    @Override
    public List<JobShard> loadAssignedJobShards(Integer schedulerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("schedulerId", schedulerId);
        params.put("state", NORMAL_STATE);
        return jobShardExtMapper.selectJobShardByCond(params);
    }

    @Override
    public int updateJobShardCurServer(Integer shardId, Integer curServer) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", shardId);
        params.put("curServer", curServer);
        return jobShardExtMapper.updateJobShardCurServer(params);
    }

    @Override
    public int updateJobShardReqServer(Integer jobShardId, Integer reqServer) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobShardId);
        params.put("reqServer", reqServer);
        return jobShardExtMapper.updateJobShardReqServer(params);
    }

    @Override
    public int updateReqServerToCurServer(List<Integer> ids) {
        return jobShardExtMapper.updateReqServerToCurServer(ids);
    }

    @Override
    public int updateJobShardByCondition(List<Integer> schedulerIds) {
        // update job_shard set cur_server = -1 where cur_server not in (schedulerIds);
        return jobShardExtMapper.updateCurServerByCond(schedulerIds);
    }

    @Override
    public int updateJobShardState(Integer jobShardId, Integer state) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobShardId);
        params.put("state", state);
        return jobShardExtMapper.updateJobShardState(params);
    }
}
