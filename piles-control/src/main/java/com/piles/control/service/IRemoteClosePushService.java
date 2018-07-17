package com.piles.control.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.control.entity.RemoteClosePushRequest;
import com.piles.control.entity.RemoteCloseRequest;

/**
 * 远程关闭充电 运营中心主动调用服务
 */

public interface IRemoteClosePushService {
    /**
     * 远程关闭充电 推送消息
     * @param remoteClosePushRequest
     * @return
     */
    BasePushCallBackResponse<RemoteCloseRequest> doPush(RemoteClosePushRequest remoteClosePushRequest);
}
