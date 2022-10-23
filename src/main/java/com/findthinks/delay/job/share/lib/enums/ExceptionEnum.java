package com.findthinks.delay.job.share.lib.enums;

public enum ExceptionEnum {

    SUCCESS("ok", "success"),
    INVALID_PARAMS("invalid_params", "parameter error"),
    USERNAME_PWD_ERROR("username_pwd_error","username or password error"),
    AUTHENTICATION_NOT_EXIST("authentication_not_exist", "user not login"),
    CANNOT_CANCEL_JOB("cannot_cancel_job", "cannot cancel job"),
    JOB_IS_CANCEL("job_is_cancel", "job is cancel"),
    JOB_NOT_EXIST("job_not_exist", "job is not exist"),
    JOB_IS_TRIGGERED("job_is_triggered", "job is triggered"),
    NO_AVAILABLE_JOB_SHARD("no_available_job_shard","no available job shard"),
    OUT_JOB_NO_IS_EXIST("out_job_no_is_exist", "out job number is exist"),
    HAS_TRANSLATING_SHARD("has_translating_job_shard", "has translating job shard"),
    DUPLICATE_KEY_EXCEPTION("data_duplicate","data duplicate"),
    UNKNOWN_ERROR( "unknown_error", "unknown error");

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