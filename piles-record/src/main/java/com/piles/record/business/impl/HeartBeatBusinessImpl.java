package com.piles.record.business.impl;


import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.GunStatusMapUtil;
import com.piles.record.entity.HeartBeatRequest;
import com.piles.record.service.IHeartBeatService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 心跳接口实现
 */
@Slf4j
@Service("heartBeatBusiness")
public class HeartBeatBusinessImpl extends BaseBusiness {


    private static SimpleDateFormat sdf = new SimpleDateFormat( "yyMMddHHmmss" );
    private static final TradeType TRADE_TYPE=TradeType.WEI_JING;

    @Resource
    IHeartBeatService heartBeatService;

    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        log.info( "接收到充电桩心跳报文" );
        //依照报文体规则解析报文
        HeartBeatRequest heartBeatRequest = HeartBeatRequest.packEntity( bodyBytes );
        log.info( "接收到充电桩心跳报文:{}", heartBeatRequest.toString() );
        ChannelEntity channelEntity=ChannelMapByEntity.getChannel( incoming );
        if (null==channelEntity){
            return null;
        }
        heartBeatRequest.setPileNo( channelEntity.getPileNo() );
        Channel channelNow=ChannelMapByEntity.getChannel( channelEntity.getTradeType().getCode(),channelEntity.getPileNo() );
        if (null!=channelNow&&channelNow!=incoming){
            log.error( "--------------------充电桩通道变更 原来是" + channelNow.remoteAddress() + "现在的：" + incoming.remoteAddress() );
            ChannelMapByEntity.removeChannel( TRADE_TYPE.getCode(),heartBeatRequest.getPileNo() );
        }
        if (null == ChannelMapByEntity.getChannel( TRADE_TYPE.getCode(),heartBeatRequest.getPileNo() )) {
            ChannelEntity channelEntity1=new ChannelEntity(heartBeatRequest.getPileNo(),TradeType.fromCode(TRADE_TYPE.getCode()));
            ChannelMapByEntity.addChannel( channelEntity1, incoming );
            ChannelMapByEntity.addChannel( incoming, channelEntity1);
        }
        //蔚景的枪号暂时不需要，直接使用0
        GunStatusMapUtil.put( heartBeatRequest.getPileNo(),TradeType.WEI_JING,0,heartBeatRequest.getStatus()[0] );
        // 不需要接调用底层接口
        Date date = heartBeatService.heartBeat( heartBeatRequest );
        byte[] responseBody = BytesUtil.str2Bcd( sdf.format( date ) );
        //组装返回报文体
        return responseBody;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.HEART_BEAT_ANSWER_CODE;
    }

}
