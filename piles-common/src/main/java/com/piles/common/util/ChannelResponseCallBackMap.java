package com.piles.common.util;


import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.entity.type.EPushResponseCode;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChannelResponseCallBackMap  {
    /**
     * channel:流水号:BasePushCallBackResponse
     */
    private static ConcurrentHashMap<Channel,ConcurrentHashMap<String,BasePushCallBackResponse>> map=new ConcurrentHashMap<>(  );

    /**
     *
     * @param channel
     * @param serial
     * @return
     */
    private static BasePushCallBackResponse getObject(Channel channel, String serial){
        ConcurrentHashMap<String,BasePushCallBackResponse> temp=map.get( channel );
        if (null==temp){
            return null;
        }
        return temp.get( serial );
    }

    /**
     * 调用callback
     * @param channel
     * @param serial
     * @param obj
     */
    public static   void callBack(Channel channel, String serial, BasePushResponse obj){
        BasePushCallBackResponse basePushCallBackResponse =getObject(channel,  serial);
        if (null== basePushCallBackResponse) {
            log.warn( channel.toString() + "流水号" + serial + "response为空,可能server端方法超时返回了" );
            return;
        }
        basePushCallBackResponse.setCode( EPushResponseCode.READ_OK );
        basePushCallBackResponse.setObj( obj );
        basePushCallBackResponse.getCountDownLatch().countDown();

    }

    public  static void add(Channel channel, String serial, BasePushCallBackResponse obj){
        ConcurrentHashMap<String,BasePushCallBackResponse> temp=map.get( channel );
        if (null==temp){
            temp=new ConcurrentHashMap<>(  );
            map.put( channel,temp );
        }
        temp.put( serial,obj );
    }

    /**
     *
     * @param pileNo
     * @param serial
     * @return
     */
    public static void remove(int tradeTypeCode,String pileNo, String serial){
        Channel channel=ChannelMapByEntity.getChannel( tradeTypeCode,pileNo );
        if (null!=channel) {
            ConcurrentHashMap<String, BasePushCallBackResponse> temp = map.get( channel );
            if (null != temp&& temp.contains( serial )) {
                map.remove( serial );
            }
        }
    }
}
