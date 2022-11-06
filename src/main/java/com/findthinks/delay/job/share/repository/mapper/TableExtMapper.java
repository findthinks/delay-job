package com.findthinks.delay.job.share.repository.mapper;

import org.apache.ibatis.annotations.Param;

public interface TableExtMapper {

    void createJobTable(@Param("jobShardId") Integer jobShardId);

    void addJobTablePartition(@Param("jobShardId") Integer jobShardId, @Param("dayOfSeconds") Long dayOfSeconds);

    void delJobTablePartition(@Param("jobShardId") Integer jobShardId, @Param("dayOfSeconds") Long dayOfSeconds);
}