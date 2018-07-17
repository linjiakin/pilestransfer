package com.piles.setting.entity;

import com.piles.common.entity.ChannelEntity;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import io.netty.channel.Channel;
import lombok.Data;

import java.io.Serializable;

/**
 * 循道升级状态汇报
 */
@Data
public class XunDaoUpdateStatusRequest implements Serializable {

    /**
     * 终端机器编 码 BCD 码 8Byte
     */
    private String pileNo;

    /**
     * 字符串，20长度 不足尾部 补 0x00 字符串 比如:XD01-V1.1234
     *  软件版本号	BIN	2	点号前后各占一个字节。如V1.0表示为0x01 0x00,V1.10表示为0x01 0x0A
     */
    private String softVersion;

    /**
     * 升级结果 BIN 码 1Byte 小端
     * 0:升级成功(非 0 为失败) 1:非本机程序 2:升级文件校验不对 3:升级文件不能成功下载 4:其它
     */
    private int status;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static XunDaoUpdateStatusRequest packEntity(byte[] msg) {

        int cursor = 0;
        XunDaoUpdateStatusRequest request = new XunDaoUpdateStatusRequest();
        request.setPileNo( BytesUtil.bcd2StrLittle( BytesUtil.copyBytes( msg, cursor, 8 ) ) );
        cursor += 8;
        byte[] versionBytes = BytesUtil.copyBytes(msg, cursor, 20);
        cursor += 20;
        int i = 0;
        while(versionBytes[i] != 0x00){
            i++;
        }
        versionBytes = BytesUtil.copyBytes(versionBytes, 0, i);
        request.setSoftVersion(new String(versionBytes));
        request.setStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,cursor,1)));
        return request;
    }

    @Override
    public String toString() {
        return "XunDaoUpdateStatusRequest{" +
                "pileNo='" + pileNo + '\'' +
                ", softVersion='" + softVersion + '\'' +
                ", status=" + status +
                '}';
    }
}
