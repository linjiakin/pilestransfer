package com.piles.setting.service.imp;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.GetPileVersionPushReqeust;
import com.piles.setting.entity.GetPileVersionReqeust;
import com.piles.setting.service.IGetPileVersionPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 远程升级 给充电桩发送消息实现类
 */
@Slf4j
@Service("xunDaoGetPileVersionPushServiceImpl_2")
public class XunDaoGetPileVersionPushServiceImpl implements IGetPileVersionPushService {

    @Resource(name = "xunDaoPushBusinessImpl")
    IPushBusiness pushBusiness;

    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;


    @Override
    public BasePushCallBackResponse<GetPileVersionReqeust> doPush(GetPileVersionPushReqeust getPileVersionPushReqeust) {
        byte[] pushMsg = GetPileVersionPushReqeust.packBytesXunDao(getPileVersionPushReqeust);
        BasePushCallBackResponse<GetPileVersionReqeust> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(getPileVersionPushReqeust.getSerial());
        boolean flag = pushBusiness.push(pushMsg, getPileVersionPushReqeust.getTradeTypeCode(), getPileVersionPushReqeust.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag){
            basePushCallBackResponse.setCode( EPushResponseCode.CONNECT_ERROR );
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await(timeout, TimeUnit.MILLISECONDS);
            ChannelResponseCallBackMap.remove(getPileVersionPushReqeust.getTradeTypeCode(), getPileVersionPushReqeust.getPileNo(), getPileVersionPushReqeust.getSerial());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error( e.getMessage(),e );
        }
        return basePushCallBackResponse;
    }
}
