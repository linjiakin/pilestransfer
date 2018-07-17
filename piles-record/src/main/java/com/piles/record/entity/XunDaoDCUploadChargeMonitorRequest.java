package com.piles.record.entity;

import com.piles.common.util.BytesUtil;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 循道上传充电过程监测数据接口请求实体
 */
@Data
@ToString
public class XunDaoDCUploadChargeMonitorRequest implements Serializable {

    //桩类型
    private int pileType;
    //枪号
    private int gunNo;


    //桩编号 8位 BCD
    private String pileNo;
    private int soc;//bin 1位 1%;
    //连接确认开关状态(跟交流不同) BIN 码 1Byte 变化上传;0:断开， 1:连接，2:故障状态
    private int switchStatus;
    //有功总电度 BIN 码 4Byte  精确到小数点后两位
    private BigDecimal activElectricalDegree;
    //充电输出电压(直 流最大输出电压)	BIN	2	精确到小数点后一位
    private BigDecimal dcAllowVoltage;
    //充电输出电流(直 流最大输出电流)	BIN	2	单位：A，精确到小数点后二位
    private BigDecimal dcAllowElectricity;
    //充电输出电压(直 流最大输出电压)	BIN	2	精确到小数点后一位
    private BigDecimal bmsAllowVoltage;
    //充电输出电流(直 流最大输出电流)	BIN	2	单位：A，精确到小数点后二位
    private BigDecimal bmsAllowElectricity;
    //充电模式 0恒压 1恒流 bin 2
    private int chargeType;
    //电池类型  bin 1 0x01-铅酸电池， 0x02-镍氢电池, 0x03-磷酸铁锂电池, 0x04-锰酸锂池, 0x05-钴酸锂电池, 0x06-三元次料电池, 0x07-聚合物锂离子 电池, 0x08-钛酸锂电池, 0xff-其他电池
    private int batteryType;
    //单体最高允许充电电压 bin 4Byte 精确一位
    private BigDecimal singleHighestAllowVoltage;
    //单体最高允许充电电流 bin 4Byte 精确两位
    private BigDecimal singleHighestAllowElectricity;
    //交流 A 相充电电压  BIN 码  2Byte  小端  精确一位
    private BigDecimal aCVoltageA;
    //交流 b 相充电电压  BIN 码  2Byte  小端  精确一位
    private BigDecimal aCVoltageB;
    //交流 c 相充电电压  BIN 码  2Byte  小端  精确一位
    private BigDecimal aCVoltageC;
    //交流 A 相充电电流  BIN 码  2Byte  小端  精确两位
    private BigDecimal aCElectricityA;
    //交流 B 相充电电流  BIN 码  2Byte  小端  精确两位
    private BigDecimal aCElectricityB;
    //交流 C 相充电电流  BIN 码  2Byte  小端  精确两位
    private BigDecimal aCElectricityC;
    // 工作状态(跟交流不同) BCD 码 1Byte 0x00-空闲 0x01-准备充电 0x02-充电中 0x03-充电结束 0x04-充电失败 0x05-系统故障
    private String workStatus;
    //故障状态(跟交流不一样) Bin 码 4Byte
    private int troubleStatus;
    //剩余充电时间 BIN 2 字节 分
    private int unDochargeDuration;
    //充电时长 BIN 2 字节 分
    private int chargeDuration;
    //本次充电电量 BIN 4 字节
    private BigDecimal currentChargeQuantity;
    //交易流水号	BCD 码16Byte
    private String serial;
    //订单号 ascii 32位小端
    private String orderNo;

    //充电桩最高允许充电电源	BIN	4
    private BigDecimal highestAllowVoltage;
    //充电桩最高功率	BIN	4	单位：A，
    private int highestAllowW;

//    private byte[] temp;//预留bin 4

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static XunDaoDCUploadChargeMonitorRequest packEntity(byte[] msg) {
        XunDaoDCUploadChargeMonitorRequest request = new XunDaoDCUploadChargeMonitorRequest();
        int cursor = 0;
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 8)));
        cursor += 8;
        request.setSoc(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setSwitchStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setActivElectricalDegree(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setDcAllowVoltage(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setDcAllowElectricity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBmsAllowVoltage(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBmsAllowElectricity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setChargeType(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2)));
        cursor += 2;
        request.setBatteryType(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;

        request.setSingleHighestAllowVoltage(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setSingleHighestAllowElectricity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setACVoltageA(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setACVoltageB(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setACVoltageC(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setACElectricityA(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setACElectricityB(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setACElectricityC(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;

        //备用字段，直接错过去
        cursor += 2;

        request.setWorkStatus(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setTroubleStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setUnDochargeDuration(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2)));
        cursor += 2;
        request.setChargeDuration(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2)));
        cursor += 2;
        request.setCurrentChargeQuantity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
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
        cursor += 32;

        request.setHighestAllowVoltage(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setHighestAllowW(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;

//        request.setTemp(BytesUtil.copyBytes(msg, cursor, 4));

        return request;
    }
}
