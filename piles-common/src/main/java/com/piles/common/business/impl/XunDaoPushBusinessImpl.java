package com.piles.common.business.impl;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.ChannelResponseCallBackMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("xunDaoPushBusinessImpl")
public class XunDaoPushBusinessImpl implements IPushBusiness {
    @Override
    public boolean push(byte[] msg, int tradeTypeCode, String pileNo, BasePushCallBackResponse basePushRequest, Enum<?> commandCode) {
        //获取连接channel 获取不到无法推送
        Channel channel = ChannelMapByEntity.getChannel( tradeTypeCode, pileNo );

        if (null != channel) {
            ChannelResponseCallBackMap.add( channel, basePushRequest.getSerial(), basePushRequest );
            String pushMsg = "";
            for (byte b : msg) {
                pushMsg += " " + Integer.toHexString( Byte.toUnsignedInt( b ) );
            }
            log.info( "向循道[" + channel.remoteAddress() + "]主动发送push请求信息:" + pushMsg );
            ChannelFuture channelFuture = channel.writeAndFlush( msg );
            channelFuture.addListener( new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    basePushRequest.setCode( EPushResponseCode.WRITE_OK );
                    basePushRequest.getCountDownLatch().countDown();
                }
            } );
            return true;
        } else {
            log.error( "厂商类型:{},桩号:{} 无法获取到长连接,请检查充电桩连接状态", tradeTypeCode, pileNo );
            return false;
        }
    }
}
