package com.piles.record.business.impl;


import com.google.common.primitives.Bytes;
import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.UploadRecordRequest;
import com.piles.record.service.IUploadRecordService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * 上传充电记录接口实现
 */
@Slf4j
@Service("uploadRecordBusiness")
public class UploadRecordBusinessImpl extends BaseBusiness {

    @Resource
    private IUploadRecordService uploadRecordService;

    private static SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss" );


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        log.info( "接收到充电桩上传充电记录报文" );
        //依照报文体规则解析报文
        UploadRecordRequest uploadRecordRequest = UploadRecordRequest.packEntity( bodyBytes );
        uploadRecordRequest.setPileNo( ChannelMapByEntity.getChannel( incoming ).getPileNo() );
        uploadRecordRequest.setSerial(order);
        log.info( "接收到充电桩上传充电记录报文:{}", uploadRecordRequest.toString() );
        //调用底层接口
        UploadRecord uploadRecord = new UploadRecord();
        BeanUtils.copyProperties(uploadRecordRequest,uploadRecord);
        //设置厂商编码
        uploadRecord.setTradeTypeCode(TradeType.WEI_JING.getCode());
        uploadRecord.setOrderNo( String.valueOf(  uploadRecordRequest.getOrderNo()) );
        boolean flag = uploadRecordService.uploadRecord( uploadRecord );
        byte[] orderNo = BytesUtil.copyBytes( bodyBytes, 1, 8 );
        byte[] result = flag == true ? new byte[]{0x00} : new byte[]{0x01};
        byte[] responseBody = Bytes.concat( orderNo, result );
        //组装返回报文体
        return responseBody;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.UPLOAD_RECORD_ANSWER_CODE;
    }
}
