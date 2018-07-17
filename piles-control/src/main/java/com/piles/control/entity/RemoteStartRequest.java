package com.piles.control.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 远程开始充电
 */
@Data
public class RemoteStartRequest extends BasePushResponse implements Serializable
{
    /**
     * 订单号 8位 BIN
     */
    private long orderNo;
    /**
     * 结果 1位 BIN    0: 启动成功 1: 枪被预约 2: 其他原因失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static RemoteStartRequest packEntity(byte[] msg){
        RemoteStartRequest request=new RemoteStartRequest();
        request.setOrderNo(BytesUtil.byte2Long(BytesUtil.copyBytes(msg,0,8)));
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,8,1),10)));
        return request;
    }


    public static void main(String[] args) {
//        byte[] msg= new byte[]{0x10,0x00,0x02,0x54,(byte)0x84,0x56,0x18,0x35,0x02,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x00,0x01,0x02,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x01};
//        packEntity(msg);

    }



}
