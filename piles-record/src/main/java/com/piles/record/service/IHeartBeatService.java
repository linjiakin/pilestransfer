package com.piles.record.service;


import com.piles.record.entity.HeartBeatRequest;

import java.util.Date;

/**
 * 心跳检测接口
 */
public interface IHeartBeatService {
    /**
     * 心跳检测
     *
     * @param heartBeatRequest 请求体
     * @return 成功返回Date类型
     */
    Date heartBeat(HeartBeatRequest heartBeatRequest);
}
