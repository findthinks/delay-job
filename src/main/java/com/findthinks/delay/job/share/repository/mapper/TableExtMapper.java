package com.findthinks.delay.job.share.repository.mapper;

import org.apache.ibatis.annotations.Param;

public interface TableExtMapper {

    void createJobTable(@Param("jobShardId") Integer jobShardId);
}