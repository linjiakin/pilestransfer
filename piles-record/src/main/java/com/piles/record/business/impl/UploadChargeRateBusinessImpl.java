package com.piles.record.business.impl;


import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.record.entity.UploadChargeRateRequest;
import com.piles.record.service.IUploadChargeRateService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 上传充电进度接口实现
 */
@Slf4j
@Service("uploadChargeRateBusiness")
public class UploadChargeRateBusinessImpl extends BaseBusiness {

    @Resource
    private IUploadChargeRateService uploadChargeRateService;


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        log.info( "接收到充电桩上传充电进度报文" );
        //依照报文体规则解析报文
        UploadChargeRateRequest uploadChargeRateRequest = UploadChargeRateRequest.packEntity( bodyBytes );
        uploadChargeRateRequest.setPileNo( ChannelMapByEntity.getChannel( incoming ).getPileNo() );
        log.info( "接收到充电桩上传充电进度报文:{}", uploadChargeRateRequest.toString() );
        //调用底层接口
        boolean flag = uploadChargeRateService.uploadChargeRate( uploadChargeRateRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return null;
    }
}
