package com.piles.common.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.business.IBusinessFactory;
import com.piles.common.business.IBusinessHandler;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 用于循道报文解析
 */
@Slf4j
@Component("xunDaoBusinessHandler")
public  class XunDaoBusinessHander implements IBusinessHandler {

    @Resource(name="xunDaoBusinessFactory")
    IBusinessFactory xunDaoBusinessFactory;

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        //判断msg格式 I格式 S格式 U格式
        if(msg == null ){
            log.error("报文为空");
            return null;
        }
        if(msg.length > 253){
            log.error("报文长度为{},过长",msg.length);
            return null;
        }
        if(msg.length < 6){
            log.error("报文长度为{},不识别的报文格式",msg.length);
            return null;
        }
        if (0x68 != msg[0]) {
            log.error("报文起始位不是0x68",msg.length);
            return null;
        }
        //排除登录报文
        if(msg[1] != 0x01){//既不是I格式也不是S格式
            //验证报文长度
            int len = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 1, 1));
            if(len != (msg.length-2)){
                log.error("报文中标的长度{},与实际长度{}不匹配",len,(msg.length-2));
                return null;
            }
            //既不是I格式也不是S格式
            if(msg.length>6 &&  msg.length<13){
                log.error("报文长度为{},不识别的报文格式",msg.length);
                return null;
            }
            //I 格式需要验证crc
            if(msg.length>=13){
                int reason = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 8, 2));
                //原因在21和25之间的需要进行crc校验
                if(reason>=21&&reason<=25) {
                    byte[] dataBytes = BytesUtil.copyBytes(msg, 13, (msg.length - 13));
                    byte[] crcBytes = BytesUtil.copyBytes(msg, 10, 2);
                    byte[] checkBytes = CRC16Util.getXunDaoCRC(dataBytes);
                    if (!Integer.toHexString(  BytesUtil.bytesToIntLittle(checkBytes) ).equalsIgnoreCase( Integer.toHexString( BytesUtil.bytesToIntLittle(crcBytes)) )) {
                        log.error("CRC验证未通过");
                        return null;
                    }
                }
            }
        }


        IBusiness business = xunDaoBusinessFactory.getByMsg(msg);
        if (null != business) {
            return business.process( msg ,incoming);
        }
        return null;

    }
}
