package com.piles.control.entity;

import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录接口请求实体
 */
@Data
public class LoginRequest implements Serializable
{


    /**
     * 桩编号 8位 BCD
     */
    private String pileNo;
    /**
     * 桩类型 1位 BIN
     */
    private int pileType;
    /**
     * 充电枪数量 1位 BIN
     */
    private int chargeGunCount;
    /**
     * 运营商编码 4位 默认 0 BIN
     */
    private String operatorCode;
    /**
     * 密码 3位 默认000000 BCD
     */
    private String password;
    /**
     * 桩软件版本号 2位 BIN 点号前后各占一个字节。如V1.0表示为0x01 0x00,V1.10表示为0x01 0x0A
     */
    private String pileSoftVersion;
    /**
     * 通信协议版本号 2位 BIN 点号前后各占一个字节。如V1.0表示为0x01 0x00,V1.10表示为0x01 0x0A
     */
    private String protocolVersion;
    /**
     * 计费规则ID 4位 BIN 首次登录填0，后续登录填桩当前正在使用的计费规则ID
     */
    private int billingRuleId;
    /**
     * 计费规则版本号 4位 BIN 首次登录填0，后续登录填桩当前正在使用的计费规则版本号
     */
    private int billingRuleVersion;
    /**
     * 厂商编码
     */
    private TradeType tradeType;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static LoginRequest packEntity(byte[] msg){
        LoginRequest request=new LoginRequest();

        request.setPileNo(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg,0,8)));
        request.setPileType(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,8,1),10)));
        request.setChargeGunCount(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,9,1),10)));
        request.setOperatorCode(BytesUtil.binary(BytesUtil.copyBytes(msg,10,4),10));
        request.setPassword(BytesUtil.bcd2Str(BytesUtil.copyBytes(msg,14,3)));
        request.setPileSoftVersion("V"+BytesUtil.binary(BytesUtil.copyBytes(msg,17,1),10)+"."+BytesUtil.binary(BytesUtil.copyBytes(msg,18,1),10));
        request.setProtocolVersion("V"+BytesUtil.binary(BytesUtil.copyBytes(msg,19,1),10)+"."+BytesUtil.binary(BytesUtil.copyBytes(msg,20,1),10));
        request.setBillingRuleId(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,21,4),10)));
        request.setBillingRuleVersion(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,25,4),10)));

        return request;
    }
    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static LoginRequest packEntityXundao(byte[] msg){
        //        起始域  长度 协议类型 连接类型   设备编号    站地址
        //         1字节  1字节 1字节  1字节   8字节BCD码   2字节
        LoginRequest request=new LoginRequest();
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg,4,8)));
        Integer pileType = Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg, 3, 1), 10));
        pileType = pileType == 2 ? 3 : pileType;
        request.setPileType(pileType);


        return request;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "pileNo='" + pileNo + '\'' +
                ", pileType=" + pileType +
                ", chargeGunCount=" + chargeGunCount +
                ", operatorCode='" + operatorCode + '\'' +
                ", password='" + password + '\'' +
                ", pileSoftVersion='" + pileSoftVersion + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", billingRuleId=" + billingRuleId +
                ", billingRuleVersion=" + billingRuleVersion +
                '}';
    }

    public static void main(String[] args) {
        byte[] msg= new byte[]{0x68,0x01,0x02,0x04,0x28,0x00,0x00,0x00,0x30,0x40,0x31,0x00,0x00};
        System.out.println(packEntityXundao(msg));

    }



}
