package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.JobSegTrigger;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

public interface JobSegTriggerExtMapper {

    int insertSegTrigger(JobSegTrigger segTrigger);

    JobSegTrigger selectOneSegTrigger();

    JobSegTrigger selectJobSegTriggerByShardId(@Param("jobShardId") Integer jobShardId);

    int compareAndSet(Map<String, Object> params);

    int deleteSegTrigger(@Param("jobShardId") Integer jobShardId);
}