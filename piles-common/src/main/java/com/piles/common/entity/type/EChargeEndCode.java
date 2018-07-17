package com.piles.common.entity.type;

/**
 * 充电结束原因
 */
public enum EChargeEndCode {

    OTHER_REASON_CODE(0x00, "其他原因"),
    FULL_END_CODE(0x01, "充满停止"),
    MONEY_END_CODE(0x02, "金额截止"),
    TIME_END_CODE(0x03, "时间截止"),
    ELECTRIC_END_CODE(0x04, "电量截止"),
    MONEY_LACK_CODE(0x05, "余额不足"),
    REMOTE_STOP_CODE(0x06, "远程停止"),
    SLOT_CARD_END_CODE(0x07, "刷卡停止"),
    DRAW_GUN_END_CODE(0x08, "拔枪停止"),
    SUDDEN_STOP_CODE(0x09, "急停"),
    DEV_FAULT_CODE(0x0A, "设备故障");

    private EChargeEndCode(int code, String value) {
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

    public static EChargeEndCode fromCode(int code) {
        for (EChargeEndCode item : EChargeEndCode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
