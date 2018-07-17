package com.piles.common.entity;

import lombok.Data;

/**
 * 客户端调用类
 */
@Data
public class ClientRequest {

    /**
     * 请求报文
     */
    private byte[] msg;
    /**
     * 请求主机
     */
    private String host;
    /**
     * 请求端口
     */
    private int port;
}
