package com.piles.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piles.common.business.IPushBusiness;
import com.piles.record.domain.UploadRecord;
import com.piles.setting.domain.GetQrCodeRequest;
import com.piles.setting.domain.GetQrCodeResponse;
import com.piles.setting.service.IGetQrCodeService;
import com.piles.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 申请二维码接口
 */
@Slf4j
@Service("xunDaoGetQrCodeService")
public class GetQrCodeServiceImpl implements IGetQrCodeService {
    /**
     * 默认1分钟超时
     */
    //TODO 修改这个url
    @Value("${get_qrcode.url}")
    private String url;


    @Override
    public GetQrCodeResponse getQrCode(GetQrCodeRequest getQrCodeRequest) {
        //请求一个接口
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("pileNo",getQrCodeRequest.getPileNo());
        jsonObject.put( "tradeTypeCode",getQrCodeRequest.getTradeTypeCode() );
        jsonObject.put( "pileType",getQrCodeRequest.getPileType() );

        Map<String,JSONObject> map= new HashedMap();
        map.put("args",jsonObject);


        //{"code":0,"pileNo":"343434","qrCode":"xxxxx"}
        String result = HttpRequest.httpPostForStrWithJson(map, url);
        log.info("请求后台获取二维码的结果:{}",result);
        GetQrCodeResponse getQrCodeResponse = null;
        try {
            getQrCodeResponse = JSON.parseObject(result, GetQrCodeResponse.class);
        }catch (Exception e){
            log.error("解析二维码返回结果:{} 异常:{}",result,e);
        }
        return getQrCodeResponse;

    }
}
