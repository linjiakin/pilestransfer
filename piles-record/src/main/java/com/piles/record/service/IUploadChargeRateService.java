package com.piles.record.service;

import com.piles.record.entity.UploadChargeRateRequest;

/**
 * 上传充电进度接口
 */
public interface IUploadChargeRateService {
    /**
     * 上传充电进度
     *
     * @param uploadChargeRateRequest 请求体
     */
    boolean uploadChargeRate(UploadChargeRateRequest uploadChargeRateRequest);
}
