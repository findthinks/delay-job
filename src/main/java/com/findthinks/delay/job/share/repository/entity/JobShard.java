package com.findthinks.delay.job.share.repository.entity;

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

    @Column(name = "state")
    private Integer state;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}