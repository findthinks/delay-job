package com.findthinks.delay.job.share.lib.enums;

/**
 * @author YuBo
 */
public enum ExceptionEnum {

    SUCCESS("ok", "Success."),
    INVALID_PARAMS("invalid_params", "Parameter error"),
    USERNAME_PWD_ERROR("username_pwd_error","username or password error."),
    AUTHENTICATION_NOT_EXIST("authentication_not_exist", "User not logged in."),
    CANNOT_CANCEL_JOB("cannot_cancel_job", "Cannot cancel job."),
    JOB_IS_CANCEL("job_is_cancel", "Job is cancel."),
    UNKNOWN_ERROR( "unknown_error", "Unknown error.");

    /** 枚举编码 */
    private String code;
    /** 枚举描述 */
    private String desc;

    ExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}