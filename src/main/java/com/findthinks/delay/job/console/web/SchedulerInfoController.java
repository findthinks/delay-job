package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.web.rr.SchedulerInfoResp;
import com.findthinks.delay.job.scheduler.JobShardManager;
import com.findthinks.delay.job.scheduler.SchedulerManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX)
public class SchedulerInfoController {

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private JobShardManager jobShardManager;

    @GetMapping(value = "/schedulers")
    public List<SchedulerInfoResp> listSchedulers() {
        Map<Integer, List<Integer>> jobShardIds = jobShardManager.loadAllJobShardsGroupByCurServer();
        return schedulerManager.loadAllSchedulers().stream().map(scheduler ->
                new SchedulerInfoResp(
                        scheduler.getId(),
                        scheduler.getUuid(),
                        scheduler.getLastHeartbeatTime(),
                        scheduler.getRegister(),
                        scheduler.getIp(),
                        jobShardIds.get(scheduler.getId())))
                .collect(Collectors.toList());
    }
}