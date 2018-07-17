package com.piles.setting.entity;

import com.piles.common.entity.ChannelEntity;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import io.netty.channel.Channel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 升级状态汇报
 */
@Data
public class UpdateStatusRequest implements Serializable {
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
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static UpdateStatusRequest packEntity(byte[] msg, Channel income) {
        ChannelEntity channelEntity = ChannelMapByEntity.getChannel(income);
        if (null==channelEntity) {
            return null;
        }
        UpdateStatusRequest request = new UpdateStatusRequest();

        request.setStatus(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 0, 1), 10)));
        request.setSoftVersion("V" + Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 1, 1), 10)) + "." +
                Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 2, 1), 10)));
        request.setProtocolVersion("V" + Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 3, 1), 10)) + "." +
                Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 4, 1), 10)));
        request.setPileNo(channelEntity.getPileNo());
        return request;
    }

    @Override
    public String toString() {
        return "UpdateStatusRequest{" +
                "status=" + status +
                ", softVersion='" + softVersion + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", pileNo='" + pileNo + '\'' +
                '}';
    }
}
