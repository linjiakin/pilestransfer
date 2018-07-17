package com.piles.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeRemoteStopRequest {
    /**
     * 默认必填充电桩编号
     */
    private String pileNo;
    /**
     * 抢号 1 位  BIN 1: A枪 2: B枪
     */
    private Integer gunNo;
    /**
     * 订单号 8位 BIN
     */
    private Long orderNo;
    /**
     * 流水号
     */
    private String serial;
    /**
     * 对应厂商类型  1:蔚景 2: 循道
     */
    private int tradeTypeCode;
    /**
     * 直流桩单枪:1 直流桩双枪:2 交流桩单枪:3 交流桩双枪:4
     */
    private int pileType;
}
