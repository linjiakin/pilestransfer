package com.piles.entity.vo;

import lombok.Data;

/**
 * 远程升级参数
 *
 * @author lizhi.zhang
 * @create 2018-02-26 下午10:32
 **/
@Data
public class XunDaoUpdateRemoteRequest {

    /**
     * 对应厂商类型  1:蔚景 2: 循道
     */
    private int tradeTypeCode;
    /**
     * 桩号 复数用 , 分割
     */
    private String pileNos;

    /**
     * 默认必填流水号
     */
    private String serial;
    /**
     * 软件版本号
     */
    private String softVersion;
    /**
     * 文件路径
     */
    private String filePath;
//
//    /**
//     * 升级服务器IP地址 字符串， 不足尾部补0x00 20长度 字符串 比如:1.23.45.110
//     */
//    private String serverIp;
//    /**
//     * 升级服务器 端口 字符串， 不足尾部 补 0x00 5长度 字符串 比如:81236
//     */
//    private String serverPort;
//    /**
//     * 升级使用 用户名 字符串， 不足尾部 补 0x00 20长度 字符串 比如:admin
//     */
//    private String userName;
//    /**
//     * 升级使用密码 字符串， 不足尾部 补 0x00 20长度 字符串 比如:123456
//     */
//    private String password;
    /**
     * 下载路径 字符串， 不足尾部 补 0x00 80长度 字符串 比如: /XDJ22-D/XD-V1.1234/XD001. bin
     */
//    private String downloadUrl;
}
