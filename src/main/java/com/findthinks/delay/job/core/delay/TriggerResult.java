package com.findthinks.delay.job.core.delay;

public class TriggerResult {

    public static final TriggerResult SUCCESS = new TriggerResult(0, "OK");

    private Integer code;

    private String msg;

    public TriggerResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccessful() {
        return 0 == code;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}