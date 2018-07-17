package com.piles.setting.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.MsgHelper;
import com.piles.setting.domain.UploadChageIpRecord;
import com.piles.setting.entity.XunDaoModifyIPReportRequest;
import com.piles.setting.service.IUploadChangeIpService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 循道 修改ip程序下行数据(充电桩-->后台)
 */
@Slf4j
@Service("xunDaoModifyIPReportBusiness")
public class XunDaoModifyIPReportBusinessImpl implements IBusiness {
    @Resource
    IUploadChangeIpService ipService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info( "接收到循道充电桩修改ip上报返回报文" );
        byte[] dataBytes = BytesUtil.copyBytes( msg, 13, 40 );
        //依照报文体规则解析报文
        XunDaoModifyIPReportRequest xunDaoModifyIPRequest = XunDaoModifyIPReportRequest.packEntity( dataBytes );
        UploadChageIpRecord uploadChageIpRecord = JSONObject.parseObject( JSON.toJSONString( xunDaoModifyIPRequest ), UploadChageIpRecord.class );
        uploadChageIpRecord.setTradeTypeCode( TradeType.XUN_DAO.getCode() );
        uploadChageIpRecord.setPileType(ChannelMapByEntity.getPileType(uploadChageIpRecord.getPileNo()));
        boolean flag = ipService.uploadRecord( uploadChageIpRecord );

        if (flag) {
            dataBytes = Bytes.concat( BytesUtil.copyBytes( msg, 13, 8 ), new byte[]{0x00} );
        } else {
            dataBytes = Bytes.concat( BytesUtil.copyBytes( msg, 13, 8 ), new byte[]{0x01} );
        }
        byte[] head = buildHead( uploadChageIpRecord.getPileNo(),dataBytes, BytesUtil.copyBytes( msg, 2, 4 ) );
        return head;
    }

    //添加报文头
    private byte[] buildHead(String pileNo, byte[] dataMsg, byte[] serial) {
        byte[] result = Bytes.concat( new byte[]{0x68, 0x14} );
        result = Bytes.concat( result, serial );
        //添加类型标识
        result = Bytes.concat( result, new byte[]{(byte) 0x85, 0x00} );
        //添加桩类型
        result = Bytes.concat( result, ChannelMapByEntity.getPileTypeArr(pileNo) );
        //添加crc
        result = Bytes.concat( result, CRC16Util.getXunDaoCRC( dataMsg ) );
        //添加记录类型
        result = Bytes.concat( result, new byte[]{0x48} );

        result = Bytes.concat( result, dataMsg );

        return result;
    }


}
