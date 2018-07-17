package com.piles.setting.service.imp;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.BillRuleSetPushRequest;
import com.piles.setting.entity.BillRuleSetRequest;
import com.piles.setting.service.IBillRuleSetPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 计费规则设置 给充电桩发送消息实现类
 */
@Slf4j
@Service
public class BillRuleSetPushServiceImpl implements IBillRuleSetPushService {

    @Resource(name = "pushBusinessImpl")
    IPushBusiness pushBusiness;

    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    @Override
    public BasePushCallBackResponse<BillRuleSetRequest> doPush(BillRuleSetPushRequest billRuleSetPushRequest) {
        byte[] pushMsg = BillRuleSetPushRequest.packBytes( billRuleSetPushRequest );
        BasePushCallBackResponse<BillRuleSetRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial( billRuleSetPushRequest.getSerial() );
        boolean flag = pushBusiness.push( pushMsg, billRuleSetPushRequest.getTradeTypeCode(),billRuleSetPushRequest.getPileNo(), basePushCallBackResponse , ECommandCode.BILL_RULE_SET_CODE);
        if (!flag) {
            basePushCallBackResponse.setCode( EPushResponseCode.CONNECT_ERROR );
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await( timeout, TimeUnit.MILLISECONDS );
            ChannelResponseCallBackMap.remove( billRuleSetPushRequest.getTradeTypeCode(),billRuleSetPushRequest.getPileNo(),billRuleSetPushRequest.getSerial() );
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error( e.getMessage(), e );
        }
        return basePushCallBackResponse;
    }
}
