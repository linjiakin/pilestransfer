package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.RemoteUpdatePushRequest;
import com.piles.setting.entity.RemoteUpdateRequest;

import java.util.List;
import java.util.Map;

/**
 * 远程升级 运营中心主动调用服务
 */

public interface IRemoteUpdatePushService {
    /**
     * 远程升级 推送消息
     *
     * @param remoteUpdatePushRequest
     * @return
     */
    BasePushCallBackResponse<RemoteUpdateRequest> doPush(RemoteUpdatePushRequest remoteUpdatePushRequest);
    /**
     * 远程升级 推送消息
     *
     * @param remoteUpdatePushRequestList
     * @return
     */
    List<Map>  doBatchPush(List<RemoteUpdatePushRequest> remoteUpdatePushRequestList);
}
