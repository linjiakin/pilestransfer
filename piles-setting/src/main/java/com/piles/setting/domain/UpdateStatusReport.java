package com.piles.setting.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 升级状态汇报service bean
 */
@Data
public class UpdateStatusReport implements Serializable {
    /**
     * 0：升级成功；
     * 1：升级失败，升级包md5校验失败
     * 2:桩体原因，取消升级
     */
    private int status;

    /**
     * 软件版本号	BIN	2	点号前后各占一个字节。如V1.0表示为0x01 0x00,V1.10表示为0x01 0x0A
     */
    private String softVersion;
    /**
     * 通信协议版本号	BIN	2	格式同上
     */
    private String protocolVersion;
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
    private int pileType;

    @Override
    public String toString() {
        return "UpdateStatusReport{" +
                "status=" + status +
                ", softVersion='" + softVersion + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", pileNo='" + pileNo + '\'' +
                ", tradeTypeCode=" + tradeTypeCode +
                ", pileType=" + pileType +
                '}';
    }
}
