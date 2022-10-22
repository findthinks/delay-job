package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.JobShardInfoResp;
import com.findthinks.delay.job.scheduler.JobScheduler;
import com.findthinks.delay.job.scheduler.JobShardManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX)
@Validated
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
    public void enableJobShard(
            @PathVariable("shardId")
            @Max(value = 255, message = "分片号范围[1,255]")
            @Min(value = 1, message = "分片号范围[1,255]") Integer shardId) {
        jobScheduler.startJobShard(shardId);
    }

    /**
     * 修改任务分片
     * @param shardId: 分片ID
     */
    @PutMapping(value = "/job/shard/{shardId}/disable")
    public void disableJobShard(
            @PathVariable("shardId")
            @Max(value = 255, message = "分片号范围[1,255]")
            @Min(value = 1, message = "分片号范围[1,255]") Integer shardId) {
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
