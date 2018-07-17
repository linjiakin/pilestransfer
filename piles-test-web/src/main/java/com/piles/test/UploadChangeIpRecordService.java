package com.piles.test;

import com.alibaba.fastjson.JSONObject;
import com.piles.record.domain.UploadRecord;
import com.piles.record.service.IUploadRecordService;
import com.piles.setting.domain.UploadChageIpRecord;
import com.piles.setting.service.IUploadChangeIpService;
import com.piles.util.HttpRequest;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UploadChangeIpRecordService implements IUploadChangeIpService {
    /**
     * 默认1分钟超时
     */
    @Value("${change_ip.url}")
    private String url;

    @Override
    public boolean uploadRecord(UploadChageIpRecord uploadRecord) {
        //请求一个接口
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("pileNo",uploadRecord.getPileNo());
        jsonObject.put("addr",uploadRecord.getAddr());
        jsonObject.put("port",uploadRecord.getPort());
        jsonObject.put( "tradeTypeCode",uploadRecord.getTradeTypeCode() );


        jsonObject.put( "pileType",uploadRecord.getPileType() );

//        http://tox.tunnel.qydev.com/order/powerEnd
        Map<String,JSONObject> map= new HashedMap();
        map.put("args",jsonObject);

        boolean flag=HttpRequest.httpPostWithJson(map,url);

        return flag;
    }
}
