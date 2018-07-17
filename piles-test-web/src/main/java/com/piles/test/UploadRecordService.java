package com.piles.test;

import com.alibaba.fastjson.JSONObject;
import com.piles.record.domain.UploadRecord;
import com.piles.record.service.IUploadRecordService;
import com.piles.util.HttpRequest;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UploadRecordService implements IUploadRecordService {
    /**
     * 默认1分钟超时
     */
    @Value("${upload_record_ADDR}")
    private String url;
    @Override
    public boolean uploadRecord(UploadRecord uploadRecord) {
        //请求一个接口
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("orderNo",uploadRecord.getOrderNo());
        jsonObject.put("pileNo",uploadRecord.getPileNo());
        jsonObject.put("serial",null==uploadRecord.getSerial()?"":uploadRecord.getSerial());
        jsonObject.put("endReason",uploadRecord.getEndReason());
        jsonObject.put("totalAmmeterDegree",uploadRecord.getTotalAmmeterDegree());
        jsonObject.put( "tradeTypeCode",uploadRecord.getTradeTypeCode() );
        jsonObject.put( "pileType",uploadRecord.getPileType() );
        jsonObject.put( "gunNo",uploadRecord.getGunNo() );

//        http://tox.tunnel.qydev.com/order/powerEnd
        Map<String,JSONObject> map= new HashedMap();
        map.put("args",jsonObject);

        boolean flag=HttpRequest.httpPostWithJson(map,url);

        return flag;
    }


}
