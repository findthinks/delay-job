package com.findthinks.delay.job.share.lib.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;

@JsonPropertyOrder({"code", "message", "data"})
public class FoxResult<T> {

    public static final FoxResult SUCCESS = new FoxResult(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getDesc(), null);
    public static final FoxResult UNKNOWN_ERROR = new FoxResult(ExceptionEnum.UNKNOWN_ERROR.getCode(), ExceptionEnum.UNKNOWN_ERROR.getDesc(), null);

    /**
     * 状态码
     */
    private String code;
    /**
     * 状态详细描述
     */
    private String message;
    /**
     * 接口返回的数据
     */
    private T data;

    public FoxResult() {
    }

    public FoxResult(String code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static FoxResult success() {
        return SUCCESS;
    }

    public static FoxResult fail() {
        return UNKNOWN_ERROR;
    }

    public static <T> FoxResult success(T data) {
        return new FoxResult(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getDesc(), data);
    }

    public static FoxResult fail(ExceptionEnum error) {
        return new FoxResult(error.getCode(), error.getDesc(), null);
    }

    public static FoxResult fail(ExceptionEnum error, String message) {
        return new FoxResult(error.getCode(), message, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
