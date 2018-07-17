package com.piles.record.entity;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 循道上传充电记录接口请求实体
 */
@Data
public class XunDaoDCUploadRecordRequest implements Serializable {

    /**
     * 桩编号 8位 BCD
     */
    private String pileNo;
    private int chargeIndex;//充电记录索引 bin 2
    //交易流水号	ascii 码16Byte
    private String serial;
    //物理卡号 BIN 码 8Byte
    private long physicCardNo;
    //用户卡号 BCD 码 8Byte
    private String userCardNo;
    //开始时间 BIN 码 7Byte 小端 CP56Time2a 格式
    private Date startTime;
    //结束时间 BIN 码 7Byte 小端 CP56Time2a 格式
    private Date endTime;
    //每半小时电量 BIN 码 2Byte 精确到小数点后两位 0:00-0:30 电量----23:30-24:00 电量
    private List<BigDecimal> everyHalfHourDegress;
    //总电量 BIN 码 4Byte 小端 精确到小数点后两位
    private BigDecimal totalAmmeterDegree;
    private String vin;//bin 17


    // 卡内余额 BIN 码 4Byte 精确到小数点后两位
    private BigDecimal unUseAmount;

    //开始 SOC BIN 码 1Byte 精确到 1%，1 表示 SOC=1% 类 推
    private int beginSoc;
    //结束 SOC BIN 码 1Byte 精确到 1%，1 表示 SOC=1% 类 推
    private int endSoc;
    // 停止充电原因 BIN 码 1Byte 0x01-余额不足 0x02-定费用到 0x03-定电量到 0x04-定时间到 0x05-汽车主动停止
    // 0x06-人工正常停止 0x07-输出失败 0x08-系统故障 0x09-未结账 0x0A-CP 异常 0x0B-意外断电
    private int stopChargeReason;
    //订单号 ascii 32位小端
    private String orderNo;
    //备用 BIN 码 4Byte 全部置 0
    private int reserved1;
    //备用 BIN 码 4Byte 全部置 0
    private int reserved2;
    //备用 BIN 码 4Byte 全部置 0
    private int reserved3;
    //备用 BIN 码 4Byte 全部置 0
    private int reserved4;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static XunDaoDCUploadRecordRequest packEntity(byte[] msg) {
        XunDaoDCUploadRecordRequest request = new XunDaoDCUploadRecordRequest();
        int cursor = 0;
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 8)));
        cursor += 8;
        request.setChargeIndex(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2)));
        cursor += 2;
        byte[] serials = BytesUtil.copyBytes(msg, cursor, 16);
        int i = 0;
        while (serials[i] != 0x00) {
            i++;
        }
        request.setSerial(new String(BytesUtil.copyBytes(serials, 0, i)));
        cursor += 16;
        request.setPhysicCardNo(BytesUtil.byte2LongLittle(BytesUtil.copyBytes(msg, cursor, 8)));
        cursor += 8;
        request.setUserCardNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 8)));
        cursor += 8;
        request.setStartTime(BytesUtil.byteCp2Date(BytesUtil.copyBytes(msg, cursor, 7)));
        cursor += 7;
        request.setEndTime(BytesUtil.byteCp2Date(BytesUtil.copyBytes(msg, cursor, 7)));
        cursor += 7;
        //处理每半个小时 共24小时 48个
        List<BigDecimal> list = Lists.newArrayList();
        for (i = 0; i < 48; i++) {
            list.add(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            cursor += 2;
        }
        request.setEveryHalfHourDegress(list);
        request.setTotalAmmeterDegree(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setVin(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, cursor, 17)));
        cursor += 17;
        request.setUnUseAmount(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 4;
        request.setBeginSoc(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setEndSoc(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setStopChargeReason(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
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
        request.setReserved1(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setReserved2(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setReserved3(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setReserved4(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        return request;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
