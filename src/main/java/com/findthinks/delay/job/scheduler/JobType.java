package com.findthinks.delay.job.scheduler;

public enum JobType {
    NORMAL(5, "普通任务"),
    PAUSABLE(10, "可暂停计时任务");

    private int code;
    private String name;

    JobType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static JobType getTypeByCode(int code) {
        for (JobType state : JobType.values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        throw new IllegalArgumentException(code + " mismatch");
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
