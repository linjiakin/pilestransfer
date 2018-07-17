package com.piles.common.business;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;

/**
 * 推送消息业务
 */
public interface IPushBusiness {
    /**
     * 推送消息
     * @param msg 消息体
     * @param pileNo 桩编号
     * @return
     */
    boolean push(byte[] msg, int tradeTypeCode,String pileNo, BasePushCallBackResponse basePushCallBackResponse, Enum<?> eCommandCode);
}
