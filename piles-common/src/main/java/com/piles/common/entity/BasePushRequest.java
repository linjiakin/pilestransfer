package com.piles.common.entity;

import lombok.Data;

/**
 * push 信息基础request
 */
@Data
public class BasePushRequest {
    /**
     * 默认必填充电桩编号
     */
    private String pileNo;

    /**
     * 默认必填流水号
     */
    private String serial;
    /**
     * 对应厂商类型  1:蔚景 2: 循道
     */
    private int tradeTypeCode;
    /**
     * 充电桩类型 3:交流桩单枪;4:交流桩双枪;5:直流桩单枪;6:直流桩双枪
     */
    private int pileType;
    /**
     * 充电枪枪号,单枪为 0，如果是双枪: A枪为0, B枪为1
     */
    private int gunNo;
}
