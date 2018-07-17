package com.piles.setting.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.VerifyTimeRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 校时
 */
@Slf4j
@Service("verifyTimeBusiness")
public class VerifyTimeBusinessImpl extends BaseBusiness {


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        //依照报文体规则解析报文
        VerifyTimeRequest verifyTimeRequest = VerifyTimeRequest.packEntity( bodyBytes );
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), verifyTimeRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.VERIFY_TIME_ANSWER_CODE;
    }
}
