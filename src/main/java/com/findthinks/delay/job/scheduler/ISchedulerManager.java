package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.repository.entity.SchedulerInfo;
import java.util.List;

public interface ISchedulerManager {

    void createSchedulerInfo(SchedulerInfo schedulerInfo);
    
    int deleteExpiredSchedulerInfo(Long threshold);
    
    void updateSchedulerInfoLastHeartbeatTime(Long lastHeartbeatTime, Integer schedulerId);

    List<Integer> loadAllSchedulerIds();
    
    SchedulerInfo loadSchedulerInfo(Integer id);

    List<SchedulerInfo> loadAllSchedulers();
}
