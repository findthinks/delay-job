package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.JobShardInfoResp;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.scheduler.JobShardManager;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class JobShardInfoController {

    @Resource
    private JobShardManager jobShardManager;

    @Resource
    private JobScheduler jobScheduler;

    /**
     * 创建任务分片
     */
    @PostMapping(value = "/job/shard")
    public void addJobShard() {
        jobShardManager.createJobShard();
    }

    /**
     * 修改任务分片
     * @param shardId: 分片ID
     */
    @PutMapping(value = "/job/shard/{shardId}/enable")
    public void enableJobShard(@PathVariable("shardId") Integer shardId) {
        jobScheduler.startJobShard(shardId);
    }

    /**
     * 修改任务分片
     * @param shardId: 分片ID
     */
    @PutMapping(value = "/job/shard/{shardId}/disable")
    public void disableJobShard(@PathVariable("shardId") Integer shardId) {
        jobScheduler.stopJobShard(shardId);
    }

    /**
     * 查询任务分片列表
     */
    @GetMapping(value = "/job/shards")
    public List<JobShardInfoResp> listJobShards() {
        return jobShardManager.loadAllJobShards().stream().map(shard ->
                new JobShardInfoResp(
                        shard.getId(),
                        shard.getCurServer() ,
                        shard.getReqServer(),
                        shard.getState()))
                .collect(Collectors.toList());
    }
}
