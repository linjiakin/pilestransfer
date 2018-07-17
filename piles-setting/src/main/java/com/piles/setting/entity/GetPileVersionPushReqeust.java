package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import lombok.Data;

/**
 * Created by lgc on 18/1/8.
 */
@Data
public class GetPileVersionPushReqeust extends BasePushRequest {


    /**
     * 解析报文并封装request体
     *
     * @param getPileVersionPushReqeusts
     * @return
     */
    public static byte[] packBytesXunDao(GetPileVersionPushReqeust getPileVersionPushReqeusts) {
        byte[] data = BytesUtil.str2BcdLittle(getPileVersionPushReqeusts.getPileNo());
        byte[] head = new byte[]{0x68};
        byte[] length = new byte[]{0x13};
        byte[] contrl = BytesUtil.xundaoControlInt2Byte(Integer.parseInt(getPileVersionPushReqeusts.getSerial()));
        byte[] type = new byte[]{(byte) 0x85};
        byte[] beiyong = new byte[]{0x00};
        byte[] reason = ChannelMapByEntity.getPileTypeArr(getPileVersionPushReqeusts.getPileNo());
        byte[] crc = CRC16Util.getXunDaoCRC(data);
        byte[] addr = new byte[]{0x1F};


        byte[] temp = Bytes.concat(head, length, contrl, type, beiyong, reason, crc, addr, data);
        return temp;
    }
}
