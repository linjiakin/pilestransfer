package com.piles.setting.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.RebootRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 重启
 */
@Slf4j
@Service("rebootBusiness")
public class RebootBusinessImpl extends BaseBusiness {


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        //依照报文体规则解析报文
        RebootRequest rebootRequest = RebootRequest.packEntity( bodyBytes );
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), rebootRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.REBOOT_ANSWER_CODE;
    }
}
