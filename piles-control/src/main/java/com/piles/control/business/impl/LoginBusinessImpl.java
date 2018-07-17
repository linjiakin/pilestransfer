package com.piles.control.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.control.entity.LoginRequest;
import com.piles.control.service.ILoginService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录接口逻辑
 */
@Slf4j
@Component("loginBusiness")
public class LoginBusinessImpl extends BaseBusiness{

    @Resource
    private ILoginService loginService;
    private static final TradeType TRADE_TYPE=TradeType.WEI_JING;

//    @Override
//    public SocketBaseDTO process(byte[] msg) {
//        System.out.println("接收到的原始请求报文：" + new String(msg));
//        return null;
//    }

    @Override
    protected byte[] processBody(byte[] bodyBytes,Channel incoming,int order) {

        log.info( "接收到登录请求报文" );
        //依照报文体规则解析报文
        LoginRequest loginRequest = LoginRequest.packEntity(bodyBytes);
        loginRequest.setTradeType(TradeType.WEI_JING);
        log.info( "接收到登录请求报文:{}", loginRequest.toString() );
        //调用底层接口
        boolean flag = loginService.login(loginRequest);
        if (flag){
            ChannelEntity channelEntity=new ChannelEntity(loginRequest.getPileNo(),TradeType.fromCode(TradeType.WEI_JING.getCode()));
            ChannelMapByEntity.addChannel(channelEntity,incoming);
            ChannelMapByEntity.addChannel(incoming,channelEntity);
        }
        byte[] pileNo = BytesUtil.copyBytes(bodyBytes, 0, 8);
        byte[] result = flag==true?new byte[]{0x00}:new byte[]{0x01};
        byte[] responseBody = Bytes.concat(pileNo,result);
        //组装返回报文体

        return responseBody;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.LOGIN_ANSWER_CODE;
    }
}
