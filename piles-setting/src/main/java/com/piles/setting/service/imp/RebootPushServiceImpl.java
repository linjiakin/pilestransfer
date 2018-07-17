package com.piles.setting.service.imp;

import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.BillRuleSetPushRequest;
import com.piles.setting.entity.BillRuleSetRequest;
import com.piles.setting.entity.RebootPushRequest;
import com.piles.setting.entity.RebootRequest;
import com.piles.setting.service.IBillRuleSetPushService;
import com.piles.setting.service.IRebootPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 重启 给充电桩发送消息实现类
 */
@Slf4j
@Service
public class RebootPushServiceImpl implements IRebootPushService {

    @Resource(name = "pushBusinessImpl")
    IPushBusiness pushBusiness;

    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;



    @Override
    public BasePushCallBackResponse<RebootRequest> doPush(RebootPushRequest rebootPushRequest) {
        byte[] pushMsg = RebootPushRequest.packBytes( rebootPushRequest );
        BasePushCallBackResponse<RebootRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial( rebootPushRequest.getSerial() );
        boolean flag = pushBusiness.push( pushMsg, rebootPushRequest.getTradeTypeCode(),rebootPushRequest.getPileNo(), basePushCallBackResponse  , ECommandCode.REBOOT_CODE );
        if (!flag) {
            basePushCallBackResponse.setCode( EPushResponseCode.CONNECT_ERROR );
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await( timeout, TimeUnit.MILLISECONDS );
            ChannelResponseCallBackMap.remove( rebootPushRequest.getTradeTypeCode(),rebootPushRequest.getPileNo(),rebootPushRequest.getSerial() );
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error( e.getMessage(), e );
        }
        return basePushCallBackResponse;
    }
}
