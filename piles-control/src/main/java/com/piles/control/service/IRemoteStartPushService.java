package com.piles.control.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.control.entity.RemoteClosePushRequest;
import com.piles.control.entity.RemoteStartPushRequest;
import com.piles.control.entity.RemoteStartRequest;

/**
 * 远程启动充电 运营中心主动调用服务
 */
public interface IRemoteStartPushService {
    /**
     * 远程启动充电 推送消息
     * @param remoteStartPushRequest
     * @return
     */
    BasePushCallBackResponse<RemoteStartRequest> doPush(RemoteStartPushRequest remoteStartPushRequest);
}
