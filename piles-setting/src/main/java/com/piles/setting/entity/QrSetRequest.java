package com.piles.setting.entity;

import com.piles.common.entity.ChannelEntity;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.io.Serializable;
import java.net.SocketAddress;

import org.apache.commons.lang3.StringUtils;

/**
 * 二维码设置
 */
@Data
public class QrSetRequest implements Serializable
{
    /**
     * 抢号 1 位  BIN 1: A枪 2: B枪
     */
    private int gunNo;
    /**
     * 充电桩编号
     */
    private String pileNo;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static QrSetRequest packEntity(byte[] msg, Channel income){
        ChannelEntity channelEntity=ChannelMapByEntity.getChannel(income);
        if (null==channelEntity){
            return null;
        }
        QrSetRequest request=new QrSetRequest();

        request.setGunNo(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,0,1),10)));
        request.setPileNo(channelEntity.getPileNo());
        return request;
    }


    public static void main(String[] args) {

    }



}
