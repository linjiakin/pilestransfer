package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 登录接口请求实体
 */
@Data
public class BillRulePeriod implements Serializable {
    /**
     * 开始时间 2位 BCD 22:30
     */
    private String startTime;
    /**
     * 结束时间 2位 BCD 22:30
     */
    private String endTime;
    /**
     * 时段类型 1位 BIN 1: 尖时  2: 峰时 3: 平时  4: 谷时
     */
    private int periodType;

    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytes(BillRulePeriod request) {
        byte[] responseBytes = new byte[]{};
        responseBytes = Bytes.concat(responseBytes,BytesUtil.str2Bcd(request.getStartTime()));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.str2Bcd(request.getEndTime()));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(request.getPeriodType(),1));
        return responseBytes;

    }

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static BillRulePeriod packEntity(byte[] msg) {
        BillRulePeriod request = new BillRulePeriod();
        request.setStartTime(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg, 0, 1)) + ":" + BytesUtil.bcd2Str(BytesUtil.copyBytes(msg, 1, 1)));
        request.setEndTime(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg, 2, 1)) + ":" + BytesUtil.bcd2Str(BytesUtil.copyBytes(msg, 3, 1)));
        request.setPeriodType(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 4, 1), 10)));
        return request;
    }


    public static void main(String[] args) {
        byte[] msg = new byte[]{0x10, 0x02, 0x03, 0x12, 0x03};
        BillRulePeriod billRulePeriod = packEntity(msg);
        System.out.println(billRulePeriod);

    }


}
