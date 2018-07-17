package com.piles.setting.service.imp;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.*;
import com.piles.setting.entity.XunDaoFtpUpgradeIssuePushRequest;
import com.piles.setting.entity.XunDaoFtpUpgradeIssueRequest;
import com.piles.setting.entity.XunDaoModifyIPPushRequest;
import com.piles.setting.entity.XunDaoModifyIPRequest;
import com.piles.setting.service.IXunDaoModifyIPeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 循道 修改ip程序数据下行
 */
@Slf4j
@Service("xunDaoModifyIPService")
public class XunDaoModifyIPServiceImpl implements IXunDaoModifyIPeService {
    @Resource(name = "xunDaoPushBusinessImpl")
    IPushBusiness pushBusiness;

    //类型标识 0x85
    private int typeCode = 133;

    //记录类型 0x47
    private int recordType = 71;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    //线程池线程数量
    @Value("${modifyIPThreadNum:5}")
    private int modifyIPThreadNum;
    @Override
    public BasePushCallBackResponse<XunDaoModifyIPRequest> doPush(XunDaoModifyIPPushRequest xunDaoModifyIPPushRequest) {
        log.info("进入推送修改IP地址接口,参数:{}",JSON.toJSONString(xunDaoModifyIPPushRequest));
        byte[] pushMsg = XunDaoModifyIPPushRequest.packBytes(xunDaoModifyIPPushRequest);
        pushMsg = buildHead(pushMsg, xunDaoModifyIPPushRequest);
        BasePushCallBackResponse<XunDaoModifyIPRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(xunDaoModifyIPPushRequest.getSerial());
        //设置桩号
        basePushCallBackResponse.setPileNo(xunDaoModifyIPPushRequest.getPileNo());
        boolean flag = pushBusiness.push(pushMsg, xunDaoModifyIPPushRequest.getTradeTypeCode(), xunDaoModifyIPPushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag) {
            log.error( "推送修改IP地址,厂商类型:{},桩号:{} 无法获取到长连接,请检查充电桩连接状态", xunDaoModifyIPPushRequest.getTradeTypeCode(), xunDaoModifyIPPushRequest.getPileNo() );
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            CountDownLatch countDownLatch = basePushCallBackResponse.getCountDownLatch();
            countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if (countDownLatch.getCount() > 0) {
                log.error("修改IP地址推送失败超时，厂商类型:{},桩号:{}", xunDaoModifyIPPushRequest.getTradeTypeCode(), xunDaoModifyIPPushRequest.getPileNo());
            }
            ChannelResponseCallBackMap.remove(xunDaoModifyIPPushRequest.getTradeTypeCode(), xunDaoModifyIPPushRequest.getPileNo(), xunDaoModifyIPPushRequest.getSerial());
        } catch (InterruptedException e) {
            log.error("修改IP地址推送异常:{}", e);
        }
        return basePushCallBackResponse;
    }

    @Override
    public List<Map> doBatchPush(List<XunDaoModifyIPPushRequest> modifyIPList) {
        if (CollectionUtils.isEmpty(modifyIPList)) {
            return null;
        }
        List<String> pileNoList = modifyIPList.stream().map(XunDaoModifyIPPushRequest::getPileNo).collect(Collectors.toList());
        log.info("进入循道批量修改ip接口,桩号{}", pileNoList);
        ExecutorService executorService = Executors.newFixedThreadPool(modifyIPThreadNum);

        List<Map> results = Lists.newArrayList();
        try {
            if (CollectionUtils.isNotEmpty(modifyIPList)) {

                for (XunDaoModifyIPPushRequest request : modifyIPList) {
                    log.info("进入循道批量修改ip信息{}", JSON.toJSONString(modifyIPList));
                    executorService.submit(() -> {
                        doPush(request);
                    });
                }
            }
        } catch (Exception e) {
            log.error("批量更新地址后台推送报文时异常:{}",e);
        } finally {
            executorService.shutdown();
        }
//        log.info("循道批量修改IP结果为:{}", JSON.toJSONString(results));
        return results;    }

    //添加报文头
    private byte[] buildHead(byte[] dataMsg, XunDaoModifyIPPushRequest request) {
        byte[] result = Bytes.concat(new byte[]{0x68}, BytesUtil.intToBytesLittle(51, 1));
        result = Bytes.concat(result, BytesUtil.xundaoControlInt2Byte(Integer.parseInt(request.getSerial())));
        //添加类型标识
        result = Bytes.concat(result, BytesUtil.intToBytesLittle(typeCode, 1));
        //添加备用
        result = Bytes.concat(result, BytesUtil.intToBytesLittle(0, 1));
        //添加桩类型
        result = Bytes.concat(result, ChannelMapByEntity.getPileTypeArr(request.getPileNo()));
//        result = Bytes.concat(result, new byte[]{0x03, 0x00});
        //添加crc
        result = Bytes.concat(result, CRC16Util.getXunDaoCRC(dataMsg));
        //添加记录类型
        result = Bytes.concat(result, BytesUtil.intToBytesLittle(recordType, 1));

        result = Bytes.concat(result, dataMsg);

        return result;
    }
}
