package com.piles.setting.business.impl;

import com.alibaba.fastjson.JSON;
import com.piles.common.business.IBusiness;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.XunDaoModifyIPRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 循道 修改ip程序下行数据(充电桩-->后台)
 */
@Slf4j
@Service("xunDaoModifyIPBusiness")
public class XunDaoModifyIPBusinessImpl implements IBusiness {


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info( "接收到循道充电桩响应修改ip返回报文" );
        String order = String.valueOf(BytesUtil.xundaoControlByte2Int(BytesUtil.copyBytes(msg, 2, 4)));
        byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));
        //依照报文体规则解析报文
        XunDaoModifyIPRequest xunDaoModifyIPRequest = XunDaoModifyIPRequest.packEntity( dataBytes );
        log.info( "接收到循道充电桩响应修改ip返回结果:{}", JSON.toJSONString(xunDaoModifyIPRequest) );
        //调用底层接口
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), xunDaoModifyIPRequest );
        return null;
    }
}
