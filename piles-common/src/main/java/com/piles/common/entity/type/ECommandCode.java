package com.piles.common.entity.type;

/**
 * 命令码
 */
public enum ECommandCode {

    LOGIN_CODE(0x01, "登录"),
    LOGIN_ANSWER_CODE(0x81, "登录回复"),
    PRE_CHARGE_CODE(0x02, "预约充电"),
    PRE_CHARGE_ANSWER_CODE_CODE(0x82, "预约充电回复"),
    PRE_CHARGE_CANCEL_CODE(0x03, "预约充电取消"),
    PRE_CHARGE_CANCEL_ANSWER_CODE(0x83, "预约充电取消回复"),
    CHARGE_CODE(0x04, "充电桩请求启动充电（卡充电）"),
    CHARGE_ANSWER_CODE(0x84, "充电桩请求启动充电回复"),
    CHARGE_OVER_CODE(0x05, "充电桩请求结束充电（卡充电）"),
    CHARGE_OVER_ANSWER_CODE(0x85, "充电桩请求结束充电回复"),
    REMOTE_CHARGE_CODE(0x06, "远程启动充电"),
    REMOTE_CHARGE_ANSWER_CODE(0x86, "远程启动充电回复"),
    REMOTE_CHARGE_OVER_CODE(0x07, "远程结束充电"),
    REMOTE_CHARGE_OVER_ANSWER_CODE(0x87, "远程结束充电回复"),
    UPLOAD_RECORD_CODE(0x08, "上传充电记录"),
    UPLOAD_RECORD_ANSWER_CODE(0x88, "上传充电记录回复"),
    UPLOAD_CHARGE_RATE_CODE(0x09, "上传充电进度"),
    UPLOAD_CHARGE_MONITOR_CODE(0x0A, "上传充电过程监控数据"),
    BILL_RULE_SET_CODE(0x0B, "计费规则设置"),
    BILL_RULE_SET_ANSWER_CODE(0x8B, "计费规则设置回复"),
    HEART_BEAT_CODE(0x0C, "心跳"),
    HEART_BEAT_ANSWER_CODE(0x8C, "心跳回复"),
    WARN_CODE(0x0D, "告警"),
    WARN_ANSWER_CODE(0x8D, "告警回复"),
    VERIFY_TIME_CODE(0x0E, "校时"),
    VERIFY_TIME_ANSWER_CODE(0x8E, "校时回复"),
    WHITE_LIST_SET_CODE(0x1A, "白名单设置"),
    WHITE_LIST_SET_ANSWER_CODE(0x9A, "白名单设置回复"),
    BLACK_LIST_ANSWER_CODE(0x1B, "黑名单设置"),
    BLACK_LIST_SET_ANSWER_CODE(0x9B, "黑名单设置回复"),
    QR_SET_CODE(0x1C, "二维码设置"),
    QR_SET_ANSWER_CODE(0x9C, "二维码设置回复"),
    REBOOT_CODE(0x1D, "重启"),
    REBOOT_ANSWER_CODE(0x9D, "重启回复"),
    REMOTE_UPDATE_CODE(0x1E, "远程升级"),
    REMOTE_UPDATE_ANSWER_CODE(0x9E, "远程升级回复"),
    REQUEST_UPDATE_PACKAGE_CODE(0x1F, "请求数据包"),
    REQUEST_UPDATE_PACKAGE_ANSWER_CODE(0x9F, "请求数据包回复"),
    UPDATE_RESULT_REPORT_CODE(0x20, "升级结果汇报"),
    UPDATE_RESULT_REPORT_ANWSER_CODE(0xA0, "升级结果汇报回复");


    private ECommandCode(int code, String value) {
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

    public static ECommandCode fromCode(int code) {
        for (ECommandCode item : ECommandCode.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }
}
