package com.findthinks.delay.job.scheduler;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FacadeJob {

    @Size(max = 32, min = 1, message = ":所含字符数在[1,32]的范围内")
    private String outJobNo;

    private Integer type = 5;

    @NotNull(message = ":不能为空，秒级时间搓")
    private Long triggerTime;

    @NotNull(message = ":不能为空，取值范围:LOG,HTTP,GRPC")
    private String callbackProtocol;

    @NotNull(message = ":不能为空")
    @Size(max = 64, min = 1, message = ":所含字符数在[1,64]的范围内")
    private String callbackEndpoint;

    private String jobInfo;

    public FacadeJob() {
    }

    public FacadeJob(String outJobNo, Integer type, Long triggerTime, String callbackProtocol, String callbackEndpoint, String jobInfo) {
        this.outJobNo = outJobNo;
        this.triggerTime = triggerTime;
        this.callbackProtocol = callbackProtocol;
        this.callbackEndpoint = callbackEndpoint;
        this.jobInfo = jobInfo;
        this.type = type;
    }

    public static FacadeJob create(String outJobNo, Integer type, Long triggerTime, String callbackProtocol, String callbackEndpoint, String jobInfo) {
        return new FacadeJob(outJobNo, type, triggerTime, callbackProtocol, callbackEndpoint, jobInfo);
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        if (null == type) {
            this.type = JobType.NORMAL.getCode();
        } else {
            this.type = type;
        }
    }
}