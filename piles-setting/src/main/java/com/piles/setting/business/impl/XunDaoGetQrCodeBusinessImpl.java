package com.piles.setting.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.setting.domain.GetQrCodeRequest;
import com.piles.setting.domain.GetQrCodeResponse;
import com.piles.setting.service.IGetQrCodeService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 循道 申请二维码(充电桩-->后台)
 */
@Slf4j
@Service("xunDaoGetQrCodeBusiness")
public class XunDaoGetQrCodeBusinessImpl implements IBusiness {
    @Resource
    IGetQrCodeService getQrCodeService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到循道充电桩获取二维码报文");
        String pileNo = BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, 13, 8));
        int tradeTypeCode = TradeType.XUN_DAO.getCode();
        Integer pileType = ChannelMapByEntity.getPileType(pileNo);
        GetQrCodeRequest getQrCodeRequest = new GetQrCodeRequest();
        getQrCodeRequest.setPileNo(pileNo);
        getQrCodeRequest.setTradeTypeCode(tradeTypeCode);
        getQrCodeRequest.setPileType(pileType);

        GetQrCodeResponse getQrCodeResponse = getQrCodeService.getQrCode(getQrCodeRequest);
        log.info("请求后台获取二维码的结果:{}",getQrCodeResponse);
        byte[] dataBytes;
        if (getQrCodeResponse == null) {
            //认为请求失败
            dataBytes = buildFailData(msg);
        } else {
            if (getQrCodeResponse.getCode() != 0 || getQrCodeResponse.getQrCode() == null) {
                dataBytes = buildFailData(msg);
            }
            dataBytes = buildSuccessData(msg,getQrCodeResponse.getQrCode());
        }
        byte[] head = buildHead(pileNo, dataBytes, BytesUtil.copyBytes(msg, 2, 4));
        return head;
    }

    private byte[] buildSuccessData(byte[] msg, String qrCode) {
        return Bytes.concat(BytesUtil.copyBytes(msg, 13, 8), new byte[]{0x00}, BytesUtil.rightPadBytes(qrCode, 100, (byte) 0x00));
    }

    private byte[] buildFailData(byte[] msg) {
        byte[] bytes = {};
        return Bytes.concat(BytesUtil.copyBytes(msg, 13, 8), new byte[]{0x01}, BytesUtil.rightPadBytes(bytes, 100, (byte) 0x00));
    }

    //添加报文头
    private byte[] buildHead(String pileNo, byte[] dataMsg, byte[] serial) {
        byte[] result = Bytes.concat(new byte[]{0x68, 0x64});
        result = Bytes.concat(result, serial);
        //添加类型标识
        result = Bytes.concat(result, new byte[]{(byte) 0x85, 0x00});
        //添加桩类型
        result = Bytes.concat(result, ChannelMapByEntity.getPileTypeArr(pileNo));
        //添加crc
        result = Bytes.concat(result, CRC16Util.getXunDaoCRC(dataMsg));
        //添加记录类型
        result = Bytes.concat(result, new byte[]{0x49});

        result = Bytes.concat(result, dataMsg);

        return result;
    }

}
