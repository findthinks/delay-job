package com.findthinks.delay.job.core.delay;

import com.findthinks.delay.job.core.repository.entity.SchedulerInfo;
import com.findthinks.delay.job.core.repository.mapper.SchedulerInfoExtMapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class SchedulerManager implements ISchedulerManager {

    @Resource
    private SchedulerInfoExtMapper schedulerInfoExtMapper;

    @Override
    public void createSchedulerInfo(SchedulerInfo schedulerInfo) {
        schedulerInfoExtMapper.insertSchedulerInfo(schedulerInfo);
    }

    @Override
    public int deleteExpiredSchedulerInfo(Long threshold) {
        return schedulerInfoExtMapper.deleteByLastHeartbeatTime(threshold);
    }

    @Override
    public void updateSchedulerInfoLastHeartbeatTime(Long lastHeartbeatTime, Integer schedulerId) {
        schedulerInfoExtMapper.updateSchedulerHeartbeatTime(lastHeartbeatTime, schedulerId);
    }

    @Override
    public List<Integer> loadAllSchedulerIds() {
        return Optional.of(schedulerInfoExtMapper.selectAllSchedulerIds()).orElse(Collections.emptyList());
    }

    @Override
    public SchedulerInfo loadSchedulerInfo(Integer id) {
        return schedulerInfoExtMapper.selectSchedulerById(id);
    }
}
