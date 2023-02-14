package com.findthinks.delay.job.scheduler;

public enum CallbackProtocol {
    HTTP(0, "httpJobTrigger"),
    GRPC(5, "grpcJobTrigger"),
    KAFKA(10, "kafkaJobTrigger"),
    LOG(20, "logJobTrigger");

    private int protocol;

    private String trigger;

    CallbackProtocol(int protocol, String trigger) {
        this.protocol = protocol;
        this.trigger = trigger;
    }

    public static CallbackProtocol getByProtocol(int protocol) {
        for (CallbackProtocol pc : CallbackProtocol.values()) {
            if (pc.protocol == protocol) {
                return pc;
            }
        }
        throw new IllegalArgumentException(protocol + " mismatch");
    }

    public int getProtocol() {
        return protocol;
    }

    public String getTrigger() {
        return trigger;
    }
}
