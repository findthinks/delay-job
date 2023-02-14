package com.findthinks.delay.job.scheduler;

public enum JobState {
    SUBMIT(10, "提交"),
    RETRY(20, "重试"), //暂未使用
    SUCCESS(30, "成功"),
    FAIL(40, "失败"),
    CANCEL(50, "取消");

    private int code;
    private String name;

    JobState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static JobState getStateByCode(int code) {
        for (JobState state : JobState.values()) {
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
