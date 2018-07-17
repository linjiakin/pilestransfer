package com.piles.setting.domain;

import lombok.Data;

/**
 * 上传充电记录接口请求实体
 * Created by zhanglizhi on 2018/1/7.
 */
@Data
public class UploadChageIpRecord {

    /**
     * 厂商编号
     */
    private int tradeTypeCode;

    /**
     * 桩编号
     */
    private String pileNo;

    /**
     * ip地址
     */
    private String addr;
    /**
     * 端口
     */
    private int port;
    /**
     * 桩类型
     */
    private int pileType;
}
