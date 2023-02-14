package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class SchedulerInitializer extends LifeCycle implements CommandLineRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerInitializer.class);

    @Resource
    private JobScheduler scheduler;
    
    @Override
    public void run(String... args) {
        doStart();
    }

    @Override
    public void doStop() {
        scheduler.stop();
    }

    @Override
    public void doStart() {
        try {
            refreshScheduler();
        } catch (Exception ex) {
            LOG.error("Refresh schedulers error.", ex);
            throw new SystemException(ex);
        }
    }

    private void refreshScheduler() {
        scheduler.start();
    }
}