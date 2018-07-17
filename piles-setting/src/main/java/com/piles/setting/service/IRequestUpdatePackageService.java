package com.piles.setting.service;

import com.piles.setting.entity.UpdatePackageRequest;
import com.piles.setting.entity.UpdatePackageResponse;

/**
 * 获取升级包数据
 */
public interface IRequestUpdatePackageService {
    /**
     * 根据段索引获取升级包信息
     *
     * @param updatePackageRequest 请求信息
     * @return
     */
    UpdatePackageResponse getUpdatePackage(UpdatePackageRequest updatePackageRequest);
}
