package com.piles.setting.entity;

import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 循道修改ip地址程序上报请求
 */
@Data
public class XunDaoModifyIPReportRequest implements Serializable {
    /**
     * 终端机器编 码 BCD 码 8Byte
     */
    private String pileNo;
    /**
     * 修改的ip或者域名 ASCII 30
     * 如果是 IP:192.168.1.25 31 39 32 2E 31 36 38 2E 31 2E 32 35 不足补 00 域名直接按照字符串下发不 足补 00
     */
    private String addr;
    /**
     * 端口 BIN 2Byte 例如:8010 4A 1F
     */
    private int port;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static XunDaoModifyIPReportRequest packEntity(byte[] msg) {
        XunDaoModifyIPReportRequest request = new XunDaoModifyIPReportRequest();
        request.setPileNo( BytesUtil.bcd2StrLittle( BytesUtil.copyBytes( msg, 0, 8 ) ) );
        int i = 8;
        while(msg[i] != 0x00){
            i++;
        }
        request.setAddr(  BytesUtil.ascii2Str( BytesUtil.copyBytes( msg, 8, i-8 )  ) );
        request.setPort(BytesUtil.bytesToIntLittle(  BytesUtil.copyBytes(msg,38,2))  );
        return request;
    }


}
