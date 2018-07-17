package com.piles.setting.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.GetPileVersionReqeust;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 请求升级包 充电桩  运营管理系统
 */
@Slf4j
@Service("xunDaoGetPileVersionBusiness")
public class XundaoGetPileVersionBusinessImpl implements IBusiness {


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info( "接收到循道获取升级软件版本号返回报文" );
        String order = String.valueOf(BytesUtil.xundaoControlByte2Int(BytesUtil.copyBytes(msg, 2, 4)));
        //依照报文体规则解析报文
        GetPileVersionReqeust getPileVersionPushReqeust = GetPileVersionReqeust.packEntityXunDao(msg);
        getPileVersionPushReqeust.setSerial(order);
        ChannelResponseCallBackMap.callBack(incoming, order, getPileVersionPushReqeust);
        return null;
    }
}
