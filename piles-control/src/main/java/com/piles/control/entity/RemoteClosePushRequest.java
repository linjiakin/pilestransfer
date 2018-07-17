package com.piles.control.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 远程结束充电
 */
@Data
public class RemoteClosePushRequest extends BasePushRequest implements Serializable
{
    /**
     * 抢号 1 位  BIN 1: A枪 2: B枪
     */
    private int gunNo;
    /**
     * 订单号 8位 BIN long
     */
    private long orderNo;

    /**
     * 封装报文体
     * @param request
     * @return
     */
    public static byte[] packBytes(RemoteClosePushRequest request){
        int gunNo = request.getGunNo();
        long orderNo = request.getOrderNo();
        byte[] gunNoBytes = BytesUtil.intToBytes(gunNo,1);
        byte[] orderNoBytes = BytesUtil.long2Byte(orderNo);
        return Bytes.concat(gunNoBytes,orderNoBytes);
    }

    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytesXundao(RemoteClosePushRequest request) {

        byte[] data = Bytes.concat(BytesUtil.str2BcdLittle(request.getPileNo()), new byte[]{0x02}, BytesUtil.intToBytes(0, 1), BytesUtil.intToBytesLittle(0, 4));
        byte[] serial = BytesUtil.rightPadBytes(String.valueOf(request.getSerial()).getBytes(), 16, (byte) 0x00);
        byte[] orderNo = BytesUtil.rightPadBytes(String.valueOf(request.getOrderNo()).getBytes(), 32, (byte) 0x00);
        data = Bytes.concat(data, serial, orderNo);
        byte[] head = new byte[]{0x68};
        byte[] length = new byte[]{0x49};
        byte[] contrl = BytesUtil.xundaoControlInt2Byte(Integer.parseInt(request.getSerial()));
        byte[] type = new byte[]{(byte) 0x85};
        byte[] beiyong = BytesUtil.intToBytesLittle(request.getGunNo(),1);
//        byte[] beiyong = 1 == request.getGunNo() ? new byte[]{0x00} : new byte[]{0x01};
        byte[] reason = ChannelMapByEntity.getPileTypeArr(request.getPileNo());
        byte[] crc = CRC16Util.getXunDaoCRC(data);
        byte[] addr = new byte[]{0x1C};


        byte[] temp = Bytes.concat(head, length, contrl, type, beiyong, reason, crc, addr, data);

        //组装返回报文体

        return temp;
    }


    public static void main(String[] args) {
        RemoteClosePushRequest remoteCloseResponse = new RemoteClosePushRequest();
        remoteCloseResponse.setGunNo(1);
        remoteCloseResponse.setOrderNo(4545454L);
        byte[] bytes = RemoteClosePushRequest.packBytes(remoteCloseResponse);
        System.out.println(BytesUtil.byte2Long(BytesUtil.copyBytes(bytes,1,bytes.length-1)));

    }


}
