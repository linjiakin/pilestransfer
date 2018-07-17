package com.piles.common.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.business.IBusinessFactory;
import com.piles.common.entity.type.XunDaoTypeCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 获取循道解析报文业务对象
 */
@Slf4j
@Component("xunDaoBusinessFactory")
public class XunDaoBusinessFactory implements IBusinessFactory {

    @Override
    public IBusiness getByMsg(byte[] msg) {
        //判断是否是登录报文
        boolean loginFlag = checkIfLoginMsg(msg);
        if(loginFlag){
            log.info("接收到登录报文");
            return SpringContextUtil.getBean("xunDaologinBusiness");
        }
        //如果报文长度小于6 不存在
        if(msg.length < 6){
            log.error("报文长度小于6，不识别：{}",msg);
            return null;
        }
        //判断是定长报文
        if(msg.length == 6){
            //如果是定长报文根据控制判断是什么报文

            if(checkIfChainTestMsg(msg)){
                return SpringContextUtil.getBean("xunDaoChainTestBusiness");
            }else{
                log.error("无法识别的报文：{}",msg);
                return null;
            }
        }
        //如果是变长根据协议类型判断
        //取出第七个字节，变长报文该位置的类型标识
        int typeCode = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 6, 1));
        XunDaoTypeCode xunDaoTypeCode = XunDaoTypeCode.fromCode(typeCode);
        if (xunDaoTypeCode == null){
            log.error("接收到报文{}，不识别的类型标识：{}",msg,typeCode);
            return null;
        }
        switch (xunDaoTypeCode){
            //总召唤命令
            case TOTAL_SUMMON_CODE:
            //时钟同步命令
            case LOCK_SYNC_CODE:
                return SpringContextUtil.getBean("xunDaoLockSyncBusiness");
            //充电桩消费记录数据
            case EXPENSE_RECORD_CODE:
                //离线交易上线后上传交易记录数据(充电桩后台 交直流 共用)
                if(msg.length>=8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,12,1))==3) {
                    return SpringContextUtil.getBean("xunDaoUnploadTradeDataBusiness");
                }
                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 4) {
                    return SpringContextUtil.getBean("xunDaoDCUnploadTradeDataBusiness");
                }
                //获取充电桩软件版本上行数据(充电桩后台 交直流共 用)
                if(msg.length>=8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,12,1))==31) {
                    return SpringContextUtil.getBean("xunDaoGetPileVersionBusiness");
                }
                //远程升级后充电桩升级结果及当前版本上送(充电桩后台 交直流共用)
                if(msg.length>=8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,12,1))==53) {
                    return SpringContextUtil.getBean("xunDaoUpdateStatusBusiness");
                }
                //充电 回复 or 停止 回复
                if(msg.length>=8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,12,1))==28) {
                    return SpringContextUtil.getBean("xunDaoStartOrStopBusiness");
                }
                //远程升级后充电桩升级结果及当前版本上送(充电桩后台 交直流共用)
                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 35) {
                    return SpringContextUtil.getBean("xunDaoFtpUpgradeIssueBusiness");
                }
                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 70) {
                    return SpringContextUtil.getBean("xunDaoChargeMonitorBusiness");
                }
                //修改ip下发回复报文
                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 71) {
                    return SpringContextUtil.getBean("xunDaoModifyIPBusiness");
                }

                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 72) {
                    return SpringContextUtil.getBean("xunDaoModifyIPReportBusiness");
                }

                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 73) {
                    return SpringContextUtil.getBean("xunDaoGetQrCodeBusiness");
                }
                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 80) {
                    return SpringContextUtil.getBean("xunDaoDCUploadChargeMonitorBusiness");
                }
                if (msg.length >= 8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 12, 1)) == 74) {
                    return SpringContextUtil.getBean("xunDaoAppendChargeBusiness");
                }
//            int typeCode = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 6,
            case SEND_DATA_CODE:

            //监控数据项
            case MONITOR_DATA_CODE:
                //充电过程实时监测数据
                if(msg.length>=8 && BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg,12,1))==1) {
                    return SpringContextUtil.getBean("xunDaoUploadChargeMonitorBusiness");
                }
            default:
                log.error("接收到报文{}，未匹配到合适的命令码：{}",msg,typeCode);
                return null;
        }

    }
    //校验是否是登录报文
    private boolean checkIfLoginMsg(byte[] msg){
//        起始域  长度 协议类型 连接类型   设备编号    站地址
//         1字节  1字节 1字节  1字节   8字节BCD码   2字节
//         0x68         0x01
        int length = msg.length;
        //登录报文为14
        if(length != 14){
            return false;
        }
        if(0x01 != msg[2]){
            return false;
        }
        return true;
    }
    //校验是否是链路测试报文
    private boolean checkIfChainTestMsg(byte[] msg){
//        起始域 数据长度 控制域内容
//         0x68 0x04    0x43 0x00 0x00 0x00 链路测试
        if(msg[0] == 0x68 && msg[1] ==0x04 && msg[2] == 0x43 && msg[3] == 0x00 && msg[4] == 0x00 && msg[5] == 0x00 ){
            return true;
        }else {
            return false;
        }
    }
}
