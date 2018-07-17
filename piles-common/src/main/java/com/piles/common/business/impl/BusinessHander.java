package com.piles.common.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.business.IBusinessFactory;
import com.piles.common.business.IBusinessHandler;
import com.piles.common.util.CRC16Util;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Component("businessHandler")
public  class BusinessHander implements IBusinessHandler {

    @Resource(name="businessFactory")
    IBusinessFactory businessFactory;

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        //报文校验不通过则抛弃
        if (CRC16Util.checkMsg( msg )){
            log.info("CRC校验通过");
            return processService( msg,incoming );
        }
        log.error("CRC验证未通过");
        return null;
    }



    public byte[] processService(byte[] msg, Channel incoming) {
        IBusiness iBusiness = null;
        iBusiness = businessFactory.getByMsg( msg );
//        iBusiness = businessFactory.getByOrder( msg[1] );
        if (null != iBusiness) {
            return iBusiness.process( msg ,incoming);
        }
        return null;
    }
}
