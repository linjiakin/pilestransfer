package com.piles.setting.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 重启 充电桩  运营管理系统
 */
@Data
public class RebootRequest extends BasePushResponse implements Serializable
{

    /**
     * 桩类型 1位 BIN 0: 准备重启 1: 重启失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static RebootRequest packEntity(byte[] msg){
        RebootRequest request=new RebootRequest();
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 0, 1), 10)));
        return request;
    }


    public static void main(String[] args) {
        byte[] msg= new byte[]{0x00};
        RebootRequest request = packEntity(msg);
        System.out.println(request.getResult());
    }



}
