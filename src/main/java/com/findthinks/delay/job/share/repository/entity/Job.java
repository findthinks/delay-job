package com.findthinks.delay.job.share.repository.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "job_0")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_shard_id")
    private Integer jobShardId;

    @Column(name = "out_job_no")
    private String outJobNo;

    @Column(name = "trigger_time")
    private Long triggerTime;

    @Column(name = "pause_time")
    private Long pauseTime;

    private Integer type;

    private Integer state;

    @Column(name = "retry_times")
    private Integer retryTimes;

    @Column(name = "callback_protocol")
    private Integer callbackProtocol;

    @Column(name = "callback_endpoint")
    private String callbackEndpoint;

    @Column(name = "job_info")
    private String jobInfo;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return job_shard_id
     */
    public Integer getJobShardId() {
        return jobShardId;
    }

    /**
     * @param jobShardId
     */
    public void setJobShardId(Integer jobShardId) {
        this.jobShardId = jobShardId;
    }

    /**
     * @return out_job_no
     */
    public String getOutJobNo() {
        return outJobNo;
    }

    /**
     * @param outJobNo
     */
    public void setOutJobNo(String outJobNo) {
        this.outJobNo = outJobNo == null ? null : outJobNo.trim();
    }

    /**
     * @return trigger_time
     */
    public Long getTriggerTime() {
        return triggerTime;
    }

    /**
     * @param triggerTime
     */
    public void setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(Long pauseTime) {
        this.pauseTime = pauseTime;
    }

    /**
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return retry_times
     */
    public Integer getRetryTimes() {
        return retryTimes;
    }

    /**
     * @param retryTimes
     */
    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     * @return callback_protocol
     */
    public Integer getCallbackProtocol() {
        return callbackProtocol;
    }

    /**
     * @param callbackProtocol
     */
    public void setCallbackProtocol(Integer callbackProtocol) {
        this.callbackProtocol = callbackProtocol;
    }

    /**
     * @return callback_endpoint
     */
    public String getCallbackEndpoint() {
        return callbackEndpoint;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @param callbackEndpoint
     */
    public void setCallbackEndpoint(String callbackEndpoint) {
        this.callbackEndpoint = callbackEndpoint == null ? null : callbackEndpoint.trim();
    }

    /**
     * @return job_info
     */
    public String getJobInfo() {
        return jobInfo;
    }

    /**
     * @param jobInfo
     */
    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo == null ? null : jobInfo.trim();
    }

    /**
     * @return gmt_create
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * @param gmtCreate
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}