package com.piles.common.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.business.IBusinessFactory;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 获取解析报文业务对象
 */
@Slf4j
@Component("businessFactory")
public class BusinessFactory implements IBusinessFactory {

    @Override
    public IBusiness getByMsg(byte[] msg) {
        byte order = msg[1];
            ECommandCode commandCode = ECommandCode.fromCode(order);
            if(commandCode!=null){
                log.info("接收到报文，报文命令码:{},{}",commandCode.getCode(),commandCode.getValue());
            }
            switch (order){
                //control
                case 0x01:
                    //登陆
                    return SpringContextUtil.getBean("loginBusiness");
                case (byte) 0x86:
                    //远程启动充电
                    return SpringContextUtil.getBean("remoteStartBusiness");
                case (byte) 0x87:
                    //远程结束充电
                    return SpringContextUtil.getBean("remoteCloseBusiness");
                //record
                case 0x08:
                    //上传充电记录
                    return SpringContextUtil.getBean("uploadRecordBusiness");
                case 0x09:
                    //上传充电进度
                    return SpringContextUtil.getBean("uploadChargeRateBusiness");
                case 0x0A:
                    //上传充电过程监测数据
                    return SpringContextUtil.getBean("uploadChargeMonitorBusiness");
                case 0x0C:
                    //心跳
                    return SpringContextUtil.getBean("heartBeatBusiness");
                //setting
                case (byte)0x8E:
                    //校时
                    return SpringContextUtil.getBean("verifyTimeBusiness");
                case 0x1C:
                    //二维码设置
                    return SpringContextUtil.getBean("qrSetBusiness");
                case (byte)0x9D:
                    //重启
                    return SpringContextUtil.getBean("rebootBusiness");
                case (byte)0x9E:
                    //远程升级
                    return SpringContextUtil.getBean("remoteUpdateBusiness");
                case (byte)0x8B:
                    //计费规则设置
                    return SpringContextUtil.getBean("billRuleSetBusiness");
                case (byte)0X1F:
                    //请求升级包
                    return SpringContextUtil.getBean("requestUpdatePackageBusiness");
                case (byte)0X20:
                    //请求升级包
                    return SpringContextUtil.getBean("updateStatusBusiness");
                default:
                    log.error("未匹配到合适的命令码：{}",order);
                    return null;
            }
        }

}
