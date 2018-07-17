package com.piles.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.piles.setting.domain.UpdateStatusReport;
import com.piles.setting.entity.UpdateStatusRequest;
import com.piles.setting.service.IUpdateStatusService;
import com.piles.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UpdateStatusServiceImpl implements IUpdateStatusService {

    @Value("${admin.update.url}")
    private String updateUrl;

    @Override
    public void updateStatus(UpdateStatusReport updateStatusReport) {
        log.info("进入升级状态汇报接口" + updateStatusReport.toString());
//        UpdateStatusRequest request = new UpdateStatusRequest();
//        BeanUtils.copyProperties(updateStatusReport, request);
        Map<String, JSONObject> map = new HashedMap();
        map.put("result", JSONObject.parseObject(JSON.toJSONString(updateStatusReport, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue)));

        boolean flag = HttpRequest.httpPostWithJson(map, updateUrl);
    }
}
