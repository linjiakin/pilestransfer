package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.XunDaoFtpUpgradeIssuePushRequest;
import com.piles.setting.entity.XunDaoFtpUpgradeIssueRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglizhi on 2018/1/8.
 */
public interface IXunDaoFtpUpgradeIssueService {

    /**
     * 下发ftp升级程序数据接口
     *
     * @param xunDaoFtpUpgradeIssuePushRequest
     * @return 请求信息
     */
    BasePushCallBackResponse<XunDaoFtpUpgradeIssueRequest> doPush(XunDaoFtpUpgradeIssuePushRequest xunDaoFtpUpgradeIssuePushRequest);

    /**
     * 批量远程更新
     * @param remoteUpdateList
     * @return
     */
    public List<Map> doBatchPush(List<XunDaoFtpUpgradeIssuePushRequest> remoteUpdateList);


}
