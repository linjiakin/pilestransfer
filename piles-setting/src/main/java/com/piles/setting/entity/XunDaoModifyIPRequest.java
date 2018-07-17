package com.piles.setting.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 循道修改ip地址程序下行
 */
@Data
public class XunDaoModifyIPRequest extends BasePushResponse implements Serializable
{
    /**
     * 终端机器编 码 BCD 码 8Byte
     */
    private String pileNo;
    /**
     * 收到指令参数确认 BIN 码 1Byte 00-成功 01-失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static XunDaoModifyIPRequest packEntity(byte[] msg){
        XunDaoModifyIPRequest request=new XunDaoModifyIPRequest();
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg,0,8)));
        request.setResult(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,8,1)));
        return request;
    }

}
