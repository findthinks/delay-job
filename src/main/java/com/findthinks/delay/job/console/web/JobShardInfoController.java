package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.JobShardInfoReq;
import com.findthinks.delay.job.console.web.rr.JobShardInfoResp;
import com.findthinks.delay.job.core.delay.JobShardManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class JobShardInfoController {

    @Resource
    private JobShardManager jobShardManager;

    /**
     * 创建任务分片
     * @param shard
     */
    @PostMapping(value = "/job/shard")
    public void addJobShard(@RequestBody JobShardInfoReq shard) {

    }

    /**
     * 修改任务分片
     * @param shardId: 分片ID
     * @param state: 5-启用，15停用
     */
    @PutMapping(value = "/job/shard/{shardId}/{state}")
    public void updateJobShard(@PathVariable("shardId") Integer shardId, @PathVariable("state") Integer state) {
    }

    /**
     * 查询任务分片列表
     */
    @PutMapping(value = "/job/shards")
    public List<JobShardInfoResp> listJobShards() {
        return null;
    }
}
