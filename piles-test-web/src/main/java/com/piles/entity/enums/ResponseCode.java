package com.piles.entity.enums;

import lombok.Data;

/**
 * @author lgc48027
 * @version Id: ResponseCode, v 0.1 2018/1/17 14:05 lgc48027 Exp $
 */
public enum ResponseCode {
    OK( 200, "成功" ),
    CONNECNTION_ERROR( 10001, "无链接" ),
    NO_STATUS(10002,"没有获取到状态"),
    ;

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
