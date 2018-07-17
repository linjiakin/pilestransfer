package com.piles.setting.service.imp;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.XunDaoFtpUpgradeIssuePushRequest;
import com.piles.setting.entity.XunDaoFtpUpgradeIssueRequest;
import com.piles.setting.service.IXunDaoFtpUpgradeIssueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 循道 FTP升级程序数据下行
 */
@Slf4j
@Service("ftpUpgradeIssueService_2")
public class XunDaoFtpUpgradeIssueServiceImpl implements IXunDaoFtpUpgradeIssueService {
    @Resource(name = "xunDaoPushBusinessImpl")
    IPushBusiness pushBusiness;

    //类型标识
    private int typeCode = 133;

    //记录类型
    private int recordType = 35;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    //线程池线程数量
    @Value("${threadNum:5}")
    private int threadNum;

    @Override
    public BasePushCallBackResponse<XunDaoFtpUpgradeIssueRequest> doPush(XunDaoFtpUpgradeIssuePushRequest xunDaoFtpUpgradeIssuePushRequest) {
        byte[] pushMsg = XunDaoFtpUpgradeIssuePushRequest.packBytes(xunDaoFtpUpgradeIssuePushRequest);
        pushMsg = buildHead(pushMsg, xunDaoFtpUpgradeIssuePushRequest);
        BasePushCallBackResponse<XunDaoFtpUpgradeIssueRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(xunDaoFtpUpgradeIssuePushRequest.getSerial());
        //设置桩号
        basePushCallBackResponse.setPileNo(xunDaoFtpUpgradeIssuePushRequest.getPileNo());
        boolean flag = pushBusiness.push(pushMsg, xunDaoFtpUpgradeIssuePushRequest.getTradeTypeCode(), xunDaoFtpUpgradeIssuePushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag) {
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            CountDownLatch countDownLatch = basePushCallBackResponse.getCountDownLatch();
            countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if (countDownLatch.getCount() > 0) {
                log.error("远程升级推送失败超时，厂商类型:{},桩号:{}", xunDaoFtpUpgradeIssuePushRequest.getTradeTypeCode(), xunDaoFtpUpgradeIssuePushRequest.getPileNo());
            }
            ChannelResponseCallBackMap.remove(xunDaoFtpUpgradeIssuePushRequest.getTradeTypeCode(), xunDaoFtpUpgradeIssuePushRequest.getPileNo(), xunDaoFtpUpgradeIssuePushRequest.getSerial());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return basePushCallBackResponse;
    }

    @Override
    public List<Map> doBatchPush(List<XunDaoFtpUpgradeIssuePushRequest> remoteUpdateList) {
        if (CollectionUtils.isEmpty(remoteUpdateList)) {
            return null;
        }
        List<String> pileNoList = remoteUpdateList.stream().map(XunDaoFtpUpgradeIssuePushRequest::getPileNo).collect(Collectors.toList());
        log.info("进入循道批量更新接口,桩号{}", pileNoList);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        List<Map> results = Lists.newArrayList();
        try {
            if (CollectionUtils.isNotEmpty(remoteUpdateList)) {

                for (XunDaoFtpUpgradeIssuePushRequest remoteUpdateRequest : remoteUpdateList) {
                    log.info("进入循道批量升级信息{}", JSON.toJSONString(remoteUpdateList));
                    executorService.submit(() -> {
                        doPush(remoteUpdateRequest);
                    });
                }
//                List<Callable<BasePushCallBackResponse>> callableList = remoteUpdateList.stream().map(s -> {
//                    return new Callable<BasePushCallBackResponse>() {
//                        @Override
//                        public BasePushCallBackResponse call() throws Exception {
//                            return doPush(s);
//                        }
//                    };
//                }).collect(Collectors.toList());
//
//                List<Future<BasePushCallBackResponse>> futureList = executorService.invokeAll(callableList);
//                for (Future<BasePushCallBackResponse> future : futureList) {
//                    Map map = Maps.newHashMap();
//                    results.add(map);
//                    //比countdownlatch多10秒超时
//                    BasePushCallBackResponse basePushCallBackResponse = future.get(timeout + 10, TimeUnit.MILLISECONDS);
//                    map.put("pileNo", basePushCallBackResponse.getPileNo());
//                    switch (basePushCallBackResponse.getCode()) {
//                        case READ_OK:
//                            map.put("status", EPushResponseCode.READ_OK.getCode());
//                            map.put("msg", "远程升级发送命令成功,详细结果见结果");
//                            map.put("data", basePushCallBackResponse.getObj());
//                            break;
//                        case TIME_OUT:
//                        case WRITE_OK:
//                            map.put("status", 300);
//                            map.put("msg", "请求超时");
//                            break;
//                        case CONNECT_ERROR:
//                            map.put("status", EPushResponseCode.CONNECT_ERROR.getCode());
//                            map.put("msg", "充电桩链接不可用");
//                            break;
//                        default:
//                            break;
//                    }
//                }
            }
        } catch (Exception e) {
            log.error("批量升级时后台推送报文时异常:{}",e);
        } finally {
            executorService.shutdown();
        }
        log.info("循道批量更新结果为:{}", JSON.toJSONString(results));
        return results;
    }

    //添加报文头
    private byte[] buildHead(byte[] dataMsg, XunDaoFtpUpgradeIssuePushRequest request) {
        byte[] result = Bytes.concat(new byte[]{0x68}, BytesUtil.intToBytesLittle(184, 1));
        result = Bytes.concat(result, BytesUtil.xundaoControlInt2Byte(Integer.parseInt(request.getSerial())));
        //添加类型标识
        result = Bytes.concat(result, BytesUtil.intToBytesLittle(typeCode, 1));
        //添加备用
        result = Bytes.concat(result, BytesUtil.intToBytesLittle(0, 1));
        //添加传送原因
        result = Bytes.concat(result, ChannelMapByEntity.getPileTypeArr(request.getPileNo()));
        //添加crc
        result = Bytes.concat(result, CRC16Util.getXunDaoCRC(dataMsg));
        //添加记录类型
        result = Bytes.concat(result, BytesUtil.intToBytesLittle(recordType, 1));

        result = Bytes.concat(result, dataMsg);

        return result;
    }
}
