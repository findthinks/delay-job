package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;

public class TriggerResult {

    public static final TriggerResult SUCCESS = new TriggerResult("ok", "success");
    public static final TriggerResult FAIL = new TriggerResult("fail", "unknown_error");

    private String code;

    private String message;

    public TriggerResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccessful() {
        return ExceptionEnum.SUCCESS.getCode().equals(code.toLowerCase());
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}