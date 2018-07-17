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
public class RemoteAppendPushRequest extends BasePushRequest implements Serializable {
    /**
     * 抢号 1 位  BIN 0: A枪 1: B枪
     */
    private int gunNo;
    /**
     * 充电模式 1位 BIN
     * 0: 自动充满 1: 定费冲  2:按电量充  3: 按时间充
     */
    private int chargeModel;
    /**
     * 充电数据 4位 BIN
     * 对应每种充电模式的数据。
     * 0：直到充满，填0
     * 1：按金额充，填金额大小，单位：元，精确到0.01
     * 3：按时间充，填时间长度，单位：秒
     * 2：按电量充，填电量大小，单位：度, 精确到0.01
     */
    private BigDecimal chargeData;

    /**
     * 订单号 8位 BIN
     */
    private long orderNo;


    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytesXunDao(RemoteAppendPushRequest request) {
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
        byte[] serial = BytesUtil.rightPadBytes(String.valueOf(request.getSerial()).getBytes(), 16, (byte) 0x00);
        byte[] orderNo = BytesUtil.rightPadBytes(String.valueOf(request.getOrderNo()).getBytes(), 32, (byte) 0x00);
        byte[] data = Bytes.concat(BytesUtil.str2BcdLittle(request.getPileNo()), orderNo,
                serial, BytesUtil.intToBytes(model, 1), BytesUtil.intToBytesLittle(dataint, 4), new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});


        byte[] head = new byte[]{0x68};
        byte[] length = new byte[]{0x4C};
        byte[] contrl = BytesUtil.xundaoControlInt2Byte(Integer.parseInt(request.getSerial()));
        byte[] type = new byte[]{(byte) 0x85};

        byte[] beiyong = BytesUtil.intToBytesLittle(request.getGunNo(), 1);
//        byte[] beiyong = 1 == request.getGunNo() ? new byte[]{0x00} : new byte[]{0x01};
        byte[] reason = ChannelMapByEntity.getPileTypeArr(request.getPileNo());
        byte[] crc = CRC16Util.getXunDaoCRC(data);
        byte[] addr = new byte[]{0x4A};


        byte[] temp = Bytes.concat(head, length, contrl, type, beiyong, reason, crc, addr, data);

        //组装返回报文体

        return temp;
    }

    public static void main(String[] args) {
        RemoteAppendPushRequest request = new RemoteAppendPushRequest();
        request.setGunNo(1);
        request.setChargeModel(4);
        request.setChargeData(new BigDecimal(0.1));
        request.setOrderNo(1223123L);
        RemoteAppendPushRequest.packBytesXunDao(request);
    }


}
