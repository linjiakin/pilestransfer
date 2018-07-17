package com.piles.util;


import com.piles.control.entity.RemoteStartRequest;
import org.apache.commons.collections.map.HashedMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Util {
    public static Map<String,Integer> map= new HashedMap(  );

    //key 是序列号
    public static Map<String,RemoteStartRequest> chargePushOrderOk = new HashedMap(  );
    //key 是订单编号

    public static Map<String,Set<String>> orderNo2Seril = new HashedMap(  );

    //自增序列号
    private static AtomicInteger atomicInteger =new AtomicInteger(1);

    public static int getNum(){
        int num= atomicInteger.getAndIncrement();
        if (num-99999999>0){
            resetNum();
            num= atomicInteger.getAndIncrement();
        }
        return num;
    }
    private static synchronized void resetNum(){

        atomicInteger =new AtomicInteger(1);
    }
    public static RemoteStartRequest getChargePushOrderOk(String orderNo){
        Set<String> serils=orderNo2Seril.get(orderNo);
        if (null==serils){
            return null;
        }
        RemoteStartRequest remoteStartRequest=null;
        for (String ser:serils){
            remoteStartRequest=chargePushOrderOk.get(ser);
            if (null!=remoteStartRequest){
                return remoteStartRequest;
            }
        }
        return remoteStartRequest;
    }



}
