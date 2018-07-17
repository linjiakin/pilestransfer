package com.piles.setting.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.BaseBusiness;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.util.BytesUtil;
import com.piles.setting.entity.QrSetRequest;
import com.piles.setting.entity.UpdatePackageRequest;
import com.piles.setting.entity.UpdatePackageResponse;
import com.piles.setting.service.IQrSetService;
import com.piles.setting.service.IRequestUpdatePackageService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 请求升级包 充电桩  运营管理系统
 */
@Slf4j
@Service("requestUpdatePackageBusiness")
public class RequestUpdatePackageBusinessImpl extends BaseBusiness{


    @Resource
    private IRequestUpdatePackageService requestUpdatePackageService;


    @Override
    protected byte[] processBody(byte[] bodyBytes,Channel incoming,int order) {
        log.info( "接收到充电桩请求升级包报文" );
        //依照报文体规则解析报文
        UpdatePackageRequest updatePackageRequest = UpdatePackageRequest.packEntity(bodyBytes, incoming);
        log.info( "接收到充电桩请求升级包报文:{}", updatePackageRequest.toString() );

        //负数直接返回
        if(updatePackageRequest.getIndex()<0){
            UpdatePackageResponse response = new UpdatePackageResponse();
            response.setResult(2);
            return UpdatePackageResponse.packBytes(response);
        }
        //调用底层接口
        UpdatePackageResponse response = requestUpdatePackageService.getUpdatePackage(updatePackageRequest);
        byte[] actualContent = response.getActualContent();
        if(actualContent == null){
            actualContent = new byte[]{};
        }
        int length = actualContent.length;
        if(length>0x9E){//158
            //TODO 超长后返回什么
        }
        response.setCurrentSegmentLen(length);
        if(response.getCurrentIndex()>response.getTotalIndex()){
            response.setResult(2);
        }
        log.info("获取后台返回的升级包信息：{}",response);
        byte[] repsBytes = UpdatePackageResponse.packBytes(response);
        //组装返回报文体
        return repsBytes;
    }

    @Override
    public ECommandCode getReponseCode() {
        return ECommandCode.REQUEST_UPDATE_PACKAGE_ANSWER_CODE;
    }


}
