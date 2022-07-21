package com.findthinks.delay.job.share.exception;

/**
 * @author YuBo
 * @version $Id: TaskCanceledException.java, v0.1 Exp $$
 */
public class JobCanceledException extends RuntimeException {

    public JobCanceledException() {
        super();
    }

    public JobCanceledException(String message) {
        super(message);
    }

    public JobCanceledException(String message, Throwable cause) {
        super(message, cause);
    }
}
