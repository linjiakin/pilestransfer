package com.piles.setting.business.impl;

import com.piles.common.business.BaseBusiness;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.BytesUtil;
import com.piles.setting.entity.UpdateStatusRequest;
import com.piles.setting.service.IUpdateStatusService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 链路测试帧(U 格式)(双向 交直流共用) 充电桩  运营管理系统
 */
@Slf4j
@Service("xunDaoChainTestBusiness")
public class XunDaoChainTestBusinessImpl implements IBusiness {

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到循道链路测试帧报文");
        return new byte[]{0x68,0x04,(byte)0x83,0x00,0x00,0x00};
    }

    public static void main(String[] args) {
        System.out.println(BytesUtil.bytesToIntLittle(new byte[]{0x03,0x00}));
        System.out.println(BytesUtil.bytesToIntLittle(new byte[]{0x21,0x00}));
    }
}
