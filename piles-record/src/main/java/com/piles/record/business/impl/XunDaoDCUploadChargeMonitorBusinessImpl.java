package com.piles.record.business.impl;


import com.alibaba.fastjson.JSON;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.*;
import com.piles.record.domain.UploadChargeMonitor;
import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.XunDaoDCUploadChargeMonitorRequest;
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
@Service("xunDaoDCUploadChargeMonitorBusiness")
public class XunDaoDCUploadChargeMonitorBusinessImpl implements IBusiness {

    @Resource
    private IUploadChargeMonitorService uploadChargeMonitorService;
    @Resource
    private IUploadRecordService uploadRecordService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到循道DC充电桩上传充电过程监测数据报文");
        byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));

        //依照报文体规则解析报文
        XunDaoDCUploadChargeMonitorRequest uploadChargeMonitorRequest = XunDaoDCUploadChargeMonitorRequest.packEntity(dataBytes);
        uploadChargeMonitorRequest.setGunNo(MsgHelper.getGunNo(msg));
        Integer pileType = Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 8, 1), 10));
        pileType = pileType == 2 ? 3 : pileType;
        uploadChargeMonitorRequest.setPileType(pileType);
        log.info("接收到循道充DC电桩上传充电过程监测数据报文:{}", uploadChargeMonitorRequest.toString());
        ChannelEntity channel = ChannelMapByEntity.getChannel(incoming);
        Channel channel1 = null;
        if (null != channel) {
            channel1 = ChannelMapByEntity.getChannel(channel);
        }

        if (null == channel || !uploadChargeMonitorRequest.getPileNo().equals(channel.getPileNo())
                || (null != channel1 && !incoming.remoteAddress().equals(channel1.remoteAddress()))) {
            log.info("--------循道DC充电桩连接改变" + incoming.remoteAddress());
            ChannelEntity channelEntity = new ChannelEntity(uploadChargeMonitorRequest.getPileNo(), TradeType.fromCode(TradeType.XUN_DAO.getCode()));
            ChannelMapByEntity.removeChannel(channelEntity);
            ChannelMapByEntity.addChannel(channelEntity, incoming);
            ChannelMapByEntity.addChannel(incoming, channelEntity);
            ChannelMapByEntity.addPileType(uploadChargeMonitorRequest.getPileNo(), uploadChargeMonitorRequest.getPileType());
            ChannelMapByEntity.addPileType(uploadChargeMonitorRequest.getPileNo(), BytesUtil.copyBytes(msg, 8, 2));
        }
        int switchStatus = uploadChargeMonitorRequest.getSwitchStatus();
        String workStatus = uploadChargeMonitorRequest.getWorkStatus();
        GunStatusMapUtil.putDC(uploadChargeMonitorRequest.getPileNo(), TradeType.XUN_DAO,uploadChargeMonitorRequest.getGunNo(), switchStatus+","+workStatus);

        UploadChargeMonitor uploadChargeMonitor = buildServiceEntity(uploadChargeMonitorRequest);
        //调用底层接口
        uploadChargeMonitorService.uploadChargeMonitor(uploadChargeMonitor);
        //组装返回报文体
        return null;
    }

    private UploadChargeMonitor buildServiceEntity(XunDaoDCUploadChargeMonitorRequest uploadChargeMonitorRequest) {
        UploadChargeMonitor updateStatusReport = new UploadChargeMonitor();
        updateStatusReport.setTradeTypeCode(TradeType.XUN_DAO.getCode());
        updateStatusReport.setPileNo(uploadChargeMonitorRequest.getPileNo());
        //TODO 封装实体
        return updateStatusReport;
    }

}
