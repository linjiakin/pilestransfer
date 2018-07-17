package com.piles.record.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 上传充电过程监测数据接口请求实体
 */
@Data
public class UploadChargeMonitorRequest implements Serializable {

    /**
     * 桩编号 8位 BCD
     */
    private String pileNo;

    //枪号	BIN	1	1: A枪2: B枪
    private int gunNo;
    //订单号	BIN	8
    private long orderNo;
    //BMS版本号	BIN	3	按国标原样传输
    private int bmsVersion;
    //BMS类型	BIN	1	 0:其他电池 1:铅酸电池 2:镍氢电池 3:磷酸铁锂电池 4:锰酸锂电池 5:钴酸锂电池 6:三原材料电池 7:聚合物锂电池 8:钛酸锂电池
    private int bmsType;
    //蓄电池标称总能量	BIN	4	单位：度，精确度为0.001
    private BigDecimal batteryNominalEnergy;
    //蓄电池额定容量	BIN	4	单位：Ah，精确度为0.001
    private BigDecimal batteryRatedEnergy;
    //蓄电池额定总电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal batteryRatedVoltage;
    //电池生产厂商	BIN	4
    private int batteryProducer;
    //电池生产日期	BCD	3	格式: YYMMDD
    private String batteryProduceTime;
    //电池组充电次数	BIN	4
    private int batteryCycleCount;
    //最高允许充电电流	BIN	4	单位：A，精确度为0.001
    private BigDecimal highestAllowElectricity;
    //最高允许充电电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal highestAllowVoltage;
    //最高允许温度	BIN	4	单位：摄氏度，精确度为0.001
    private BigDecimal highestAllowTemperature;
    //单体允许最高电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal singleAllowHighestVoltage;
    //单体电池最高电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal singleHighestVoltage;
    //单体电池最低电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal singleLowestVoltage;
    //单体电池最高温度	BIN	4	单位：摄氏度，精确度为0.001
    private BigDecimal singleHighestTemperature;
    //单体电池最低温度	BIN	4	单位：摄氏度，精确度为0.001
    private BigDecimal singleLowestTemperature;
    //充电机温度	BIN	4	单位：摄氏度，精确度为0.001
    private BigDecimal chargerTemperature;
    //充电枪头温度	BIN	4	单位：摄氏度，精确度为0.001
    private BigDecimal chargerGunTemperature;
    //充电机输入电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal chargerImportVoltage;
    //充电机输入电流	BIN	4	单位：A，精确度为0.001
    private BigDecimal chargerImportElectricity;
    //充电机输入功率	BIN	4	单位：KW，精确度为0.001
    private BigDecimal chargerImportPower;
    //充电机输出电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal chargerExportVoltage;
    //充电机输出电流	BIN	4	单位：A，精确度为0.001
    private BigDecimal chargerExportElectricity;
    //充电机输出功率	BIN	4	单位：KW，精确度为0.001
    private BigDecimal chargerExportPower;
    //电压需求	BIN	4	单位：V，精确度为0.001
    private BigDecimal voltageRequire;
    //电流需求	BIN	4	单位：A，精确度为0.001
    private BigDecimal electricityRequire;
    //A相电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal axVoltage;
    //B相电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal bxVoltage;
    //C相电压	BIN	4	单位：V，精确度为0.001
    private BigDecimal cxVoltage;
    //A相电流	BIN	4	单位：A，精确度为0.001
    private BigDecimal axElectricity;
    //B相电流	BIN	4	单位：A，精确度为0.001
    private BigDecimal bxElectricity;
    //C相电流	BIN	4	单位：A，精确度为0.001
    private BigDecimal cxElectricity;


    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static UploadChargeMonitorRequest packEntity(byte[] msg) {
        UploadChargeMonitorRequest request = new UploadChargeMonitorRequest();
        int cursor = 0;
        request.setGunNo( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, cursor, 1 ), 10 ) ) );
        cursor += 1;
        request.setOrderNo( BytesUtil.byte2Long( BytesUtil.copyBytes( msg, cursor, 8 ) ) );
        cursor += 8;
        //TODO 按国际原样输出？？
        request.setBmsVersion( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, cursor, 3 ), 10 ) ) );
        cursor += 3;
        request.setBmsType( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, cursor, 1 ), 10 ) ) );
        cursor += 1;
        request.setBatteryNominalEnergy( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setBatteryRatedEnergy( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setBatteryRatedVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setBatteryProducer( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, cursor, 4 ), 10 ) ) );
        cursor += 4;
        request.setBatteryProduceTime( BytesUtil.bcd2Str( BytesUtil.copyBytes( msg, cursor, 3 ) ) );
        cursor += 3;
        request.setBatteryCycleCount( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, cursor, 4 ), 10 ) ) );
        cursor += 4;
        request.setHighestAllowElectricity( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setHighestAllowVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setHighestAllowTemperature( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setSingleAllowHighestVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setSingleHighestVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setSingleLowestVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setSingleHighestTemperature( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setSingleLowestTemperature( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerTemperature( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerGunTemperature( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerImportVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerImportElectricity( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerImportPower( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerExportVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerExportElectricity( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setChargerExportPower( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setVoltageRequire( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setElectricityRequire( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setAxVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setBxVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setCxVoltage( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setAxElectricity( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setBxElectricity( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );
        cursor += 4;
        request.setCxElectricity( BigDecimal.valueOf( BytesUtil.bytesToInt( BytesUtil.copyBytes( msg, cursor, 4 ), 0 ) ).divide( new BigDecimal( 1000 ), 3, BigDecimal.ROUND_HALF_UP ) );

        return request;
    }

    public static byte[] packBytes(UploadChargeMonitorRequest request) {
        byte[] responseBytes = new byte[]{};
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getGunNo(), 1 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.long2Byte( request.getOrderNo() ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBmsVersion(), 3 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBmsType(), 1 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBatteryNominalEnergy().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBatteryRatedEnergy().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBatteryRatedVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBatteryProducer(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.str2Bcd( request.getBatteryProduceTime() ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBatteryCycleCount(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getHighestAllowElectricity().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getHighestAllowVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getHighestAllowTemperature().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getSingleAllowHighestVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getSingleHighestVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getSingleLowestVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getSingleHighestTemperature().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getSingleLowestTemperature().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerTemperature().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerGunTemperature().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerImportVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerImportElectricity().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerImportPower().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerExportVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerExportElectricity().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getChargerExportPower().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getVoltageRequire().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getElectricityRequire().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getAxVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBxVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getCxVoltage().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getAxElectricity().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getBxElectricity().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        responseBytes = Bytes.concat( responseBytes, BytesUtil.intToBytes( request.getCxElectricity().multiply( BigDecimal.valueOf( 1000 ) ).intValue(), 4 ) );
        return responseBytes;
    }


    public static void main(String[] args) {
        UploadChargeMonitorRequest bean = new UploadChargeMonitorRequest();
        Class clazz = UploadChargeMonitorRequest.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();

            if (methodName.indexOf( "set" ) == 0) {
                System.out.println( "request." + methods[i].getName() + "(new BigDecimal(" + i + "));" );
            }
        }

    }

    @Override
    public String toString() {
        return "UploadChargeMonitorRequest{" +
                "gunNo=" + gunNo +
                ", orderNo=" + orderNo +
                ", bmsVersion=" + bmsVersion +
                ", bmsType=" + bmsType +
                ", batteryNominalEnergy=" + batteryNominalEnergy +
                ", batteryRatedEnergy=" + batteryRatedEnergy +
                ", batteryRatedVoltage=" + batteryRatedVoltage +
                ", batteryProducer=" + batteryProducer +
                ", batteryProduceTime='" + batteryProduceTime + '\'' +
                ", batteryCycleCount=" + batteryCycleCount +
                ", highestAllowElectricity=" + highestAllowElectricity +
                ", highestAllowVoltage=" + highestAllowVoltage +
                ", highestAllowTemperature=" + highestAllowTemperature +
                ", singleAllowHighestVoltage=" + singleAllowHighestVoltage +
                ", singleHighestVoltage=" + singleHighestVoltage +
                ", singleLowestVoltage=" + singleLowestVoltage +
                ", singleHighestTemperature=" + singleHighestTemperature +
                ", singleLowestTemperature=" + singleLowestTemperature +
                ", chargerTemperature=" + chargerTemperature +
                ", chargerGunTemperature=" + chargerGunTemperature +
                ", chargerImportVoltage=" + chargerImportVoltage +
                ", chargerImportElectricity=" + chargerImportElectricity +
                ", chargerImportPower=" + chargerImportPower +
                ", chargerExportVoltage=" + chargerExportVoltage +
                ", chargerExportElectricity=" + chargerExportElectricity +
                ", chargerExportPower=" + chargerExportPower +
                ", voltageRequire=" + voltageRequire +
                ", electricityRequire=" + electricityRequire +
                ", axVoltage=" + axVoltage +
                ", bxVoltage=" + bxVoltage +
                ", cxVoltage=" + cxVoltage +
                ", axElectricity=" + axElectricity +
                ", bxElectricity=" + bxElectricity +
                ", cxElectricity=" + cxElectricity +
                '}';
    }
}
