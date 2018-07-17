package com.piles.common.business;

/**
 * Created by lgc48027 on 2017/11/3.
 */
public interface IBusinessFactory {

    /**
     * 根据报文获取处理报文类
     * @param msg
     * @return
     */
    IBusiness getByMsg(byte[] msg);
}
