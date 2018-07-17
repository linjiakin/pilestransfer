package com.piles.control.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 续充
 */
@Data
public class RemoteAppendRequest extends BasePushResponse implements Serializable {

    /**
     * 默认必填充电桩编号
     */
    private String pileNo;
    /**
     * 订单号 8位 BIN
     */
    private long orderNo;
    /**
     * 结果 1位 BIN    0: 启动成功 1: 失败
     */
    private int result;

    /**
     * 失败原因
     * 00-无失败 01-已不在充电中 02-充电方式不一致 03-订单号不同 04-续充总金额小于当前值 05-其它
     */
    private int reason;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static RemoteAppendRequest packEntity(byte[] msg) {
        RemoteAppendRequest request = new RemoteAppendRequest();
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, 0, 8)));
        byte[] orderNos = BytesUtil.copyBytes(msg, 8, 32);
        int i = 0;
        while (orderNos[i] != 0x00) {
            i++;
        }
        request.setOrderNo(Long.valueOf(BytesUtil.ascii2Str(BytesUtil.copyBytes(orderNos, 0, i))));
        byte[] serials = BytesUtil.copyBytes(msg, 40, 16);
        int j = 0;
        while (serials[j] != 0x00) {
            j++;
        }
        request.setSerial(BytesUtil.ascii2Str(BytesUtil.copyBytes(serials, 0, j)));
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 56, 1), 10)));
        request.setReason(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 57, 1), 10)));
        return request;
    }


    public static void main(String[] args) {
//        byte[] msg= new byte[]{0x10,0x00,0x02,0x54,(byte)0x84,0x56,0x18,0x35,0x02,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x00,0x01,0x02,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x01};
//        packEntity(msg);

    }


}
