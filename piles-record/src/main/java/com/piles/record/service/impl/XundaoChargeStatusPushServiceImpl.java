package com.piles.record.service.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.record.entity.XunDaoChargeMonitorRequest;
import com.piles.record.service.IChargeMonitorPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 远程开始充电给充电桩发送消息实现类
 */
@Slf4j
@Service("chargeStatusPushService_2")
public class XundaoChargeStatusPushServiceImpl implements IChargeMonitorPushService {
    @Resource(name = "xunDaoPushBusinessImpl")
    IPushBusiness pushBusiness;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    @Override
    public BasePushCallBackResponse<XunDaoChargeMonitorRequest> doPush(BasePushRequest basePushRequest) {
        byte[] pushMsg = packBytesXunDao( basePushRequest );
        BasePushCallBackResponse<XunDaoChargeMonitorRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial( basePushRequest.getSerial() );
        boolean flag = pushBusiness.push( pushMsg, basePushRequest.getTradeTypeCode(), basePushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE );
        if (!flag) {
            basePushCallBackResponse.setCode( EPushResponseCode.CONNECT_ERROR );
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await( timeout, TimeUnit.MILLISECONDS );
            ChannelResponseCallBackMap.remove( basePushRequest.getTradeTypeCode(), basePushRequest.getPileNo(), basePushRequest.getSerial() );
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error( e.getMessage(), e );
        }
        return basePushCallBackResponse;
    }

    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytesXunDao(BasePushRequest request) {

        byte[] data = BytesUtil.str2BcdLittle( request.getPileNo() );
        byte[] head = new byte[]{0x68};
        byte[] length = new byte[]{0x13};
        byte[] contrl = BytesUtil.xundaoControlInt2Byte( Integer.parseInt( request.getSerial() ) );
        byte[] type = new byte[]{(byte) 0x85};
        int gunNo = request.getGunNo();
        byte[] beiyong = BytesUtil.intToBytesLittle(gunNo,1);//枪号
        byte[] reason = ChannelMapByEntity.getPileTypeArr(request.getPileNo());
        byte[] crc = CRC16Util.getXunDaoCRC( data );
        byte[] addr = new byte[]{0x46};


        byte[] temp = Bytes.concat( head, length, contrl, type, beiyong, reason, crc, addr, data );

        //组装返回报文体

        return temp;
    }
}
