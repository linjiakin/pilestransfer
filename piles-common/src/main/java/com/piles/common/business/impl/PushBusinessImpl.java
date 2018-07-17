package com.piles.common.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.entity.type.XunDaoTypeCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.ChannelResponseCallBackMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("pushBusinessImpl")
public class PushBusinessImpl implements IPushBusiness {
    @Override
    public boolean push(byte[] msg, int tradeTypeCode,String pileNo, BasePushCallBackResponse basePushRequest, Enum<?> commandCode) {
        //获取连接channel 获取不到无法推送
        Channel channel=ChannelMapByEntity.getChannel(tradeTypeCode,pileNo);

        if (null!=channel){
            ChannelResponseCallBackMap.add(channel,  basePushRequest.getSerial(),basePushRequest);
            //拼接报文
            byte[] start=new byte[]{0x68};
            byte[] command=new byte[]{(byte)((ECommandCode)commandCode).getCode()};
            byte[] serial= BytesUtil.intToBytes( Integer.parseInt(basePushRequest.getSerial()));
            byte[] length=BytesUtil.intToBytes( msg.length );
            byte[] temp= Bytes.concat( command,serial,length,msg );
            byte[] crc= BytesUtil.intToBytes( CRC16Util.getCRC( temp ));

            byte[] writeMsg=Bytes.concat( start,temp,crc );

            String pushMsg="";
            for (byte b:writeMsg){
                pushMsg+= " "+ Integer.toHexString(Byte.toUnsignedInt(b));
            }
            log.info("向蔚景[" + channel.remoteAddress() + "]主动发送push请求信息:" + pushMsg);
            ChannelFuture channelFuture=channel.writeAndFlush(writeMsg);
            channelFuture.addListener( new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    basePushRequest.setCode( EPushResponseCode.WRITE_OK );
                    basePushRequest.getCountDownLatch().countDown();
                }
            } );
            return true;
        }else {
            log.error("厂商类型:{},桩号:{} 无法获取到长连接,请检查充电桩连接状态",tradeTypeCode,pileNo);
            return false;
        }
    }
}
