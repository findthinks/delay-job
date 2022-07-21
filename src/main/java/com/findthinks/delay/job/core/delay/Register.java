package com.findthinks.delay.job.core.delay;

public enum Register {
    YES(1), NO(0);

    Register(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
