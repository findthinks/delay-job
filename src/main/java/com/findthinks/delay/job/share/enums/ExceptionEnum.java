package com.findthinks.delay.job.share.enums;

/**
 * @author YuBo
 */
public enum ExceptionEnum {

    SUCCESS("ok", "成功"),
    INVALID_PARAMS("invalid_params", "参数错误"),
    AUTHENTICATION_NOT_EXIST("authentication_not_exist", "请先登录"),
    UNKNOWN_ERROR( "unknown_error", "未知异常");

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