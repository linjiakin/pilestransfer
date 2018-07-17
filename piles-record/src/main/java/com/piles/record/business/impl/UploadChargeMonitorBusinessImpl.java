package com.piles.record.business.impl;


import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.record.domain.UploadChargeMonitor;
import com.piles.record.entity.UploadChargeMonitorRequest;
import com.piles.record.service.IUploadChargeMonitorService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 上传充电过程监测数据 接口实现
 */
@Slf4j
@Service("uploadChargeMonitorBusiness")
public class UploadChargeMonitorBusinessImpl extends BaseBusiness {

    @Resource
    private IUploadChargeMonitorService uploadChargeMonitorService;


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        log.info("接收到蔚景充电桩上传充电过程监测数据报文");
        //依照报文体规则解析报文
        UploadChargeMonitorRequest uploadChargeMonitorRequest = UploadChargeMonitorRequest.packEntity(bodyBytes);
        uploadChargeMonitorRequest.setPileNo( ChannelMapByEntity.getChannel( incoming ).getPileNo() );
        log.info("接收到蔚景充电桩上传充电过程监测数据报文:{}", uploadChargeMonitorRequest.toString());
        UploadChargeMonitor uploadChargeMonitor = buildServiceEntity(uploadChargeMonitorRequest);
        //调用底层接口
        uploadChargeMonitorService.uploadChargeMonitor(uploadChargeMonitor);
        //组装返回报文体
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return null;
    }


    private UploadChargeMonitor buildServiceEntity(UploadChargeMonitorRequest uploadChargeMonitorRequest) {
        UploadChargeMonitor updateStatusReport = new UploadChargeMonitor();
        BeanUtils.copyProperties(uploadChargeMonitorRequest,updateStatusReport);
        updateStatusReport.setTradeTypeCode(TradeType.WEI_JING.getCode());
        updateStatusReport.setPileNo(uploadChargeMonitorRequest.getPileNo());
        //TODO 封装实体
//        updateStatusReport.setProtocolVersion();
        return updateStatusReport;
    }
}
