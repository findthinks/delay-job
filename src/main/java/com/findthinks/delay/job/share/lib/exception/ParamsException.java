package com.findthinks.delay.job.share.lib.exception;

import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;

public class ParamsException extends DelayJobException {

    public ParamsException() {
        super(ExceptionEnum.INVALID_PARAMS);
    }

    public ParamsException(String message) {
        super(ExceptionEnum.INVALID_PARAMS, message);
    }
}
