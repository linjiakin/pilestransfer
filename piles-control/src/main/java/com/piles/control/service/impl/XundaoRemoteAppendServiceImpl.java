package com.piles.control.service.impl;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.control.entity.RemoteAppendPushRequest;
import com.piles.control.entity.RemoteAppendRequest;
import com.piles.control.entity.RemoteStartPushRequest;
import com.piles.control.entity.RemoteStartRequest;
import com.piles.control.service.IRemoteAppendPushService;
import com.piles.control.service.IRemoteStartPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 远程开始充电给充电桩发送消息实现类
 */
@Slf4j
@Service("remoteAppendPushService_2")
public class XundaoRemoteAppendServiceImpl implements IRemoteAppendPushService {
    @Resource(name = "xunDaoPushBusinessImpl")
    IPushBusiness pushBusiness;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    @Override
    public BasePushCallBackResponse<RemoteAppendRequest> doPush(RemoteAppendPushRequest remoteAppendPushRequest) {
        byte[] pushMsg = RemoteAppendPushRequest.packBytesXunDao(remoteAppendPushRequest);
        BasePushCallBackResponse<RemoteAppendRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(remoteAppendPushRequest.getSerial());
        boolean flag = pushBusiness.push(pushMsg, remoteAppendPushRequest.getTradeTypeCode(), remoteAppendPushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag) {
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await(timeout, TimeUnit.MILLISECONDS);
            ChannelResponseCallBackMap.remove(remoteAppendPushRequest.getTradeTypeCode(), remoteAppendPushRequest.getPileNo(), remoteAppendPushRequest.getSerial());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return basePushCallBackResponse;
    }
}
