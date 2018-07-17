package com.piles.setting.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 循道ftp升级程序下行
 */
@Data
public class XunDaoFtpUpgradeIssueRequest extends BasePushResponse implements Serializable
{
    /**
     * 终端机器编 码 BCD 码 8Byte
     */
    private String pileNo;
    /**
     * 升级确认 1位 BIN    0:开始升级 1:异常，不执行升级
     */
    private int result;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static XunDaoFtpUpgradeIssueRequest packEntity(byte[] msg){
        XunDaoFtpUpgradeIssueRequest request=new XunDaoFtpUpgradeIssueRequest();
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg,0,8)));
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,8,1),10)));
        return request;
    }



}
