package com.piles.setting.entity;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 计费规则设置 充电桩  运营管理系统
 */
@Data
public class BillRuleSetRequest extends BasePushResponse implements Serializable
{
    /**
     * 计费规则ID 4位 BIN 首次登录填0，后续登录填桩当前正在使用的计费规则ID
     */
    private int billingRuleId;
    /**
     * 计费规则版本号 4位 BIN 首次登录填0，后续登录填桩当前正在使用的计费规则版本号
     */
    private int billingRuleVersion;
    /**
     * 结果	BIN	1	0: 设置成功  1: 设置失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static BillRuleSetRequest packEntity(byte[] msg){
        BillRuleSetRequest request=new BillRuleSetRequest();
        request.setBillingRuleId(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,0,4),10)));
        request.setBillingRuleVersion(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,4,4),10)));
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,8,1),10)));
        return request;
    }


    public static void main(String[] args) {

    }



}
