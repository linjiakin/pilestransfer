package com.piles.control.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteCloseRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 远程结束充电
 */
@Slf4j
@Service("xunDaoStartOrStopBusiness")
public class XunDaoStartOrStopBusinessImpl implements IBusiness {


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到循道启动或者停止充电返回报文");
        String order = String.valueOf(BytesUtil.xundaoControlByte2Int(BytesUtil.copyBytes(msg, 2, 4)));
        //依照报文体规则解析报文
        RemoteCloseRequest remoteCloseRequest = RemoteCloseRequest.packEntityXunDao( msg );
        log.info("接收到循道充电或停止命令" + remoteCloseRequest.toString());
        ChannelResponseCallBackMap.callBack(incoming, order, remoteCloseRequest);
        return null;
    }
}
