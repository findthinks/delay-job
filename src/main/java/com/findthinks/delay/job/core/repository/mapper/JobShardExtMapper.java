package com.findthinks.delay.job.core.repository.mapper;

import com.findthinks.delay.job.core.repository.entity.JobShard;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JobShardExtMapper {

    List<JobShard> selectJobShardByCond(Map<String, Object> params);

    int updateJobCurServer(Map<String, Object> params);

    int updateJobReqServer(Map<String, Object> params);

    int updateReqServerToCurServer(@Param("ids") List<Integer> ids);

    int updateCurServerByCond(@Param("schedulerIds") List<Integer> schedulerIds);

    int selectJobShardCount();
}