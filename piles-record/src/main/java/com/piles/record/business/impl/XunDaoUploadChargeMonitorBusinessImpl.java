package com.piles.record.business.impl;


import com.alibaba.fastjson.JSON;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.*;
import com.piles.record.domain.UploadChargeMonitor;
import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.XunDaoUploadChargeMonitorRequest;
import com.piles.record.service.IUploadChargeMonitorService;
import com.piles.record.service.IUploadRecordService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 上传充电过程监测数据 接口实现
 */
@Slf4j
@Service("xunDaoUploadChargeMonitorBusiness")
public class XunDaoUploadChargeMonitorBusinessImpl implements IBusiness {

    @Resource
    private IUploadChargeMonitorService uploadChargeMonitorService;
    @Resource
    private IUploadRecordService uploadRecordService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到循道充电桩上传充电过程监测数据报文");
        byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));

        //依照报文体规则解析报文
        XunDaoUploadChargeMonitorRequest uploadChargeMonitorRequest = XunDaoUploadChargeMonitorRequest.packEntity(dataBytes);
        uploadChargeMonitorRequest.setGunNo(MsgHelper.getGunNo(msg));
        Integer pileType = Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 8, 1), 10));
        pileType = pileType == 2 ? 3 : pileType;
        uploadChargeMonitorRequest.setPileType(pileType);
        log.info("接收到循道充电桩上传充电过程监测数据报文:{}", uploadChargeMonitorRequest.toString());
        ChannelEntity channel = ChannelMapByEntity.getChannel(incoming);
        Channel channel1 = null;
        if (null != channel) {
            channel1 = ChannelMapByEntity.getChannel(channel);
        }

        if (null == channel || !uploadChargeMonitorRequest.getPileNo().equals(channel.getPileNo())
                || (null != channel1 && !incoming.remoteAddress().equals(channel1.remoteAddress()))) {
            log.info("--------循道充电桩连接改变" + incoming.remoteAddress());
            ChannelEntity channelEntity = new ChannelEntity(uploadChargeMonitorRequest.getPileNo(), TradeType.fromCode(TradeType.XUN_DAO.getCode()));
            ChannelMapByEntity.removeChannel(channelEntity);
            ChannelMapByEntity.addChannel(channelEntity, incoming);
            ChannelMapByEntity.addChannel(incoming, channelEntity);
            ChannelMapByEntity.addPileType(uploadChargeMonitorRequest.getPileNo(), uploadChargeMonitorRequest.getPileType());
            ChannelMapByEntity.addPileType(uploadChargeMonitorRequest.getPileNo(), BytesUtil.copyBytes(msg, 8, 2));
        }
        int switchStatus = uploadChargeMonitorRequest.getSwitchStatus();
        BigDecimal highestAllowElectricity = uploadChargeMonitorRequest.getHighestAllowElectricity();
        String workStatus = uploadChargeMonitorRequest.getWorkStatus();
        GunStatusMapUtil.put(uploadChargeMonitorRequest.getPileNo(), TradeType.XUN_DAO, uploadChargeMonitorRequest.getGunNo(), switchStatus);
        GunElecAmountMapUtil.put(uploadChargeMonitorRequest.getPileNo(), TradeType.XUN_DAO, highestAllowElectricity);
        GunWorkStatusMapUtil.put(uploadChargeMonitorRequest.getPileNo(), TradeType.XUN_DAO, workStatus);

        UploadChargeMonitor uploadChargeMonitor = buildServiceEntity(uploadChargeMonitorRequest);
        //调用底层接口
        uploadChargeMonitorService.uploadChargeMonitor(uploadChargeMonitor);

        if (!"01".equals(workStatus) &&
                uploadChargeMonitorRequest.getCurrentChargeQuantity().compareTo(new BigDecimal(0)) > 0 &&
                (switchStatus == 1 || (switchStatus == 2 && (highestAllowElectricity != null &&
                highestAllowElectricity.compareTo(BigDecimal.ZERO) >= 0 &&
                highestAllowElectricity.compareTo(BigDecimal.ONE) <= 0)))) {

            UploadRecord uploadRecord = buildUploadRecordServiceEntity(uploadChargeMonitorRequest);
            log.info("循道充电状态上传账单" + JSON.toJSONString(uploadRecord));
            //添加serial
            //调用底层接口
            boolean flag = uploadRecordService.uploadRecord(uploadRecord);
        } else {
            log.info("厂商{}桩{}未触发上传账单,状态:{},输出电流:{},本次充电量:{}", TradeType.XUN_DAO, uploadChargeMonitorRequest.getPileNo(), switchStatus, highestAllowElectricity, uploadChargeMonitorRequest.getCurrentChargeQuantity());
        }
        //组装返回报文体
        return null;
    }

    private UploadChargeMonitor buildServiceEntity(XunDaoUploadChargeMonitorRequest uploadChargeMonitorRequest) {
        UploadChargeMonitor updateStatusReport = new UploadChargeMonitor();
        updateStatusReport.setTradeTypeCode(TradeType.XUN_DAO.getCode());
        updateStatusReport.setPileNo(uploadChargeMonitorRequest.getPileNo());
        //TODO 封装实体
//        updateStatusReport.setProtocolVersion();
        return updateStatusReport;
    }

    private UploadRecord buildUploadRecordServiceEntity(XunDaoUploadChargeMonitorRequest uploadRecordRequest) {
        UploadRecord uploadRecord = new UploadRecord();
        uploadRecord.setTradeTypeCode(TradeType.XUN_DAO.getCode());
        uploadRecord.setOrderNo(uploadRecordRequest.getOrderNo());
        uploadRecord.setPileNo(uploadRecordRequest.getPileNo());
        //99 标志监控状态上传
        uploadRecord.setEndReason(99);
        uploadRecord.setTotalAmmeterDegree(uploadRecordRequest.getCurrentChargeQuantity());
        try {

            uploadRecord.setSerial(Integer.parseInt(uploadRecordRequest.getSerial()));
        } catch (NumberFormatException e) {

        }
        uploadRecord.setPileType(uploadRecordRequest.getPileType());
        uploadRecord.setGunNo(uploadRecordRequest.getGunNo());
        return uploadRecord;
    }


}
