package com.piles.entity.vo;

import lombok.Data;

/**
 * 修改ip后台请求参数
 *
 * @author lizhi.zhang
 * @create 2018-04-11 上午07:32
 **/
@Data
public class ModifyIPRequest {

    /**
     * 对应厂商类型  1:蔚景 2: 循道
     */
    private int tradeTypeCode;
    /**
     * 桩号 复数用 , 分割
     */
    private String pileNos;

    /**
     * 默认必填流水号
     */
    private String serial;
    /**
     * 服务器地址
     */
    private String addr;
    /**
     * 端口
     */
    private int port;
}
