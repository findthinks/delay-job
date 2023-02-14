package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.JobSegTrigger;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface JobSegTriggerExtMapper {

    int insertSegTrigger(JobSegTrigger segTrigger);

    JobSegTrigger selectJobSegTriggerByShardId(@Param("jobShardId") Integer jobShardId);

    List<JobSegTrigger> selectJobSegTriggers(@Param("jobShardIds") List<Integer> jobShardIds);

    int compareAndSet(Map<String, Object> params);

    int deleteSegTrigger(@Param("jobShardId") Integer jobShardId);
}