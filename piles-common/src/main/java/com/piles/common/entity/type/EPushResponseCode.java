package com.piles.common.entity.type;


public enum EPushResponseCode {

    WRITE_OK(100,"写ok"),
    READ_OK(200,"读ok"),
    CONNECT_ERROR(400,"链接出错"),
    TIME_OUT(300,"操作超时");

    private int code;
    private String msg;

    EPushResponseCode(int code, String msg) {
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
