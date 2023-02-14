package com.findthinks.delay.job.share.repository.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "job_seg_trigger_flow")
public class JobSegTriggerFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_shard_id")
    private Integer jobShardId;

    @Column(name = "trigger_time_start")
    private Long triggerTimeStart;

    @Column(name = "trigger_time_end")
    private Long triggerTimeEnd;

    private Integer state;

    @Column(name = "retry_times")
    private Integer retryTimes;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
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
     * @return trigger_time_start
     */
    public Long getTriggerTimeStart() {
        return triggerTimeStart;
    }

    /**
     * @param triggerTimeStart
     */
    public void setTriggerTimeStart(Long triggerTimeStart) {
        this.triggerTimeStart = triggerTimeStart;
    }

    /**
     * @return trigger_time_end
     */
    public Long getTriggerTimeEnd() {
        return triggerTimeEnd;
    }

    /**
     * @param triggerTimeEnd
     */
    public void setTriggerTimeEnd(Long triggerTimeEnd) {
        this.triggerTimeEnd = triggerTimeEnd;
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