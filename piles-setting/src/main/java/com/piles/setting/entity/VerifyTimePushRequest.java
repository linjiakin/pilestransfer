package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 远程结束充电 运营管理系统  充电桩
 */
@Data
public class VerifyTimePushRequest extends BasePushRequest implements Serializable
{
    /**
     * 服务器时间	BCD	6	格式: YYMMDDHHMMSS
     */
    private String serverTime;

    /**
     * 封装报文体
     * @param request
     * @return
     */
    public static byte[] packBytes(VerifyTimePushRequest request){
        String serverTime = request.getServerTime();
        byte[] serverTimebytes = BytesUtil.str2Bcd(serverTime);
        return serverTimebytes;
    }


    public static void main(String[] args) {
        VerifyTimePushRequest request = new VerifyTimePushRequest();
        request.setServerTime("171106123245");
        byte[] bytes = VerifyTimePushRequest.packBytes(request);
        System.out.println(BytesUtil.bcd2Str(bytes));

    }


}
