package com.piles.control.entity;

import com.alibaba.fastjson.JSON;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.MsgHelper;
import lombok.Data;

import java.io.Serializable;

/**
 * 远程结束充电
 */
@Data
public class RemoteCloseRequest extends BasePushResponse implements Serializable {

    /**
     * 订单号 8位 BIN
     */
    private long orderNo;
    /**
     * 结果 1位 BIN    0: 结束成功 1: 枪被预约 2: 其他原因失败
     */
    private int result;
    /**
     * 枪号 0 a枪 1 b枪
     */
    private int gunNo;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static RemoteCloseRequest packEntity(byte[] msg) {
        RemoteCloseRequest request = new RemoteCloseRequest();
        request.setOrderNo( BytesUtil.byte2Long( BytesUtil.copyBytes( msg, 0, 8 ) ) );
        request.setResult( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, 8, 1 ), 10 ) ) );
        return request;
    }
    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static RemoteCloseRequest packEntityXunDao(byte[] msg) {
        RemoteCloseRequest request = new RemoteCloseRequest();
        request.setResult( Integer.parseInt( BytesUtil.binary( BytesUtil.copyBytes( msg, 22, 1 ), 10 ) ) );
        request.setGunNo(MsgHelper.getGunNo(msg));
        return request;
    }

    public static void main(String[] args) {
        byte[] msg= new byte[]{ 0x68,0x15,0x4,(byte)0xd4,0x4,(byte)0xd4,(byte)0x82,0x0,0x3,0x0,(byte)0x88,0x0,0x1c,0x0,0x6,0x0,(byte)0x80,0x0,0x0,0x0,0x0,0x1,0x1};
//        packEntity(msg);
        System.out.println( JSON.toJSONString( packEntityXunDao( msg ) ));

    }

}
