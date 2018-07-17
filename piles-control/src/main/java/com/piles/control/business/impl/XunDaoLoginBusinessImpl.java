package com.piles.control.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.entity.ChannelEntity;
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
@Component("xunDaologinBusiness")
public class XunDaoLoginBusinessImpl implements IBusiness {

    @Resource
    private ILoginService loginService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到登录请求报文");
        //依照报文体规则解析报文
        LoginRequest loginRequest = LoginRequest.packEntityXundao(msg);
        loginRequest.setTradeType(TradeType.XUN_DAO);
        log.info("接收到登录请求报文:{}", loginRequest.toString());
        //调用底层接口
        boolean flag = loginService.login(loginRequest);
        if (flag) {
            ChannelEntity channelEntity = new ChannelEntity(loginRequest.getPileNo(), TradeType.fromCode(TradeType.XUN_DAO.getCode()));
            ChannelMapByEntity.removeChannel(channelEntity);
            ChannelMapByEntity.addChannel(channelEntity, incoming);
            ChannelMapByEntity.addChannel(incoming, channelEntity);
            ChannelMapByEntity.addPileType(loginRequest.getPileNo(), loginRequest.getPileType());
            ChannelMapByEntity.addPileType(loginRequest.getPileNo(), new byte[]{BytesUtil.copyBytes(msg, 3, 1)[0], 0x00});
        }
        //组装返回报文体

        return msg;
    }
}
