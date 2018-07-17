package com.piles.record.business.impl;


import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.TradeType;
import com.piles.common.entity.type.XunDaoTypeCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.MsgHelper;
import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.XunDaoUploadRecordRequest;
import com.piles.record.service.IUploadRecordService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 上传充电记录接口实现
 */
@Slf4j
@Service("xunDaoUnploadTradeDataBusiness")
public class XunDaoUploadRecordBusinessImpl implements IBusiness {

    @Resource
    private IUploadRecordService uploadRecordService;


    //返回报文记录类型
    private int recordType = 15;

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info( "接收到循道充电桩上传充电记录报文" );
        int gunNo = MsgHelper.getGunNo(msg);
        //依照报文体规则解析报文
        byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));
        XunDaoUploadRecordRequest uploadRecordRequest = XunDaoUploadRecordRequest.packEntity( dataBytes );
        log.info( "接收到循道充电桩上传充电记录报文:{}", uploadRecordRequest.toString() );
        UploadRecord uploadRecord = buildServiceEntity(uploadRecordRequest,gunNo);
        //添加serial
        //调用底层接口
        boolean flag = uploadRecordService.uploadRecord( uploadRecord );
        return buildReponse(msg,flag);
    }

    //封装返回报文
    private byte[] buildReponse(byte[] msg, boolean result) {
        //消息头
//        byte[] bytes = BytesUtil.copyBytes(msg, 0, 13);
//        //设置类型标识
//        BytesUtil.replaceBytes(bytes,6,BytesUtil.intToBytesLittle(XunDaoTypeCode.SEND_DATA_CODE.getCode(),1));
//        //替换记录类型
//        BytesUtil.replaceBytes(bytes,12,BytesUtil.intToBytesLittle(recordType,1));
//
//        //失败直接返回的3 0:成功，1:记录格式有误失 败，2:数据重复;3:交易记 录插入数据库失败
//        byte[] resultByte = result == true ? new byte[]{0x00} : new byte[]{0x03};
//        //加上终端机器编码
//        Bytes.concat(bytes,dataBytes);
//        byte[] xunDaoCRC = CRC16Util.getXunDaoCRC(dataBytes);
//        //替换crc
//        BytesUtil.replaceBytes(bytes,10,xunDaoCRC);
//        //替换长度
//        BytesUtil.replaceBytes(bytes,1,BytesUtil.intToBytesLittle(bytes.length-2));
//
//        return bytes;


        byte[] head = new byte[]{0x68};
        byte[] length = BytesUtil.intToBytesLittle(20,1);
        byte[] contrl = BytesUtil.copyBytes(msg, 2, 4);

        byte[] type = BytesUtil.intToBytesLittle(XunDaoTypeCode.SEND_DATA_CODE.getCode(),1);
        byte[] beiyong = new byte[]{0x00};
        byte[] reason = new byte[]{0x03, 0x00};
        byte[] recordType = BytesUtil.intToBytesLittle(this.recordType,1);
        byte[] data = BytesUtil.copyBytes(msg, 13, 8);
        byte[] resultByte = result == true ? new byte[]{0x00} : BytesUtil.intToBytesLittle(3,1);
        data = Bytes.concat(data,resultByte);
        byte[] crc = CRC16Util.getXunDaoCRC(data);

        return Bytes.concat(head, length, contrl, type, beiyong, reason, crc, recordType, data);


    }

    private UploadRecord buildServiceEntity(XunDaoUploadRecordRequest uploadRecordRequest, int gunNo){
        UploadRecord uploadRecord = new UploadRecord();
        uploadRecord.setTradeTypeCode(TradeType.XUN_DAO.getCode());
        uploadRecord.setOrderNo(uploadRecordRequest.getOrderNo());
        uploadRecord.setPileNo(uploadRecordRequest.getPileNo());
        uploadRecord.setEndReason(uploadRecordRequest.getStopChargeReason());
        uploadRecord.setTotalAmmeterDegree(uploadRecordRequest.getTotalAmmeterDegree());
        uploadRecord.setSerial( Integer.parseInt(  uploadRecordRequest.getSerial()));
        uploadRecord.setPileType(ChannelMapByEntity.getPileType(uploadRecordRequest.getPileNo()));
        uploadRecord.setGunNo(gunNo);
        return uploadRecord;
    }
}
