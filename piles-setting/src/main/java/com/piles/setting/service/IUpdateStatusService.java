package com.piles.setting.service;

import com.piles.setting.domain.UpdateStatusReport;
import com.piles.setting.entity.UpdateStatusRequest;

/**
 * 升级状态汇报
 */
public interface IUpdateStatusService {
    /**
     * 升级状态汇报
     *
     * @param updateStatusReport 汇报的状态信息
     * @return
     */
    void updateStatus(UpdateStatusReport updateStatusReport);
}
