package com.piles.common.entity.type;

/**
 * 告警码
 */
public enum EWarnCode {

    TOUCH_SCREEN_FAULT_CODE(1001,"触摸屏故障"),
    CARD_READER_FAULT_CODE(1002,"读卡器故障"),
    PRINTER_FAULT_CODE(1003,"打印机故障"),
    AMMETER_FAULT_CODE(1004,"电表故障"),
    AMMETER_TIME_FAULT_CODE(1005,"电表对时故障"),
    AMMETER_COMMUTE_FAULT_CODE(1006,"电表通讯故障"),
    REPAIR_DOOR_FAULT_CODE(1007,"维修门异常打开"),
    SOCKET_DOOR_FAULT_CODE(1008,"插座门故障"),
    SPD_FAULT_CODE(1009,"防雷器故障"),
    AIR_FAN_FAULT_CODE(1010,"风扇故障"),
    TEMPERATURE_SENSOR_FAULT_CODE(1011,"温度传感器故障"),
    CHARGE_GUN_FAULT_CODE(1012,"充电枪故障"),
    FALL_SHAKE_CODE(1013,"桩体倾倒或严重振动"),
    CONNECT_CONFIRM_FAULT_CODE(1014,"连接确认异常"),
    RELAY_FAULT_CODE(1015,"输出继电器异常"),
    CONTROL_PANEL_FAULT_CODE(1016,"控制板异常"),
    PICK_PANEL_FAULT_CODE(1017,"采集板异常"),
    MONITOR_UNIT_FAULT_CODE(1018,"充电桩监控器异常"),
    FUSE_UNIT_FAULT_CODE(1019,"熔断器故障"),
    POWER_TEMPLATE_FAULT_CODE(1020,"充电桩电源模块异常"),
    DIRECT_CONTACTOR_FAULT_CODE(1021,"直流接触器异常"),
    EXCHANGE_CONTACTOR_FAULT_CODE(1022,"交流接触器异常"),
    BATTERY_LOCK_FAULT_CODE(1023,"电池锁异常"),
    LOW_POWER_FAULT(1024,"低压辅源异常"),
    CONCENTRATOR_FAULT_CODE(1025,"集中器异常"),
    DEV_INSULATION_FAULT_CODE(1026,"设备绝缘异常"),
    DEV_POWER_OFF_CODE(2001,"设备停电"),
    DEV_POWER_LOW_CODE(2002,"备用电池电量低"),
    SUDDEN_SWITCH_TRIGGER_CODE(2003,"急停开关触发"),
    EXCHANGE_TRIP_CODE(2004,"交流跳闸"),
    SHORT_PROTECT_TIGGER_CODE(2005,"短路保护触发"),
    LEAKANCE_PROTECT_TRIGGER_CODE(2006,"漏电保护触发"),
    TEMPERATURE_FAULT_CODE(2007,"温度异常"),
    EXCHANGE_IMPORT_FAULT_CODE(3101,"交流输入异常"),
    EXCHANGE_IMPORT_LOSS_CODE(3102,"交流输入缺相"),
    EXCHANGE_VOLTAGE_FAULT_CODE(3103,"交流电压异常"),
    EXCHANGE_IMPORT_OVER_VOLTAGE_CODE(3104,"交流输入过压"),
    EXCHANGE_IMPORT_UNDER_VOLTAGE_CODE(3105,"交流输入欠压"),
    EXCHANGE_IMPORT_OVER_FREQUENCY_CODE(3106,"交流输入频率过频"),
    EXCHANGE_IMPORT_UNDER_FREQUENCY_CODE(3107,"交流输入频率欠频"),
    EXCHANGE_IMPORT_OVER_LOAD_CODE(3108,"交流电流过负荷"),
    A_OVER_LOAD_CODE(3171,"A相电流过负荷"),
    B_OVER_LOAD_CODE(3172,"B相电流过负荷"),
    C_OVER_LOAD_CODE(3173,"C相电流过负荷"),
    A_LOSS_PHASE_CODE(3174,"A相断相"),
    B_LOSS_PHASE_CODE(3175,"B相断相"),
    C_LOSS_PHASE_CODE(3176,"C相断相"),
    A_OVER_VOLTAGE_CODE(3177,"A相电压过压"),
    B_OVER_VOLTAGE_CODE(3178,"B相电压过压"),
    C_OVER_VOLTAGE_CODE(3179,"C相电压过压"),
    A_UNDER_VOLTAGE_CODE(3180,"A相电压欠压"),
    B_UNDER_VOLTAGE_CODE(3181,"B相电压欠压"),
    C_UNDER_VOLTAGE_CODE(3182,"C相电压欠压"),
    DIRECT_EXPORT_FAULT_CODE(3201,"直流输出异常"),
    DIRECT_EXPORT_OVER_VOLTAGE_CODE(3202,"直流输出过压"),
    DIRECT_EXPORT_UNDER_VOLTAGE_CODE(3203,"直流输出欠压"),
    DIRECT_EXPORT_OVER_FLOW_CODE(3204,"直流输出电流过流"),
    DIRECT_EXPORT_INVERSE_CODE(3205,"直流输出反接"),
    DIRECT_EXPORT_OFF_CODE(3206,"直流输出断路"),
    DIRECT_BUSS_IMPORT_OVER_VOLTAGE_CODE(3207,"直流母线输入过压"),
    DIRECT_BUSS_IMPORT_UNDER_VOLTAGE_CODE(3208,"直流母线输入欠压"),
    CAR_BMS_COMMUTE_FAULT_CODE(4001,"车辆BMS通信异常"),
    CAR_BMS_REVERSE_CODE(4002,"车辆BMS反接"),
    CAR_BMS_STOP_CODE(4003,"车辆BMS终止"),
    CAR_BATTERY_OVER_FLOW_CODE(4004,"车辆蓄电池充电过流"),
    CAR_BATTERY_TEMPLATE_HIGH_CODE(4005,"车辆蓄电池模块过温"),
    CAR_BATTERY_CONNECT_FAULT_CODE(4006,"车辆蓄电池连接异常"),
    CAR_BATTERY_VOLTAGE_COLLECT_FAULT_CODE(4007,"车辆蓄电池电压采集异常"),
    CAR_BATTERY_OVER_VOLTAGE_CODE(4008,"车辆蓄电池电压过压"),
    DW_COMMUT_FAULT_CODE(5002,"迪文通信异常"),
    OTHER_FAULT_CODE(9999,"其他故障");


    private EWarnCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static EWarnCode fromCode(int code) {
        for (EWarnCode item : EWarnCode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
