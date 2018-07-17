package com.piles.entity.vo;

import lombok.Data;

/**
 * 远程升级参数
 *
 * @author lizhi.zhang
 * @create 2018-02-26 下午8:38
 **/
@Data
public class UpdateRemoteRequest {

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
     * 通信协议版本号
     */
    private String protocolVersion;

    //升级文件MD5	ASCII	32	升级文件的MD5，用于校验文件是否完整
//    private String md5;
    //升级文件下载地址长度	BIN	2	可变
//    private int urlLen;
    //升级文件下载地址	ASCII	N	http链接地址
//    private String url;
}
