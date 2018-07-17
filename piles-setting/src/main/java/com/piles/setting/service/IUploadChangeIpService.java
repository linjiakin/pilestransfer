package com.piles.setting.service;


import com.piles.setting.domain.UploadChageIpRecord;

/**
 * 上传充电记录接口
 */
public interface IUploadChangeIpService {
    /**
     * 上传充电记录
     * @param uploadRecord 请求体
     * @return 成功返回boolean类型
     */
    boolean uploadRecord(UploadChageIpRecord uploadRecord);
}
