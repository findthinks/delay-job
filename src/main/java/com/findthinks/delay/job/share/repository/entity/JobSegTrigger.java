package com.findthinks.delay.job.share.repository.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "job_seg_trigger")
public class JobSegTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_shard_id")
    private Integer jobShardId;

    @Column(name = "trigger_time_start")
    private Long triggerTimeStart;

    @Column(name = "trigger_time_end")
    private Long triggerTimeEnd;

    @Column(name = "gmt_modify")
    private Date gmtModify;

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
     * @return gmt_modify
     */
    public Date getGmtModify() {
        return gmtModify;
    }

    /**
     * @param gmtModify
     */
    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
}