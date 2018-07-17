package com.piles.control.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.control.entity.RemoteAppendPushRequest;
import com.piles.control.entity.RemoteAppendRequest;
import com.piles.control.entity.RemoteStartPushRequest;
import com.piles.control.entity.RemoteStartRequest;

/**
 * 续充 运营中心主动调用服务
 */
public interface IRemoteAppendPushService {
    /**
     * 续充 推送消息
     *
     * @param remoteAppendPushRequest
     * @return
     */
    BasePushCallBackResponse<RemoteAppendRequest> doPush(RemoteAppendPushRequest remoteAppendPushRequest);
}
