package com.findthinks.delay.job.scheduler;

import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;

public class TriggerResult {

    public static final TriggerResult SUCCESS = new TriggerResult("ok", "Success");

    private String code;

    private String msg;

    public TriggerResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccessful() {
        return ExceptionEnum.SUCCESS.getCode().equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}