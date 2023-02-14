package com.findthinks.delay.job.console.web.rr;

import javax.validation.constraints.Size;

public class AccountInfo {

    @Size(max = 32, min = 2, message = ":所含字符数在[2,32]的范围内")
    private String account;

    @Size(max = 32, min = 2, message = ":所含字符数在[2,32]的范围内")
    private String pwd;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}