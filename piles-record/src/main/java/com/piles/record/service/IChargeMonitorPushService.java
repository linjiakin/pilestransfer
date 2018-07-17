package com.piles.record.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.BasePushRequest;
import com.piles.record.entity.XunDaoChargeMonitorRequest;

/**
 * 上传充电过程监测数据接口
 */
public interface IChargeMonitorPushService {
    /**
     * 上传充电过程监测数据
     *
     * @param basePushRequest 请求体
     */
    BasePushCallBackResponse<XunDaoChargeMonitorRequest> doPush(BasePushRequest basePushRequest);
}
