package com.findthinks.delay.job.console.web.rr;

import java.util.List;

public class SchedulerInfoResp {

    public SchedulerInfoResp(Integer id, String uuid, Long lastHeartbeatTime, Integer register, String ip, List<Integer> jobShardIds) {
        this.id = id;
        this.uuid = uuid;
        this.lastHeartbeatTime = lastHeartbeatTime;
        this.register = register;
        this.ip = ip;
        this.jobShardIds = jobShardIds;
    }

    private Integer id;

    private String uuid;

    private Long lastHeartbeatTime;

    private Integer register;

    private String ip;

    private List<Integer> jobShardIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(Long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    public Integer getRegister() {
        return register;
    }

    public void setRegister(Integer register) {
        this.register = register;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Integer> getJobShardIds() {
        return jobShardIds;
    }

    public void setJobShardIds(List<Integer> jobShardIds) {
        this.jobShardIds = jobShardIds;
    }
}
