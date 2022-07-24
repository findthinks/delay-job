package com.findthinks.delay.job.share.lib.enums;

/**
 * @author YuBo
 */
public enum ExceptionEnum {

    SUCCESS("ok", "success"),
    INVALID_PARAMS("invalid_params", "param error"),
    AUTHENTICATION_NOT_EXIST("authentication_not_exist", "user not logged in"),
    UNKNOWN_ERROR( "unknown_error", "unknown exception");

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