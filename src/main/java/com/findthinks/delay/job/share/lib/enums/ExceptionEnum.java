package com.findthinks.delay.job.share.lib.enums;

/**
 * @author YuBo
 */
public enum ExceptionEnum {

    SUCCESS("ok", "Success"),
    INVALID_PARAMS("invalid_params", "Param error"),
    AUTHENTICATION_NOT_EXIST("authentication_not_exist", "User not logged in"),
    CANNOT_CANCEL_JOB("cannot_cancel_job", "Can not cancel job"),
    JOB_IS_CANCEL("job_is_cancel", "Job is cancel"),
    UNKNOWN_ERROR( "unknown_error", "Unknown exception");

    /** 枚举编码 */
    private String code;
    /** 枚举描述 */
    private String desc;

    private ExceptionEnum(String code, String desc) {
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