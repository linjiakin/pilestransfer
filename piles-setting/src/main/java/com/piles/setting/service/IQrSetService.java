package com.piles.setting.service;

import com.piles.setting.entity.QrSetRequest;

/**
 * 二维码设置
 */
public interface IQrSetService {
    /**
     * 二维码设置
     * @param qrSetRequest 请求体
     * @return 二维码链接地址
     */
    String qrSet(QrSetRequest qrSetRequest);
}
