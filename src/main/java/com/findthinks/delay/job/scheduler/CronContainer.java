package com.findthinks.delay.job.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class CronContainer {

    @Resource
    private JobScheduler jobScheduler;

    @Scheduled(cron="${scheduler.job.cron.load:0 0/1 * * * ?}")
    public void loadDelayJob() {
        jobScheduler.doLoadDelayJob();
    }

    @Scheduled(cron="${scheduler.cron.heartbeat:2,7,12,17,22,27,32,37,42,47,52,57 * * * * ?}")
    public void heartbeat() {
        jobScheduler.doHeartbeat();
    }

    @Scheduled(cron="${scheduler.job.cron.retry:5,15,25,35,45,55 * * * * ?}")
    public void retry() {
        jobScheduler.doRetry();
    }

    @Scheduled(cron="${scheduler.job.cron.sync-state:5,15,25,35,45,55 * * * * ?}")
    public void syncState() {
        jobScheduler.doStateSync();
    }

    @Scheduled(cron="${scheduler.job.cron.translate:10,25,40,55 * * * * ?}")
    public void translate() {
        jobScheduler.doTranslateDisabledShardJobToOther();
    }
}
