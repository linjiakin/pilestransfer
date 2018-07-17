package com.piles.entity.vo;

import lombok.Data;

/**
 * @author lgc48027
 * @version Id: PileStatusRequest, v 0.1 2018/1/17 13:53 lgc48027 Exp $
 */
@Data
public class PileStatusRequest {
    /**
     * 默认必填充电桩编号
     */
    private String pileNo;


    /**
     * 对应厂商类型  1:蔚景 2: 循道
     */
    private Integer tradeTypeCode;

    /**
     * 桩类型
     */
    private Integer pileType;

    /**
     * 枪号
     */
    private Integer gunNo;
}
