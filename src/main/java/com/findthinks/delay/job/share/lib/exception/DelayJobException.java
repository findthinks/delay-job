package com.findthinks.delay.job.share.lib.exception;

import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;

public class DelayJobException extends RuntimeException {

    private ExceptionEnum eenum;

    public DelayJobException(ExceptionEnum eenum) {
        super(eenum.getDesc());
        this.eenum = eenum;
    }

    public DelayJobException(ExceptionEnum exception, String message) {
        super(message);
        this.eenum = exception;
    }

    public DelayJobException(ExceptionEnum exception, String message, Throwable cause) {
        super(message, cause);
        this.eenum = exception;
    }

    public ExceptionEnum getExceptionEnum() {
        return this.eenum;
    }
}
