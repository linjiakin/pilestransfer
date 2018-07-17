package com.piles.common.entity;


import com.piles.common.entity.type.EPushResponseCode;
import lombok.Data;

import java.util.concurrent.CountDownLatch;

@Data
public class BasePushCallBackResponse<C extends BasePushResponse> {
    /**
     * 返回操作码
     */
    private EPushResponseCode code=EPushResponseCode.TIME_OUT;

    /**
     * 流水号
     */
    private String serial;

    /**
     * 默认两步 第一步发送成功  第二部是处理成功
     */
    private CountDownLatch countDownLatch=new CountDownLatch( 2 );
    /**
     *读取到的消息
     */
    private C obj;
    /**
     * 桩号 可为空
     */
    private String pileNo;
}
