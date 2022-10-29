package com.findthinks.delay.job.share.lib.exception;

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
