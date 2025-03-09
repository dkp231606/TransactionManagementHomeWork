package com.hometask.dkp.hsbctransactionmanagement.controller;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

public enum StatusEnums {
    SUCCESS(200, "success"),
    FAIL(500, "error"),
    PARAMS_INVALID_ERROR(300, "params is not valid");



    private final int code;
    public final String msg;

    StatusEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
