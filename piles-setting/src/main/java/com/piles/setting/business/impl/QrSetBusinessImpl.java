package com.piles.setting.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.BytesUtil;
import com.piles.setting.entity.QrSetRequest;
import com.piles.setting.service.IQrSetService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 二维码设置
 */
@Slf4j
@Service("qrSetBusiness")
public class QrSetBusinessImpl extends BaseBusiness{


    @Resource
    private IQrSetService qrSetService;


    @Override
    protected byte[] processBody(byte[] bodyBytes,Channel incoming,int order) {
        //依照报文体规则解析报文
        QrSetRequest qrSetRequest = QrSetRequest.packEntity(bodyBytes,incoming);
        //调用底层接口
        String qrContent = qrSetService.qrSet(qrSetRequest);
        if(qrContent == null){
            qrContent = "";
        }
        log.info("获取后台返回二维码：{}",qrContent);
        byte[] lenBytes = BytesUtil.intToBytes(qrContent.length(), 2);
        // 枪号+二维码长度+二维码内容
        byte[] responseBody = Bytes.concat(bodyBytes,lenBytes,qrContent.getBytes());
        //组装返回报文体
        return responseBody;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.QR_SET_ANSWER_CODE;
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{0x22,0x33};
        String qrContent = "";
        byte[] lenBytes = BytesUtil.intToBytes(qrContent.length(), 2);
        // 枪号+二维码长度+二维码内容
        byte[] responseBody = Bytes.concat(bytes,lenBytes,qrContent.getBytes());
        System.out.println(responseBody);
    }
}
