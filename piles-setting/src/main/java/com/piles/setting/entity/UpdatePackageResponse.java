package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 请求升级包数据回复
 */
@Data
public class UpdatePackageResponse implements Serializable
{
    /**
     * 结果 1 位
     * 0：正常
     * 1，	找不到文件，本地文件已被删除或损坏，收到此结果，桩不再继续下载(停止请求0x1F)
     * 2，	索引值不合法(比如大于总索引，负数等)
     */
    private int result;
    /**
     * 当前索引 2 位  BIN
     */
    private int currentIndex;
    /**
     * 总索引 2 位  BIN
     */
    private int totalIndex;
    /**
     * 当前段数据包长度 2 位  BIN
     * 最大长度为0x9E返回的最大允许长度，最后一段不确定按实际长度
     */
    private int currentSegmentLen;
    /**
     * 实际数据
     */
    private byte[] actualContent;

    /**
     * 封装报文体
     * @param response
     * @return
     */
    public static byte[] packBytes(UpdatePackageResponse response){
        byte[] responseBytes = new byte[]{};
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(response.getResult(),1));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(response.getCurrentIndex(),2));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(response.getTotalIndex(),2));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(response.getCurrentSegmentLen(),2));
        if(response.getActualContent()!=null) {
            responseBytes = Bytes.concat(responseBytes, response.getActualContent());
        }
        return responseBytes;

    }


    public static void main(String[] args) {
        UpdatePackageResponse response = new UpdatePackageResponse();
        response.setResult(2);
        UpdatePackageResponse.packBytes(response);
    }

    @Override
    public String toString() {
        return "UpdatePackageResponse{" +
                "result=" + result +
                ", currentIndex=" + currentIndex +
                ", totalIndex=" + totalIndex +
                ", currentSegmentLen=" + currentSegmentLen +
                ", actualContent=" + Arrays.toString(actualContent) +
                '}';
    }
}
