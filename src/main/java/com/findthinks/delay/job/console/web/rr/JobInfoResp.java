package com.findthinks.delay.job.console.web.rr;

import java.util.Date;

public class JobInfoResp {

    private Long id;

    private Integer jobShardId;

    private String outJobNo;

    private Long triggerTime;

    private Integer type;

    private Integer state;

    private Integer retryTimes;

    private Integer callbackProtocol;

    private String callbackEndpoint;

    private String jobInfo;

    private Date gmtCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJobShardId() {
        return jobShardId;
    }

    public void setJobShardId(Integer jobShardId) {
        this.jobShardId = jobShardId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getCallbackProtocol() {
        return callbackProtocol;
    }

    public void setCallbackProtocol(Integer callbackProtocol) {
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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
