package com.piles.setting.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.RemoteUpdateRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 远程升级
 */
@Slf4j
@Service("remoteUpdateBusiness")
public class RemoteUpdateBusinessImpl extends BaseBusiness {


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        //依照报文体规则解析报文
        RemoteUpdateRequest remoteUpdateRequest = RemoteUpdateRequest.packEntity( bodyBytes );
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), remoteUpdateRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.REMOTE_UPDATE_ANSWER_CODE;
    }
}
