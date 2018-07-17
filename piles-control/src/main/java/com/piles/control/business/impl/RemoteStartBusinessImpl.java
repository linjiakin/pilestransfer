package com.piles.control.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteStartRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 远程启动充电  运营管理系统 -> 充电桩
 */
@Slf4j
@Service("remoteStartBusiness")
public class RemoteStartBusinessImpl extends BaseBusiness {


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        log.info("接收到蔚景启动充电返回报文");
        //依照报文体规则解析报文
        RemoteStartRequest remoteStartRequest = RemoteStartRequest.packEntity( bodyBytes );
        //调用底层接口
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), remoteStartRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return null;
    }
}
