package com.piles.control.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteAppendRequest;
import com.piles.control.entity.RemoteCloseRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 续充
 */
@Slf4j
@Service("xunDaoAppendChargeBusiness")
public class XunDaoAppendBusinessImpl implements IBusiness {


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));
        log.info("接收到循道续充充电返回报文");
        String order = String.valueOf(BytesUtil.xundaoControlByte2Int(BytesUtil.copyBytes(msg, 2, 4)));
        //依照报文体规则解析报文
        RemoteAppendRequest remoteCloseRequest = RemoteAppendRequest.packEntity(dataBytes);
        log.info("接收到循道续充命令" + remoteCloseRequest.toString());
        ChannelResponseCallBackMap.callBack(incoming, order, remoteCloseRequest);
        return null;
    }
}
