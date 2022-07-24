package com.findthinks.delay.job.console.web.rr;

public class JobShardInfoReq {

    private Integer curServer;

    private Integer reqServer;

    private Integer state;

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
