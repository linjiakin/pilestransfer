package com.piles.record.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 上传充电进度接口请求实体
 */
@Data
public class UploadChargeRateRequest implements Serializable {

    /**
     * 桩编号 8位 BCD
     */
    private String pileNo;

    //枪号	BIN	1	1: A枪 2: B枪
    private int gunNo;
    //订单号	BIN	8
    private long orderNo;
    //充电方式	BIN	1	1: 刷卡充电 2: APP充电
    private int chargeModel;
    //卡号	BCD	8	非卡充电全部置0
    private String cardNo;
    //车辆识别码(VIN)	ASCII	17
    private String vin;
    //SOC	BIN	1	数据范围0~100
    private int soc;
    //开始时间	BCD	6	格式: YYMMDDHHMMSS
    private String startTime;
    //结束时间	BCD	6	格式: YYMMDDHHMMSS
    private String endTime;
    //当前充电总电量	BIN	4	单位: 度，精确度为0.001
    private BigDecimal totalAmmeterDegree;
    //尖时电量	BIN	4	单位: 度，精确度为0.001
    private BigDecimal pointElectricQuantity;
    //峰时电量	BIN	4	单位: 度，精确度为0.001
    private BigDecimal peakElectricQuantity;
    //平时电量	BIN	4	单位: 度，精确度为0.001
    private BigDecimal ordinaryElectricQuantity;
    //谷时电量	BIN	4	单位: 度，精确度为0.001
    private BigDecimal dipElectricQuantity;
    //当前充电费总金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal totalElectricAmount;
    //尖时电费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal pointElectricAmount;
    //峰时电费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal peakElectricAmount;
    //平时电费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal ordinaryElectricAmount;
    //谷时电费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal dipElectricAmount;
    //预约费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal subscriptionAmount;
    //服务费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal serviceAmount;
    //停车费金额	BIN	4	单位: 元，精确度为0.001
    private BigDecimal parkingAmount;
    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static UploadChargeRateRequest packEntity(byte[] msg) {
        UploadChargeRateRequest request = new UploadChargeRateRequest();
        int cursor = 0;
        request.setGunNo(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,cursor,1),10)));
        cursor+=1;
        request.setOrderNo(BytesUtil.byte2Long(BytesUtil.copyBytes(msg,cursor,8)));
        cursor+=8;
        request.setChargeModel(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,cursor,1),10)));
        cursor+=1;
        request.setCardNo(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg,cursor,8)));
        cursor+=8;
        //TODO String 类型直接用new String
        request.setVin(new String(BytesUtil.copyBytes(msg,cursor,17)));
        cursor+=17;
        request.setSoc(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,cursor,1),10)));
        cursor+=1;
        request.setStartTime(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg,cursor,6)));
        cursor+=6;
        request.setEndTime(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg,cursor,6)));
        cursor+=6;
        //TODO 是否需要除以1000
        request.setTotalAmmeterDegree(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setPointElectricQuantity(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setPeakElectricQuantity(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setOrdinaryElectricQuantity(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setDipElectricQuantity(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setTotalElectricAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setPointElectricAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setPeakElectricAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setOrdinaryElectricAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setDipElectricAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setSubscriptionAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setServiceAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        cursor+=4;
        request.setParkingAmount(BigDecimal.valueOf(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, cursor, 4), 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));
        return request;
    }

    public static byte[] packBytes(UploadChargeRateRequest request) {
        byte[] responseBytes = new byte[]{};
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getGunNo(),1));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.long2Byte(request.getOrderNo()));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getChargeModel(),1));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.str2Bcd(request.getCardNo()));
        responseBytes = Bytes.concat(responseBytes,request.getVin().getBytes());
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getSoc(),1));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.str2Bcd(request.getStartTime()));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.str2Bcd(request.getEndTime()));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getTotalAmmeterDegree().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getPointElectricQuantity().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getPeakElectricQuantity().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getOrdinaryElectricQuantity().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getDipElectricQuantity().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getTotalElectricAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getPointElectricAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getPeakElectricAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getOrdinaryElectricAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getDipElectricAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getSubscriptionAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getServiceAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getParkingAmount().multiply(BigDecimal.valueOf(1000)).intValue(),4));
        return responseBytes;
    }

    @Override
    public String toString() {
        return "UploadChargeRateRequest{" +
                "gunNo=" + gunNo +
                ", orderNo=" + orderNo +
                ", chargeModel=" + chargeModel +
                ", cardNo='" + cardNo + '\'' +
                ", vin='" + vin + '\'' +
                ", soc=" + soc +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", totalAmmeterDegree=" + totalAmmeterDegree +
                ", pointElectricQuantity=" + pointElectricQuantity +
                ", peakElectricQuantity=" + peakElectricQuantity +
                ", ordinaryElectricQuantity=" + ordinaryElectricQuantity +
                ", dipElectricQuantity=" + dipElectricQuantity +
                ", totalElectricAmount=" + totalElectricAmount +
                ", pointElectricAmount=" + pointElectricAmount +
                ", peakElectricAmount=" + peakElectricAmount +
                ", ordinaryElectricAmount=" + ordinaryElectricAmount +
                ", dipElectricAmount=" + dipElectricAmount +
                ", subscriptionAmount=" + subscriptionAmount +
                ", serviceAmount=" + serviceAmount +
                ", parkingAmount=" + parkingAmount +
                '}';
    }

    public static void main(String[] args) {
        UploadChargeRateRequest bean = new UploadChargeRateRequest();
        Class clazz = UploadChargeRateRequest.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();

            if (methodName.indexOf("set") == 0) {
                System.out.println("request."+methods[i].getName()+"(new BigDecimal("+i+"));");
            }
        }

    }


}
