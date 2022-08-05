package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.JobShard;
import java.util.List;
import java.util.Map;

public interface IJobShardManager {

    void createJobShard();

    int selectJobShardCount();

    List<JobShard> loadEnabledJobShards();

    List<JobShard> loadTranslatingJobShards();

    List<JobShard> loadAllJobShards();

    Map<Integer, List<Integer>> loadAllJobShardsGroupByCurServer();

    List<JobShard> loadAssignedJobShards(Integer schedulerId);

    int updateJobShardCurServer(Integer jobShardId, Integer curServer);

    int updateJobShardReqServer(Integer jobShardId, Integer reqServer);

    int updateReqServerToCurServer(List<Integer> ids);

    int updateJobShardByCondition(List<Integer> schedulerIds);

    int updateJobShardState(Integer jobShardId, Integer oldState, Integer newState);
}