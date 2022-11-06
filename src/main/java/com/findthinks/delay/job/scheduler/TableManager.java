package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.utils.DateUtils;
import com.findthinks.delay.job.share.repository.mapper.TableExtMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class TableManager implements ITableManager {

    @Resource
    private TableExtMapper tableExtMapper;

    @Resource
    private IJobShardManager jobShardManager;

    @Override
    public void addJobPartition() {
        jobShardManager.loadAllJobShards().forEach(jobShard -> tableExtMapper.addJobTablePartition(jobShard.getId(), DateUtils.getEndDayOfCurrentWeek()));
    }
}