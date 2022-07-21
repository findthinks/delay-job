package com.findthinks.delay.job.share.exception;

import com.findthinks.delay.job.share.enums.ExceptionEnum;

/**
 * @author YuBo
 */
public class ParamsException extends DelayJobException {

    public ParamsException() {
        super(ExceptionEnum.INVALID_PARAMS);
    }

    public ParamsException(String message) {
        super(ExceptionEnum.INVALID_PARAMS, message);
    }
}
