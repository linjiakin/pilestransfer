package com.piles.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by lgc on 17/12/9.
 */
@Data
public class ChargeRemoteStartRequest {
    /**
     * 默认必填充电桩编号
     */
    private String pileNo;


    /**
     * 抢号 1 位  BIN 1: A枪 2: B枪
     */
    private Integer gunNo;
    /**
     * 充电模式 1位 BIN
     * 1: 自动充满 2: 按金额充  3: 按时间充 4: 按电量充
     */
    private Integer chargeModel;
    /**
     * 充电数据 4位 BIN
     * 对应每种充电模式的数据。
     * 1：直到充满，填0
     * 2：按金额充，填金额大小，单位：元，精确到0.001
     * 3：按时间充，填时间长度，单位：秒
     * 4：按电量充，填电量大小，单位：度, 精确到0.001
     */
    private BigDecimal chargeData;
    /**
     * 充电停止码 2位  BCD
     * 用户在屏幕输入充电停止码，可结束充电
     */
    private String chargeStopCode;
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
