package com.piles.setting.service.imp;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.RemoteUpdatePushRequest;
import com.piles.setting.entity.RemoteUpdateRequest;
import com.piles.setting.service.IRemoteUpdatePushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
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
 * 远程升级 给充电桩发送消息实现类
 */
@Slf4j
@Service("remoteUpdatePushServiceImpl_1")
public class RemoteUpdatePushServiceImpl implements IRemoteUpdatePushService, InitializingBean {

    @Resource(name = "pushBusinessImpl")
    IPushBusiness pushBusiness;
    ExecutorService executorService = null;

    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    //线程池线程数量
    @Value("${threadNum:5}")
    private int threadNum;


    @Override
    public BasePushCallBackResponse<RemoteUpdateRequest> doPush(RemoteUpdatePushRequest remoteUpdatePushRequest) {
        byte[] pushMsg = RemoteUpdatePushRequest.packBytes(remoteUpdatePushRequest);
        BasePushCallBackResponse<RemoteUpdateRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(remoteUpdatePushRequest.getSerial());
        basePushCallBackResponse.setPileNo(remoteUpdatePushRequest.getPileNo());
        boolean flag = pushBusiness.push(pushMsg, remoteUpdatePushRequest.getTradeTypeCode(), remoteUpdatePushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_UPDATE_CODE);
        if (!flag) {
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            CountDownLatch countDownLatch = basePushCallBackResponse.getCountDownLatch();
            countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if(countDownLatch.getCount()>0){
                log.error("远程升级推送失败超时，厂商类型:{},桩号:{}",remoteUpdatePushRequest.getTradeTypeCode(),remoteUpdatePushRequest.getPileNo());
            }
            ChannelResponseCallBackMap.remove(remoteUpdatePushRequest.getTradeTypeCode(), remoteUpdatePushRequest.getPileNo(), remoteUpdatePushRequest.getSerial());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return basePushCallBackResponse;
    }

    @Override
    public List<Map> doBatchPush(List<RemoteUpdatePushRequest> remoteUpdateList) {
        List<String> pileNoList = remoteUpdateList.stream().map(RemoteUpdatePushRequest::getPileNo).collect(Collectors.toList());
        log.info("进入蔚景批量更新接口,桩号{}",pileNoList);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        List<Map> results = Lists.newArrayList();
        try {
            if (CollectionUtils.isNotEmpty(remoteUpdateList)) {

                for (RemoteUpdatePushRequest remoteUpdateRequest : remoteUpdateList) {
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
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        log.info("蔚景批量更新结果为:{}", JSON.toJSONString(results));

        return results;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(threadNum);

    }
}
