package com.hometask.dkp.hsbctransactionmanagement.controller;

import lombok.Data;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Data
public class ResponseResult<T> {
    private int code;

    private String msg;

    private T data;

    public ResponseResult() {
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(StatusEnums statusEnums) {
        this.code = statusEnums.getCode();
        this.msg = statusEnums.getMsg();
    }

    public static <T> ResponseResult<T> succeed(T data) {
        ResponseResult responseResult = new ResponseResult(StatusEnums.SUCCESS);
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult succeed() {
        return new ResponseResult(StatusEnums.SUCCESS);
    }

    public static <T> ResponseResult<T> fail(int code, String msg) {
        return new ResponseResult(code, msg);
    }

    public static <T> ResponseResult<T> fail(StatusEnums statusEnums) {
        return new ResponseResult(statusEnums);
    }

    public static <T> ResponseResult<T> fail(String msg) {
        return new ResponseResult(StatusEnums.FAIL.getCode(), msg);
    }
}
