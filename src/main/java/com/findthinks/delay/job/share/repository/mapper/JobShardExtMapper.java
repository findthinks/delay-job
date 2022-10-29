package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.JobShard;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JobShardExtMapper {

    int insertJobShard(JobShard jobShard);

    List<JobShard> selectJobShardByCond(Map<String, Object> params);

    int updateJobShardCurServer(Map<String, Object> params);

    int updateJobShardReqServer(Map<String, Object> params);

    int updateJobShardState(Map<String, Object> params);

    int updateReqServerToCurServer(@Param("ids") List<Integer> ids);

    int updateCurServerByCond(@Param("schedulerIds") List<Integer> schedulerIds);

    int selectJobShardCount();
}