package com.findthinks.delay.job.share.repository.mapper;

import com.findthinks.delay.job.share.repository.entity.SchedulerInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SchedulerInfoExtMapper {

    int insertSchedulerInfo(SchedulerInfo schedulerInfo);

    int deleteByLastHeartbeatTime(@Param("threshold") Long threshold);

    int updateSchedulerHeartbeatTime(@Param("lastHeartbeatTime") Long lastHeartbeatTime, @Param("schedulerId") Integer schedulerId);

    List<Integer> selectAllSchedulerIds();

    List<SchedulerInfo> selectAllSchedulers();

    SchedulerInfo selectSchedulerById(@Param("schedulerId") Integer schedulerId);
}