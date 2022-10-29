package com.findthinks.delay.job.share.repository.entity;

import javax.persistence.*;

@Table(name = "scheduler_info")
public class SchedulerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uuid;

    @Column(name = "last_heartbeat_time")
    private Long lastHeartbeatTime;

    private Integer register;

    private String ip;

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
     * @return uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * @return last_heartbeat_time
     */
    public Long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    /**
     * @param lastHeartbeatTime
     */
    public void setLastHeartbeatTime(Long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    /**
     * @return register
     */
    public Integer getRegister() {
        return register;
    }

    /**
     * @param register
     */
    public void setRegister(Integer register) {
        this.register = register;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }
}