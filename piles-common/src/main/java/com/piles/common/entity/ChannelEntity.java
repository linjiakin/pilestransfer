package com.piles.common.entity;

import com.piles.common.entity.type.TradeType;
import lombok.Data;

/**
 * Created by lgc on 18/1/7.
 */
@Data
public class ChannelEntity {
    private String pileNo;
    private TradeType tradeType;

    public ChannelEntity(String pileNo,TradeType tradeType){
        this.pileNo=pileNo;
        this.tradeType=tradeType;
    }
}
