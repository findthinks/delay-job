package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.JobShard;
import java.util.List;

public interface IJobShardManager {

    int selectJobShardCount();

    List<JobShard> loadReceivingJobShards();

    List<JobShard> loadConsumingJobShards();

    List<JobShard> loadAssignedJobShards(Integer schedulerId);

    int updateJobShardCurServer(Integer jobShardId, Integer curServer);

    int updateJobShardReqServer(Integer jobShardId, Integer reqServer);

    int updateReqServerToCurServer(List<Integer> ids);

    int updateJobShardByCondition(List<Integer> schedulerIds);

    int updateJobShardState(Integer jobShardId, Integer state);
}