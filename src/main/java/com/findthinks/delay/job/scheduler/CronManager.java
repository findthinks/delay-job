package com.findthinks.delay.job.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class CronManager {

    @Resource
    private JobScheduler jobScheduler;

    @Scheduled(cron="${scheduler.job.cron.load}")
    public void loadDelayJob() {
        jobScheduler.doLoadDelayJob();
    }

    @Scheduled(cron="${scheduler.cron.heartbeat}")
    public void heartbeat() {
        jobScheduler.doHeartbeat();
    }

    @Scheduled(cron="${scheduler.job.cron.retry}")
    public void retry() {
        jobScheduler.doRetry();
    }

    @Scheduled(cron="${scheduler.job.cron.sync-state}")
    public void syncState() {
        jobScheduler.doStateSync();
    }
}
