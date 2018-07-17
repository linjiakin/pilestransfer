package com.piles.common.util;

import com.piles.common.entity.type.TradeType;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * @author lgc48027
 * @version Id: GunStatusMapUtil, v 0.1 2018/1/31 18:45 lgc48027 Exp $
 */
public class GunStatusMapUtil {
    private static Map<String,Integer> map=new HashedMap(  );

    public static void put(String pileNo, TradeType tradeType,int gunNo,int status){
        String key=pileNo+"_"+tradeType.getCode();
        //循道的需要拼上枪号
        if(TradeType.XUN_DAO.equals(tradeType)){
            key = key+"_"+gunNo;
        }
        if (map.containsKey( key )){
            Integer val=map.get( key );
            if (status!=val){
                map.replace( key,val,status );
            }
        }else {
            map.put( key,status );
        }
    }

    public static Integer get(String pileNo, int tradeType, int gunNo){

        String key=pileNo+"_"+tradeType;
        //循道要加上枪号
        if(TradeType.XUN_DAO.getCode() == tradeType){
            key = key+"_"+gunNo;
        }
        return map.get( key );
    }
    private static Map<String,String> map2=new HashedMap(  );

    public static void putDC(String pileNo, TradeType tradeType,int gunNo,String status){
        String key=pileNo+"_"+tradeType.getCode();
        //循道的需要拼上枪号
        if(TradeType.XUN_DAO.equals(tradeType)){
            key = key+"_"+gunNo;
        }
        if (map2.containsKey( key )){
            String val=map2.get( key );
            if (status!=val){
                map2.replace( key,val,status );
            }
        }else {
            map2.put( key,status );
        }
    }

    public static String getDC(String pileNo, int tradeType, int gunNo){

        String key=pileNo+"_"+tradeType;
        //循道要加上枪号
        if(TradeType.XUN_DAO.getCode() == tradeType){
            key = key+"_"+gunNo;
        }
        return map2.get( key );
    }
}
