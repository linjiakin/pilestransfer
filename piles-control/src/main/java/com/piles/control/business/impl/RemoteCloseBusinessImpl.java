package com.piles.control.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteCloseRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 远程结束充电
 */
@Slf4j
@Service("remoteCloseBusiness")
public class RemoteCloseBusinessImpl extends BaseBusiness {


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        log.info("接收到蔚景停止充电返回报文");
        //依照报文体规则解析报文
        RemoteCloseRequest remoteCloseRequest = RemoteCloseRequest.packEntity( bodyBytes );
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), remoteCloseRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return null;
    }
}
