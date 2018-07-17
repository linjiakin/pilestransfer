package com.piles.setting.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请二维码返回结果
 */
@Data
public class GetQrCodeResponse implements Serializable {

    /**
     * 0：成功  1：失败
     */
    private int code;
    /**
     * 充电桩编号
     */
    private String pileNo;
    /**
     * 二维码
     */
    private String qrCode;

}
