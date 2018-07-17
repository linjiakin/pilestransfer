package com.piles.record.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 上传充电过程监测数据接口实体
 */
@Data
public class UploadChargeMonitor implements Serializable {
    /**
     * 厂商code 1 蔚景 2 循道
     */
    private int tradeTypeCode;
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
