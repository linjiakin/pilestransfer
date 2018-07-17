package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.XunDaoFtpUpgradeIssuePushRequest;
import com.piles.setting.entity.XunDaoFtpUpgradeIssueRequest;
import com.piles.setting.entity.XunDaoModifyIPPushRequest;
import com.piles.setting.entity.XunDaoModifyIPRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglizhi on 2018/4/10.
 */
public interface IXunDaoModifyIPeService {

    /**
     * 下发修改ip地址程序数据接口
     *
     * @param xunDaoModifyIPPushRequest
     * @return 请求信息
     */
    BasePushCallBackResponse<XunDaoModifyIPRequest> doPush(XunDaoModifyIPPushRequest xunDaoModifyIPPushRequest);


    List<Map> doBatchPush(List<XunDaoModifyIPPushRequest> modifyIPList);
}
