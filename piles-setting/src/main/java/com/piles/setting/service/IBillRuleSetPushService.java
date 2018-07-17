package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.BillRuleSetPushRequest;
import com.piles.setting.entity.BillRuleSetRequest;

/**
 * 计费规则设置 运营中心主动调用服务
 */

public interface IBillRuleSetPushService {
    /**
     * 计费规则设置 推送消息
     *
     * @param billRuleSetPushRequest
     * @return
     */
    BasePushCallBackResponse<BillRuleSetRequest> doPush(BillRuleSetPushRequest billRuleSetPushRequest);
}
