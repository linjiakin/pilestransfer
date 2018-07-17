package com.piles.setting.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import lombok.Data;

/**
 * Created by lgc on 18/1/8.
 */
@Data
public class GetPileVersionReqeust extends BasePushResponse {
    /**
     * 充电桩编号
     */
    private String pileNo;
    /**
     * 厂商类型
     */
    private TradeType tradeType;
    /**
     * 厂商编号
     */
    private String merchantNo;
    /**
     * 软件版本号
     */
    private String softVersion;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static GetPileVersionReqeust packEntityXunDao(byte[] msg) {
        GetPileVersionReqeust request = new GetPileVersionReqeust();
        request.setPileNo(BytesUtil.bcd2StrLittle(BytesUtil.copyBytes(msg, 13, 8)));
        byte[] merchantNobytes = BytesUtil.copyBytes(msg, 21, 20);
        int i = 0;
        while (merchantNobytes[i] != 0x00) {
            i++;
        }
        merchantNobytes = BytesUtil.copyBytes(merchantNobytes, 0, i);
        byte[] versionBytes = BytesUtil.copyBytes(msg, 41, 20);
        i = 0;
        while (versionBytes[i] != 0x00) {
            i++;
        }
        versionBytes = BytesUtil.copyBytes(versionBytes, 0, i);
        request.setMerchantNo(new String(merchantNobytes));
        request.setSoftVersion(new String(versionBytes));
        request.setTradeType(TradeType.XUN_DAO);
        return request;
    }

}
