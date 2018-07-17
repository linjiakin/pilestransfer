package com.piles.setting.entity;


import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;

import java.io.Serializable;

public class RebootPushRequest extends BasePushRequest implements Serializable {
    /**
     * 封装报文体
     * @param request
     * @return
     */
    public static byte[] packBytes(RebootPushRequest request){
        byte[] responseBytes = new byte[]{};
        byte[] periodLenBytes = new byte[]{0x00};

        responseBytes = Bytes.concat(responseBytes,periodLenBytes);

        return responseBytes;
    }
}
