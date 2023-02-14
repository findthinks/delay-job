package com.findthinks.delay.job.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LifeCycle {
    
    private static final Logger LOG = LoggerFactory.getLogger(LifeCycle.class);
    
    public void start() {
        try {
            LOG.info("Start server ...");
            doStart();
            LOG.info("Start server success.");
        } catch (Exception ex) {
            LOG.error("Start servererror.", ex);
        }
    }
    
    public void stop() {
        try {
            LOG.info("Stop server...");
            doStop();
            LOG.info("Stop server success.");
        } catch (Exception ex) {
            LOG.error("Stop server error.", ex);
        }
    }
    
    abstract public void doStart();

    abstract public void doStop();
}
