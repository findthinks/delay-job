package com.findthinks.delay.job.scheduler;

public enum JobShardState {
    ENABLED(5, "启用"),
    TRANSLATING(10, "任务迁移中"),
    DISABLED(15, "停用");

    private int code;
    private String name;

    JobShardState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static JobShardState getStateByCode(int code) {
        for (JobShardState state : JobShardState.values()) {
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
