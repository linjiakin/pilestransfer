package com.piles.record.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 上传充电记录接口请求实体
 * Created by zhanglizhi on 2018/1/7.
 */
@Data
public class UploadRecord {

    /**
     * 厂商编号
     */
    private int tradeTypeCode;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 桩编号
     */
    private String pileNo;
    /**
     * 序号
     */
    private Integer serial;
    /**
     * 结束原因
     */
    private int endReason;
    /**
     * 充电总电量	BIN	4	单位: 度，精确度为0.001
     */
    private BigDecimal totalAmmeterDegree;
    /**
     * 桩类型
     */
    private int pileType;
    /**
     * 枪号
     */
    private int gunNo;
}
