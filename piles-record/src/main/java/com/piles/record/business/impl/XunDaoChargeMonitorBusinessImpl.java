package com.piles.record.business.impl;


import com.piles.common.business.IBusiness;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.record.entity.XunDaoChargeMonitorRequest;
import com.piles.record.entity.XunDaoDCChargeMonitorRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 上传充电过程监测数据 接口实现
 */
@Slf4j
@Service("xunDaoChargeMonitorBusiness")
public class XunDaoChargeMonitorBusinessImpl implements IBusiness {

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info( "接收到循道充电桩上传充电过程监测数据报文" );
        int pileType = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 8, 2));
        String order = String.valueOf( BytesUtil.xundaoControlByte2Int( BytesUtil.copyBytes( msg, 2, 4 ) ) );
        //3、4 交流   5、6直流
        BasePushResponse xunDaoChargeMonitorRequest = null;
        if(pileType == 3 || pileType == 4){
            xunDaoChargeMonitorRequest = XunDaoChargeMonitorRequest.packEntity( msg );
        }else{
            //直流
            xunDaoChargeMonitorRequest = XunDaoDCChargeMonitorRequest.packEntity( msg );
        }
        log.info( "接收到循道充电桩上传充电过程监测数据报文:{}", xunDaoChargeMonitorRequest.toString() );

        ChannelResponseCallBackMap.callBack( incoming, order, xunDaoChargeMonitorRequest );
        //组装返回报文体
        return null;
    }

}
