package com.piles.setting.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.setting.domain.UpdateStatusReport;
import com.piles.setting.entity.XunDaoUpdateStatusRequest;
import com.piles.setting.service.IUpdateStatusService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 循道升级状态汇报 充电桩  运营管理系统
 */
@Slf4j
@Service("xunDaoUpdateStatusBusiness")
public class XunDaoUpdateStatusBusinessImpl implements IBusiness {


    @Resource
    private IUpdateStatusService updateStatusService;

    //返回报文记录类型
    private int recordType = 53;
    @Override
    public byte[] process(byte[] msg, Channel incoming) {

        log.info( "接收到循道充电桩升级结果汇报报文" );
        byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));

        //依照报文体规则解析报文
        XunDaoUpdateStatusRequest updatePackageRequest = XunDaoUpdateStatusRequest.packEntity(dataBytes);
        log.info( "接收到循道充电桩升级结果汇报报文:{}", updatePackageRequest.toString() );
        UpdateStatusReport uploadRecord = buildServiceEntity(updatePackageRequest);

        updateStatusService.updateStatus(uploadRecord);
        //组装返回报文体
        byte[] response = buildReponse(uploadRecord.getPileNo(),msg);
        return response;
    }

    //封装返回报文
    private byte[] buildReponse(String pileNo, byte[] msg) {
        byte[] data= BytesUtil.copyBytes( msg,13,8 );
        byte[] head = new byte[]{0x68};
        byte[] length = BytesUtil.intToBytesLittle(19,1);
        byte[] contrl = BytesUtil.copyBytes( msg,2,4 );
        byte[] type = BytesUtil.intToBytesLittle(133,1);
        byte[] beiyong = new byte[]{0x00};
        byte[] reason = ChannelMapByEntity.getPileTypeArr(pileNo);
//        byte[] reason = new byte[]{0x03, 0x00};
        byte[] crc = CRC16Util.getXunDaoCRC(data);
        byte[] addr = BytesUtil.intToBytesLittle(53,1);;


        byte[] temp = Bytes.concat(head, length, contrl, type, beiyong, reason, crc, addr, data);
        return temp;
    }

    private UpdateStatusReport buildServiceEntity(XunDaoUpdateStatusRequest updatePackageRequest) {
        UpdateStatusReport updateStatusReport = new UpdateStatusReport();
        updateStatusReport.setTradeTypeCode(TradeType.XUN_DAO.getCode());
        updateStatusReport.setPileNo(updatePackageRequest.getPileNo());
        updateStatusReport.setSoftVersion(updatePackageRequest.getSoftVersion());
        updateStatusReport.setStatus(updatePackageRequest.getStatus());
        //添加桩类型
        updateStatusReport.setPileType(ChannelMapByEntity.getPileType(updatePackageRequest.getPileNo()));

//        updateStatusReport.setProtocolVersion();
        return updateStatusReport;
    }
}
