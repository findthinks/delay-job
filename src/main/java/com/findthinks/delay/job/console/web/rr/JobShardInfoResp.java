package com.findthinks.delay.job.console.web.rr;

public class JobShardInfoResp {

    public JobShardInfoResp(Integer id, Integer curServer, Integer reqServer, Integer state) {
        this.id = id;
        this.curServer = curServer;
        this.reqServer = reqServer;
        this.state = state;
    }

    private Integer id;

    private Integer curServer;

    private Integer reqServer;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurServer() {
        return curServer;
    }

    public void setCurServer(Integer curServer) {
        this.curServer = curServer;
    }

    public Integer getReqServer() {
        return reqServer;
    }

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
