package com.piles.common.entity.type;

/**
 * 循道类型标识
 */
public enum XunDaoTypeCode {

    TOTAL_SUMMON_CODE(100, "总召唤命令"),
    LOCK_SYNC_CODE(103, "时钟同步命令"),
    EXPENSE_RECORD_CODE(130, "充电桩消费记录数据"),
    SEND_DATA_CODE(133, "下发数据项"),
    MONITOR_DATA_CODE(134, "监测数据项");

    private XunDaoTypeCode(int code, String value) {
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

    public static XunDaoTypeCode fromCode(int code) {
        for (XunDaoTypeCode item : XunDaoTypeCode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
