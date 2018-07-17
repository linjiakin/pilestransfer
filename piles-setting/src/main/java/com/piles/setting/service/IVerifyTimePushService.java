package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.VerifyTimePushRequest;
import com.piles.setting.entity.VerifyTimeRequest;

/**
 * 校时推送消息 运营中心主动调用服务
 */

public interface IVerifyTimePushService {
    /**
     * 校时推送消息 推送消息
     *
     * @param verifyTimePushRequest
     * @return
     */
    BasePushCallBackResponse<VerifyTimeRequest> doPush(VerifyTimePushRequest verifyTimePushRequest);
}
