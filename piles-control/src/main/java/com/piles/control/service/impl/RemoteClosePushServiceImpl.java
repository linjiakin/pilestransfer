package com.piles.control.service.impl;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteClosePushRequest;
import com.piles.control.entity.RemoteCloseRequest;
import com.piles.control.service.IRemoteClosePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 远程关闭充电给充电桩发送消息实现类
 */
@Slf4j
@Service("remoteClosePushServiceImpl_1")
public class RemoteClosePushServiceImpl implements IRemoteClosePushService {

    @Resource(name = "pushBusinessImpl")
    IPushBusiness pushBusiness;
    @Override
    public BasePushCallBackResponse<RemoteCloseRequest>  doPush(RemoteClosePushRequest remoteClosePushRequest) {

        byte[] pushMsg=RemoteClosePushRequest.packBytes(remoteClosePushRequest);
        BasePushCallBackResponse<RemoteCloseRequest> basePushCallBackResponse=new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial( remoteClosePushRequest.getSerial() );
        boolean flag= pushBusiness.push(pushMsg,remoteClosePushRequest.getTradeTypeCode(),remoteClosePushRequest.getPileNo(),basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_OVER_CODE);
        if (!flag){
            basePushCallBackResponse.setCode( EPushResponseCode.CONNECT_ERROR );
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await(timeout, TimeUnit.MILLISECONDS);
            ChannelResponseCallBackMap.remove( remoteClosePushRequest.getTradeTypeCode(),remoteClosePushRequest.getPileNo(),remoteClosePushRequest.getSerial() );
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error( e.getMessage(),e );
        }
        return basePushCallBackResponse;
    }

    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;
}
