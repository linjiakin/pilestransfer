package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 循道 FTP升级程序下行数据 后台--》充电桩
 */
@Data
public class XunDaoFtpUpgradeIssuePushRequest extends BasePushRequest implements Serializable
{

    /**
     * 终端机器编 码 BCD 码 8Byte --16位设备编码，离散充电桩 资产编号，由运营监控系统提供
     */
    private String pileNo;
    /**
     * 升级服务器IP地址 字符串， 不足尾部补0x00 20长度 字符串 比如:1.23.45.110
     */
    private String serverIp;
    /**
     * 升级服务器 端口 字符串， 不足尾部 补 0x00 5长度 字符串 比如:81236
     */
    private String serverPort;
    /**
     * 升级使用 用户名 字符串， 不足尾部 补 0x00 20长度 字符串 比如:admin
     */
    private String userName;
    /**
     * 升级使用密码 字符串， 不足尾部 补 0x00 20长度 字符串 比如:123456
     */
    private String password;
    /**
     * 软件版本号 字符串， 不足尾部 补 0x00 20长度 字符串 比如:XD01-V1.1234
     */
    private String softVersion;
    /**
     * 下载路径 字符串， 不足尾部 补 0x00 80长度 字符串 比如: /XDJ22-D/XD-V1.1234/XD001. bin
     */
    private String downloadUrl;

    /**
     * 封装报文体
     * @param request
     * @return
     */
    public static byte[] packBytes(XunDaoFtpUpgradeIssuePushRequest request){
        byte[] result = new byte[0];
        result = Bytes.concat(result,BytesUtil.str2BcdLittle(request.getPileNo()));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getServerIp(),20,(byte)0x00));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getServerPort(),5,(byte)0x00));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getUserName(),20,(byte)0x00));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getPassword(),20,(byte)0x00));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getSoftVersion(),20,(byte)0x00));
        result = Bytes.concat(result,BytesUtil.rightPadBytes(request.getDownloadUrl(),80,(byte)0x00));
        return result;
    }


}
