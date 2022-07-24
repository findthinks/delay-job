package com.findthinks.delay.job.scheduler;

public class FacadeJob {
    private String outJobNo;

    private Long triggerTime;

    private String callbackProtocol;

    private String callbackEndpoint;

    private String jobInfo;

    public FacadeJob() {
    }

    public FacadeJob(String outJobNo, Long triggerTime, String callbackProtocol, String callbackEndpoint, String jobInfo) {
        this.outJobNo = outJobNo;
        this.triggerTime = triggerTime;
        this.callbackProtocol = callbackProtocol;
        this.callbackEndpoint = callbackEndpoint;
        this.jobInfo = jobInfo;
    }

    public static FacadeJob create(String outJobNo, Long triggerTime, String callbackProtocol, String callbackEndpoint, String jobInfo) {
        return new FacadeJob(outJobNo, triggerTime, callbackProtocol, callbackEndpoint, jobInfo);
    }

    public String getOutJobNo() {
        return outJobNo;
    }

    public void setOutJobNo(String outJobNo) {
        this.outJobNo = outJobNo;
    }

    public Long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getCallbackProtocol() {
        return callbackProtocol;
    }

    public void setCallbackProtocol(String callbackProtocol) {
        this.callbackProtocol = callbackProtocol;
    }

    public String getCallbackEndpoint() {
        return callbackEndpoint;
    }

    public void setCallbackEndpoint(String callbackEndpoint) {
        this.callbackEndpoint = callbackEndpoint;
    }

    public String getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }
}