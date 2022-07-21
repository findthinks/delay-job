package com.findthinks.delay.job.share.exception;

import com.findthinks.delay.job.share.enums.ExceptionEnum;

/**
 * @author YuBo
 */
public class SystemException extends DelayJobException {

    public SystemException() {
        super(ExceptionEnum.UNKNOWN_ERROR);
    }

    public SystemException(String message) {
        super(ExceptionEnum.UNKNOWN_ERROR, message);
    }

    public SystemException(Throwable thrown) {
        super(ExceptionEnum.UNKNOWN_ERROR, thrown.getMessage(), thrown);
    }

    public SystemException(String message, Throwable thrown) {
        super(ExceptionEnum.UNKNOWN_ERROR, message, thrown);
    }
}
