package com.findthinks.delay.job.core.repository.entity;

import javax.persistence.*;

@Table(name = "job_shard")
public class JobShard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cur_server")
    private Integer curServer;

    @Column(name = "req_server")
    private Integer reqServer;

    @Column(name = "is_receiving")
    private Boolean isReceiving;

    @Column(name = "is_consuming")
    private Boolean isConsuming;

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
     * @return cur_server
     */
    public Integer getCurServer() {
        return curServer;
    }

    /**
     * @param curServer
     */
    public void setCurServer(Integer curServer) {
        this.curServer = curServer;
    }

    /**
     * @return req_server
     */
    public Integer getReqServer() {
        return reqServer;
    }

    /**
     * @param reqServer
     */
    public void setReqServer(Integer reqServer) {
        this.reqServer = reqServer;
    }

    /**
     * @return is_receiving
     */
    public Boolean getIsReceiving() {
        return isReceiving;
    }

    /**
     * @param isReceiving
     */
    public void setIsReceiving(Boolean isReceiving) {
        this.isReceiving = isReceiving;
    }

    /**
     * @return is_consuming
     */
    public Boolean getIsConsuming() {
        return isConsuming;
    }

    /**
     * @param isConsuming
     */
    public void setIsConsuming(Boolean isConsuming) {
        this.isConsuming = isConsuming;
    }
}