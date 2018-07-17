package com.piles.common.util;

import com.piles.common.entity.type.TradeType;
import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author zlz
 * @version Id: GunElecAmountMapUtil, v 0.1
 */
public class GunElecAmountMapUtil {
    private static Map<String, BigDecimal> map = new HashedMap();
    private static String PRE = "ELEC";

    public static void put(String pileNo, TradeType tradeType, BigDecimal elecAmount) {
        String key = PRE + pileNo + "_" + tradeType.getCode();
        map.put(key, elecAmount);
    }

    public static BigDecimal get(String pileNo, int tradeType) {
        String key = PRE + pileNo + "_" + tradeType;
        return map.get(key);
    }
}
