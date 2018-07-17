package com.piles.setting.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 校时 充电桩 运营管理系统
 */
@Data
public class VerifyTimeRequest extends BasePushResponse implements Serializable {
    /**
     * BIN	1	0: 设置成功 1: 设置失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static VerifyTimeRequest packEntity(byte[] msg) {
        VerifyTimeRequest request = new VerifyTimeRequest();
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 0, 1), 10)));
        return request;
    }

    public static void main(String[] args) {
        byte[] msg = new byte[]{0x01};
        packEntity(msg);

    }
}
