package com.piles.setting.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.BillRuleSetRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 计费规则设置
 */
@Slf4j
@Service("billRuleSetBusiness")
public class BillRuleSetBusinessImpl extends BaseBusiness {


    @Override
    protected byte[] processBody(byte[] bodyBytes, Channel incoming, int order) {
        //依照报文体规则解析报文
        BillRuleSetRequest ruleSetRequest = BillRuleSetRequest.packEntity( bodyBytes );
        //调用底层接口
        ChannelResponseCallBackMap.callBack( incoming, String.valueOf( order ), ruleSetRequest );
        return null;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.BILL_RULE_SET_ANSWER_CODE;
    }
}
