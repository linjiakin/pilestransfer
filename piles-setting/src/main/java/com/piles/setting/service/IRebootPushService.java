package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.RebootPushRequest;
import com.piles.setting.entity.RebootRequest;

/**
 * 重启 运营中心主动调用服务
 */

public interface IRebootPushService {
    /**
     * 重启 推送消息
     *
     * @param rebootPushRequest
     * @return
     */
    BasePushCallBackResponse<RebootRequest> doPush(RebootPushRequest rebootPushRequest);
}
