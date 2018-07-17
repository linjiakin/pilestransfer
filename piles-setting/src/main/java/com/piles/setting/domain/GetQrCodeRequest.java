package com.piles.setting.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请二维码参数
 */
@Data
public class GetQrCodeRequest implements Serializable {
    /**
     * 0：升级成功；
     * 1：升级失败，升级包md5校验失败
     * 2:桩体原因，取消升级
     */
    private int status;

    /**
     * 软件版本号
     */
    private String softVersion;
    /**
     * 充电桩编号
     */
    private String pileNo;
    /**
     * 厂商编码
     */
    private int tradeTypeCode;
    /**
     * 桩类型
     */
    private Integer pileType;

}
