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
 * 远程开始充电
 */
@Data
public class RemoteStartPushRequest extends BasePushRequest implements Serializable
{
    /**
     * 抢号 1 位  BIN 1: A枪 2: B枪
     */
    private int gunNo;
    /**
     * 充电模式 1位 BIN
     * 1: 自动充满 2: 按金额充  3: 按时间充 4: 按电量充 5 追加电量
     */
    private int chargeModel;
    /**
     * 充电数据 4位 BIN
     * 对应每种充电模式的数据。
     * 1：直到充满，填0
     * 2：按金额充，填金额大小，单位：元，精确到0.001
     * 3：按时间充，填时间长度，单位：秒
     * 4：按电量充，填电量大小，单位：度, 精确到0.001
     * 5：追加电量，填电量大小，单位：度, 精确到0.001
     */
    private BigDecimal chargeData;
    /**
     * 充电停止码 2位  BCD
     * 用户在屏幕输入充电停止码，可结束充电
     */
    private String chargeStopCode;
    /**
     * 订单号 8位 BIN
     */
    private long orderNo;

    /**
     * 封装报文体
     * @param request
     * @return
     */
    public static byte[] packBytes(RemoteStartPushRequest request){
        int gunNo = request.getGunNo();
        int chargeModel = request.getChargeModel();
        BigDecimal chargeData = request.getChargeData();
        String chargeStopCode = request.getChargeStopCode();
        long orderNo = request.getOrderNo();
        byte[] gunNoBytes = BytesUtil.intToBytes(gunNo,1);
        byte[] chargeModelBytes = BytesUtil.intToBytes(chargeModel,1);
        BigDecimal chargeDataInt = request.getChargeData();
        if(chargeModel==2 || chargeModel==4 || chargeModel==5){
            BigDecimal chargeDataVal = chargeData.multiply(BigDecimal.valueOf(1000));
            chargeDataInt = chargeDataVal;
        }else if(chargeModel==3){
            chargeDataInt = chargeData;
        }
        byte[] chargeDataBytes = BytesUtil.intToBytes(chargeDataInt.intValue(),4);
        byte[] chargeStopCodeBytes = BytesUtil.str2Bcd(chargeStopCode);
        if (chargeStopCodeBytes.length==1){
            chargeStopCodeBytes=Bytes.concat( new byte[]{0},chargeStopCodeBytes );
        }
        byte[] orderNoBytes = BytesUtil.long2Byte(orderNo);
        return Bytes.concat(gunNoBytes,chargeModelBytes,chargeDataBytes,chargeStopCodeBytes,orderNoBytes);
    }

    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytesXunDao(RemoteStartPushRequest request) {
        int model = request.getChargeModel();

        BigDecimal chargeData = request.getChargeData();
        int dataint = 0;

        switch (model) {
            case 1:
                //充满
                model = 0;
                break;
            case 2:
                //定费
                dataint = chargeData.multiply(new BigDecimal(100)).intValue();
                model = 1;
                break;
            case 4:
                //定量
                dataint = chargeData.multiply(new BigDecimal(100)).intValue();
                model = 2;
                break;

            case 3:
                //定时
                dataint = chargeData.divide(new BigDecimal(60), 0).intValue();
                model = 3;
                break;
        }
        byte[] data = Bytes.concat(BytesUtil.str2BcdLittle(request.getPileNo()), new byte[]{0x01}, BytesUtil.intToBytes(model, 1), BytesUtil.intToBytesLittle(dataint, 4));
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
        RemoteStartPushRequest request = new RemoteStartPushRequest();
        request.setGunNo(1);
        request.setChargeModel(4);
        request.setChargeData(new BigDecimal(0.1));
        request.setChargeStopCode("4");
        request.setOrderNo(1223123L);
        RemoteStartPushRequest.packBytes(request);
    }



}
