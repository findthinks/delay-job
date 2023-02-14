package com.findthinks.delay.job.scheduler;

public enum TriggerFLowState {
    PROCESS(20,"执行中"),
    RETRY(30,"补偿中"),
    COMPLETE(40,"完成");

    private int code;

    private String name;

    TriggerFLowState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}