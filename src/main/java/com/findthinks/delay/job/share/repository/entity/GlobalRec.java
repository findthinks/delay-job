package com.findthinks.delay.job.share.repository.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "global_rec")
public class GlobalRec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "out_job_no")
    private String outJobNo;

    @Column(name = "job_shard_id")
    private Integer jobShardId;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "trigger_time")
    private Long triggerTime;

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
     * @return job_id
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}