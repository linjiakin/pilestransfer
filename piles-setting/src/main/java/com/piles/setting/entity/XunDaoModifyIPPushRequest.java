package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 循道 下发修改ip地址 后台--》充电桩
 */
@Data
public class XunDaoModifyIPPushRequest extends BasePushRequest implements Serializable
{

    /**
     * 终端机器编 码 BCD 码 8Byte --16位设备编码，离散充电桩 资产编号，由运营监控系统提供
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
     * 封装报文体
     * @param request
     * @return
     */
    public static byte[] packBytes(XunDaoModifyIPPushRequest request){
        byte[] result = new byte[0];
        result = Bytes.concat(result,BytesUtil.str2BcdLittle(request.getPileNo()));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getAddr(),30,(byte)0x00));
        result = Bytes.concat(result,BytesUtil.intToBytesLittle(request.getPort(),2));
        return result;
    }




}
