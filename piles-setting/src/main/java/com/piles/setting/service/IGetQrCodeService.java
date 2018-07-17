package com.piles.setting.service;


import com.piles.setting.domain.GetQrCodeRequest;
import com.piles.setting.domain.GetQrCodeResponse;

/**
 * 申请二维码接口
 */
public interface IGetQrCodeService {
    /**
     * 申请二维码接口
     * @param getQrCodeRequest 请求体
     * @return 成功返回boolean类型
     */
    GetQrCodeResponse getQrCode(GetQrCodeRequest getQrCodeRequest);
}
