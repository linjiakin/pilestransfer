package com.piles.setting.entity;


import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class RemoteUpdateRequest extends BasePushResponse implements Serializable {
    /**
     * 结果 1位
     * 0：接受升级，即将启动请求数据(0x1F)，或启动http下载文件 
     * 1:正在升级中(应对可能出现服务器重复下发升级命令的情况)
     * 2：拒绝升级，桩不支持或者其他原因不能升级
     */
    private int result;

    /**
     * 升级方式 1位
     * 1：http下载，桩自己直接文件下载地址下载
     * 2：报文下载，桩根据报文0x1F分段下载数据包
     * 回复结果为0时有效
     */
    private int updateType;
    /**
     * 升级包分段下载单次最大长度限制 2位
     * 升级方式选择了2时有效(选择1填0)，限定0x9F报文里实际数据字段的最大长度，即每次最多从服务端获取多少个字节的升级数据
     */
    private int downMaxLenLimit;

    /**
     * 解析报文并封装request体
     * @param msg
     * @returnD
     */
    public static RemoteUpdateRequest packEntity(byte[] msg){
        RemoteUpdateRequest request=new RemoteUpdateRequest();
        request.setResult(Integer.parseInt( BytesUtil.binary(BytesUtil.copyBytes(msg, 0, 1), 10)));
        request.setUpdateType(Integer.parseInt( BytesUtil.binary(BytesUtil.copyBytes(msg, 1, 1), 10)));
        request.setDownMaxLenLimit(Integer.parseInt( BytesUtil.binary(BytesUtil.copyBytes(msg, 2, 2), 10)));
        return request;
    }

    public static void main(String[] args) {
        RemoteUpdateRequest remoteUpdateRequest = new RemoteUpdateRequest();
        remoteUpdateRequest.setResult(1);
        remoteUpdateRequest.setUpdateType(1);
        remoteUpdateRequest.setDownMaxLenLimit(0);
        byte[] responseBytes = new byte[]{};
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(remoteUpdateRequest.getResult(),1));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(remoteUpdateRequest.getUpdateType(),1));
        responseBytes = Bytes.concat(responseBytes,BytesUtil.intToBytes(remoteUpdateRequest.getDownMaxLenLimit(),2));

        RemoteUpdateRequest request = packEntity(responseBytes);
        System.out.println(request);

    }

}
