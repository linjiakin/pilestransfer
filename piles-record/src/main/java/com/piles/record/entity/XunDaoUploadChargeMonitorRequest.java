package com.piles.record.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 循道上传充电过程监测数据接口请求实体
 */
@Data
public class XunDaoUploadChargeMonitorRequest implements Serializable {
    private int pileType;
    private int gunNo;


    //充电输出电压(直 流最大输出电压)	BIN	2	精确到小数点后一位
    private BigDecimal highestAllowVoltage;
    //充电输出电流(直 流最大输出电流)	BIN	2	单位：A，精确到小数点后二位
    private BigDecimal highestAllowElectricity;
    //输出继电器状态  BIN 码 1Byte 布尔型, 变化上传;0 断开，1:闭合
    private int outputRelayStatus;
    //连接确认开关状态 BIN 码 1Byte 变化上传;0:断开， 1:连接，2:可充电， 3:故障状态
    private int switchStatus;
    //有功总电度 BIN 码 4Byte  精确到小数点后两位
    private BigDecimal activElectricalDegree;
    //桩编号 8位 BCD
    private String pileNo;
    //是否连接电池 BIN 码 1Byte
    private int connectBattery;
    // 工作状态 BCD 码 1Byte 0x00 离线，0x01 故障 0x02 待机，0x03 充电 04 停止充电 0x10 暂停，0x11 维护 0x12 测试
    private String workStatus;
    /**
     * 故障状态 Bin 码 1Byte 共 8bit
     * 第 0bit:读卡器状态 0:正常，1 故障
     * 第 1bit:电表状态 0: 正常，1 故障
     * 第 2bit:急停状态 0: 正常，1 故障
     * 第 3bit:过压状态 0: 正常，1 故障
     * 第 4bit:欠压状态 0: 正常，1 故障
     * 第 5it:过流状态 0: 正常，1 故障
     * 第 6bit:充电机状态 0:正常，1 故障
     * 第 7bit:其它状态 0: 正常，1 故障
     */
    private int troubleStatus;
    //充电时长 BIN 2 字节 分
    private int chargeDuration;
    //本次充电电量 BIN 4 字节
    private BigDecimal currentChargeQuantity;
    //交易流水号	BCD 码16Byte
    private String serial;
    //订单号 ascii 32位小端
    private String orderNo;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static XunDaoUploadChargeMonitorRequest packEntity(byte[] msg) {
        XunDaoUploadChargeMonitorRequest request = new XunDaoUploadChargeMonitorRequest();
        int cursor = 0;
        request.setHighestAllowVoltage(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setHighestAllowElectricity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setOutputRelayStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setSwitchStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setActivElectricalDegree(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 8)));
        cursor += 8;
        request.setConnectBattery(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setWorkStatus(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setTroubleStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setChargeDuration(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2)));
        cursor += 2;
        request.setCurrentChargeQuantity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));


        if (msg.length > 25) {
            cursor += 4;
            byte[] serials = BytesUtil.copyBytes(msg, cursor, 16);
            int i = 0;
            while (serials[i] != 0x00) {
                i++;
            }
            request.setSerial(new String(BytesUtil.copyBytes(serials, 0, i)));
            cursor += 16;
            byte[] orderNos = BytesUtil.copyBytes(msg, cursor, 32);
            i = 0;
            while (orderNos[i] != 0x00) {
                i++;
            }
            String orderNo = new String(BytesUtil.copyBytes(orderNos, 0, i));
            if (orderNo.length() > 0 && '\u0006' == orderNo.charAt(0)) {
                orderNo = orderNo.substring(1);
            }
            request.setOrderNo(orderNo);
        }

        return request;
    }

    public static byte[] packBytes(XunDaoUploadChargeMonitorRequest request) {
        byte[] responseBytes = new byte[]{};
        return responseBytes;
    }


    @Override
    public String toString() {
        return "XunDaoUploadChargeMonitorRequest{" +
                "highestAllowVoltage=" + highestAllowVoltage +
                ", highestAllowElectricity=" + highestAllowElectricity +
                ", outputRelayStatus=" + outputRelayStatus +
                ", switchStatus=" + switchStatus +
                ", activElectricalDegree=" + activElectricalDegree +
                ", pileNo='" + pileNo + '\'' +
                ", connectBattery=" + connectBattery +
                ", workStatus='" + workStatus + '\'' +
                ", troubleStatus=" + troubleStatus +
                ", chargeDuration=" + chargeDuration +
                ", currentChargeQuantity=" + currentChargeQuantity +
                ", serial=" + serial +
                ", orderNo=" + orderNo +
                '}';
    }

    public static void main(String[] args) {
        byte[] b = new byte[]{0x01};
        System.out.println(BytesUtil.bcd2StrLittle(b));
        BytesUtil.bcd2StrLittle(b);
        byte[] bytes = new byte[]{(byte) 0x68, 0x26, (byte) 0x9a, (byte) 0xd0, 0x0, 0x16, (byte) 0x86, 0x0, 0x3, 0x0, 0x57, 0x0, 0x1, 0x9, 0x9, 0x0, 0x0, 0x0, 0x1, (byte) 0xfe, 0x18, 0x0, 0x0, 0x55, 0x0, 0x0, (byte) 0x80, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3, 0x0, 0xa, 0xa, 0x3d, 0x5, 0x0, 0x0};
        byte[] dataBytes = BytesUtil.copyBytes(bytes, 13, (bytes.length - 13));

        //依照报文体规则解析报文
        XunDaoUploadChargeMonitorRequest uploadChargeMonitorRequest = XunDaoUploadChargeMonitorRequest.packEntity(dataBytes);
    }
}
