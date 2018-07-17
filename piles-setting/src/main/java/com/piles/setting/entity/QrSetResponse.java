package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 二维码设置
 */
@Data
public class QrSetResponse implements Serializable
{
    /**
     * 抢号 1 位  BIN 1: A枪 2: B枪
     */
    private int gunNo;
    /**
     * 二维码长度 2 位  BIN 可变
     */
    private int qrLen;
    /**
     * 二维码内容 ASCII 链接地址
     */
    private String qrContent;

    /**
     * 封装报文体
     * @param response
     * @return
     */
    public static byte[] packBytes(QrSetResponse response){


        int gunNo = response.getGunNo();
        int qrLen = response.getQrLen();
        String qrContent = response.getQrContent();
        byte[] gunNoBytes = BytesUtil.intToBytes(gunNo,1);
        byte[] qrLenBytes = BytesUtil.intToBytes(qrLen,2);
        byte[] qrContentbytes = qrContent.getBytes();
        return Bytes.concat(gunNoBytes,qrLenBytes,qrContentbytes);

    }


    public static void main(String[] args) {
        QrSetResponse qrSetResponse = new QrSetResponse();
        String qrContent = "sjdfsdjlfjasjfsdaljflsdjflasdkfjsldjfsadjf";
        qrSetResponse.setGunNo(1);
        qrSetResponse.setQrLen(500);
        qrSetResponse.setQrContent(qrContent);
        byte[] bytes = QrSetResponse.packBytes(qrSetResponse);
        System.out.println(bytes);
        System.out.println(BytesUtil.bytesToInt(BytesUtil.copyBytes(bytes,1,2),0));

    }



}
