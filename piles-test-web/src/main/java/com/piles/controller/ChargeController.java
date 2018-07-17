package com.piles.controller;


import com.alibaba.fastjson.JSON;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.control.entity.*;
import com.piles.control.service.IRemoteAppendPushService;
import com.piles.control.service.IRemoteClosePushService;
import com.piles.control.service.IRemoteStartPushService;
import com.piles.entity.vo.ChargeAppendRequest;
import com.piles.entity.vo.ChargeRemoteStartRequest;
import com.piles.entity.vo.ChargeRemoteStopRequest;
import com.piles.util.ServiceFactoryUtil;
import com.piles.util.Util;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

import static com.piles.common.entity.type.EPushResponseCode.CONNECT_ERROR;
import static com.piles.common.entity.type.EPushResponseCode.READ_OK;

@Slf4j
@Controller
@RequestMapping("/charge")
public class ChargeController {
    @Resource
    ServiceFactoryUtil serviceFactoryUtil;


    /**
     * 启动充电
     *
     * @param remoteStartRequest
     * @return
     */
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> charge(ChargeRemoteStartRequest remoteStartRequest) {
        Map<String, Object> map = new HashedMap();
        try {
            log.info("请求充电请求信息:" + JSON.toJSONString(remoteStartRequest));
            map = checkParams( remoteStartRequest );
            if (null != map) {
                return map;
            }
            map = new HashedMap();
            if (!(1 == remoteStartRequest.getChargeModel()
                    || 2 == remoteStartRequest.getChargeModel()
                    || 3 == remoteStartRequest.getChargeModel()
                    || 4 == remoteStartRequest.getChargeModel())
                    ) {
                map.put( "status", "-1" );
                map.put( "msg", "充电模式不可用" );
                log.info( "return请求充电请求fan:" + JSON.toJSONString( map ) );
                return map;
            }

            IRemoteStartPushService iRemoteStartPushService = serviceFactoryUtil.getStartPushService( remoteStartRequest.getTradeTypeCode() );
            RemoteStartPushRequest remoteStartPushRequest = new RemoteStartPushRequest();
            remoteStartPushRequest.setTradeTypeCode( remoteStartRequest.getTradeTypeCode() );
            remoteStartPushRequest.setGunNo( remoteStartRequest.getGunNo() );
            remoteStartPushRequest.setOrderNo( remoteStartRequest.getOrderNo() );
            remoteStartPushRequest.setPileNo( remoteStartRequest.getPileNo() );
//        remoteStartPushRequest.setPileNo("0000000080000004");
            remoteStartPushRequest.setSerial( remoteStartRequest.getSerial() );
            remoteStartPushRequest.setChargeData( remoteStartRequest.getChargeData() );
            remoteStartPushRequest.setChargeModel( remoteStartRequest.getChargeModel() );
            remoteStartPushRequest.setChargeStopCode( StringUtils.isEmpty( remoteStartRequest.getChargeStopCode() ) ? "6464" : remoteStartRequest.getChargeStopCode() );
            BasePushCallBackResponse<RemoteStartRequest> remoteStartRequestBasePushCallBackResponse = iRemoteStartPushService.doPush( remoteStartPushRequest );

            if (remoteStartRequestBasePushCallBackResponse.getCode() != READ_OK) {
                //重试1
                remoteStartRequestBasePushCallBackResponse = iRemoteStartPushService.doPush( remoteStartPushRequest );
            }
            log.info( "远程启动充电请求返回报文:{}", JSON.toJSONString( remoteStartRequestBasePushCallBackResponse ) );


            switch (remoteStartRequestBasePushCallBackResponse.getCode()) {
                case READ_OK:
                    map.put( "status", READ_OK.getCode() );
                    map.put( "msg", "启动充电发送命令成功,详细结果见结果" );
                    map.put( "data", remoteStartRequestBasePushCallBackResponse.getObj() );
                    Util.chargePushOrderOk.put( String.valueOf( remoteStartPushRequest.getSerial() ), remoteStartRequestBasePushCallBackResponse.getObj() );
                    break;
                case TIME_OUT:
                case WRITE_OK:
                    map.put( "status", 300 );
                    map.put( "msg", "请求超时" );
                    break;
                case CONNECT_ERROR:
                    map.put( "status", CONNECT_ERROR.getCode() );
                    map.put( "msg", "充电桩链接不可用" );
                    break;
                default:
                    break;

            }

            log.info( "return请求充电请求fan:" + JSON.toJSONString( map ) );
        }catch (Exception e){
            log.error( "-----------error"+e );
        }
        return map;

    }

    /**
     * 追加充电
     *
     * @param chargeAppendRequest
     * @return
     */
    @RequestMapping(value = "/appendCharge", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> appendharge(ChargeAppendRequest chargeAppendRequest) {
        log.info("请求追加充电请求信息:" + JSON.toJSONString(chargeAppendRequest));
        Map<String, Object> map = new HashedMap();

        map = checkParams(chargeAppendRequest);
        if (null != map) {
            return map;
        }
        map = new HashedMap();


        RemoteAppendPushRequest remoteAppendPushRequest = new RemoteAppendPushRequest();
        remoteAppendPushRequest.setTradeTypeCode(chargeAppendRequest.getTradeTypeCode());
        remoteAppendPushRequest.setGunNo(chargeAppendRequest.getGunNo());
        remoteAppendPushRequest.setOrderNo(chargeAppendRequest.getOrderNo());
        remoteAppendPushRequest.setPileNo(chargeAppendRequest.getPileNo());
        remoteAppendPushRequest.setSerial(chargeAppendRequest.getSerial());
        remoteAppendPushRequest.setChargeData(chargeAppendRequest.getChargeData());
        remoteAppendPushRequest.setChargeModel(chargeAppendRequest.getChargeModel());
        remoteAppendPushRequest.setPileType(chargeAppendRequest.getPileType());
        IRemoteAppendPushService iRemoteStartPushService = serviceFactoryUtil.getService(remoteAppendPushRequest.getTradeTypeCode(), IRemoteAppendPushService.class);
        BasePushCallBackResponse<RemoteAppendRequest> remoteStartRequestBasePushCallBackResponse = iRemoteStartPushService.doPush(remoteAppendPushRequest);
        int i = 0;
        while (remoteStartRequestBasePushCallBackResponse.getCode() != READ_OK) {
            //重试2次 总共调用三次

            remoteStartRequestBasePushCallBackResponse = iRemoteStartPushService.doPush(remoteAppendPushRequest);
            i++;
            if (i >= 2) {
                break;
            }
        }

        log.info("远程启动充电追加充电请求返回报文:{}", JSON.toJSONString(remoteStartRequestBasePushCallBackResponse));

        map.put("status", remoteStartRequestBasePushCallBackResponse.getCode().getCode());

        switch (remoteStartRequestBasePushCallBackResponse.getCode()) {
            case READ_OK:
                map.put("status", READ_OK.getCode());
                map.put("msg", "启动追加充电发送命令成功,详细结果见结果");
                map.put("data", remoteStartRequestBasePushCallBackResponse.getObj());
                break;
            case TIME_OUT:
            case WRITE_OK:
                map.put("status", 300);
                map.put("msg", "充电请求超时");
                break;
            case CONNECT_ERROR:
                map.put("status", CONNECT_ERROR.getCode());
                map.put("msg", "充电桩链接不可用");
                break;
            default:
                break;

        }

        log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
        return map;

    }

    private Map<String, Object> checkParams(ChargeRemoteStartRequest remoteStartRequest) {
        Map<String, Object> map = new HashedMap();
        //check 参数
        int serial = 0;


        if (StringUtils.isEmpty(remoteStartRequest.getTradeTypeCode())) {
            map.put("status", "-1");
            map.put("msg", "充电桩厂商类型为空");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteStartRequest.getPileNo())) {
            map.put("status", "-1");
            map.put("msg", "充电桩编号为空");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteStartRequest.getSerial())) {
            map.put("status", "-1");
            map.put("msg", "流水号为空");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        try {
            serial = Integer.parseInt(remoteStartRequest.getSerial());
            if (serial > 65535) {
                map.put("status", "-1");
                map.put("msg", "流水号不能大于65535");
                log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            }
        } catch (Exception e) {
            map.put("status", "-1");
            map.put("msg", "流水号需要是数字");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }

        if (StringUtils.isEmpty(remoteStartRequest.getOrderNo())) {
            map.put("status", "-1");
            map.put("msg", "订单号不可用");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }

        //获取连接channel 获取不到无法推送
        Channel channel = ChannelMapByEntity.getChannel(remoteStartRequest.getTradeTypeCode(), remoteStartRequest.getPileNo());
        if (null == channel) {
            map.put("status", "400");
            map.put("msg", "充电桩链接不可用");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        return null;

    }

    /**
     * 停止充电
     *
     * @param chargeRemoteStopRequest
     * @return
     */
    @RequestMapping(value = "/stopCharge", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> stopCharge(ChargeRemoteStopRequest chargeRemoteStopRequest) {
        log.info("请求停止充电请求信息:" + JSON.toJSONString(chargeRemoteStopRequest));
        RemoteClosePushRequest remoteClosePushRequest = new RemoteClosePushRequest();
        BeanUtils.copyProperties(chargeRemoteStopRequest,remoteClosePushRequest);
        Map<String, Object> map = new HashedMap();
        map = checkParamsStopCharge(remoteClosePushRequest);
        if (null != map) {
            return map;
        }
        map = new HashedMap();


        IRemoteClosePushService iRemoteClosePushService = serviceFactoryUtil.getService(remoteClosePushRequest.getTradeTypeCode(), IRemoteClosePushService.class);
        BasePushCallBackResponse<RemoteCloseRequest> remoteStartRequestBasePushCallBackResponse = iRemoteClosePushService.doPush(remoteClosePushRequest);

        if (remoteStartRequestBasePushCallBackResponse.getCode() != READ_OK) {
            //重试1
            remoteStartRequestBasePushCallBackResponse = iRemoteClosePushService.doPush(remoteClosePushRequest);
        }
        log.info("远程停止充电请求返回报文:{}", JSON.toJSONString(remoteStartRequestBasePushCallBackResponse));


        switch (remoteStartRequestBasePushCallBackResponse.getCode()) {
            case READ_OK:
                map.put("status", READ_OK.getCode());
                map.put("msg", "停止充电发送命令成功,详细结果见结果");
                map.put("data", remoteStartRequestBasePushCallBackResponse.getObj());
                break;
            case TIME_OUT:
            case WRITE_OK:
                map.put("status", 300);
                map.put("msg", "请求超时");
                break;
            case CONNECT_ERROR:
                map.put("status", CONNECT_ERROR.getCode());
                map.put("msg", "充电桩链接不可用");
                break;
            default:
                break;

        }

        log.info("return请求停止充电请求fan:" + JSON.toJSONString(map));
        return map;

    }

    private Map<String, Object> checkParamsStopCharge(RemoteClosePushRequest remoteClosePushRequest) {
        Map<String, Object> map = new HashedMap();
        //check 参数
        int serial = 0;


        if (StringUtils.isEmpty(remoteClosePushRequest.getTradeTypeCode())) {
            map.put("status", "-1");
            map.put("msg", "充电桩厂商类型为空");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteClosePushRequest.getPileNo())) {
            map.put("status", "-1");
            map.put("msg", "充电桩编号为空");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteClosePushRequest.getSerial())) {
            map.put("status", "-1");
            map.put("msg", "流水号为空");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        try {
            serial = Integer.parseInt(remoteClosePushRequest.getSerial());
            if (serial > 65535) {
                map.put("status", "-1");
                map.put("msg", "流水号不能大于65535");
                log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            }
        } catch (Exception e) {
            map.put("status", "-1");
            map.put("msg", "流水号需要是数字");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }

        if (StringUtils.isEmpty(remoteClosePushRequest.getOrderNo())) {
            map.put("status", "-1");
            map.put("msg", "订单号不可用");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }

        //获取连接channel 获取不到无法推送
        Channel channel = ChannelMapByEntity.getChannel(remoteClosePushRequest.getTradeTypeCode(), remoteClosePushRequest.getPileNo());
        if (null == channel) {
            map.put("status", "400");
            map.put("msg", "充电桩链接不可用");
            log.info("return请求充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        return null;

    }

    private Map<String, Object> checkParams(ChargeAppendRequest chargeAppendRequest) {
        Map<String, Object> map = new HashedMap();
        //check 参数
        int serial = 0;


        if (StringUtils.isEmpty(chargeAppendRequest.getTradeTypeCode())) {
            map.put("status", "-1");
            map.put("msg", "充电桩厂商类型为空");
            log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(chargeAppendRequest.getPileNo())) {
            map.put("status", "-1");
            map.put("msg", "充电桩编号为空");
            log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(chargeAppendRequest.getSerial())) {
            map.put("status", "-1");
            map.put("msg", "流水号为空");
            log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        try {
            serial = Integer.parseInt(chargeAppendRequest.getSerial());
            if (serial > 65535) {
                map.put("status", "-1");
                map.put("msg", "流水号不能大于65535");
                log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            }
        } catch (Exception e) {
            map.put("status", "-1");
            map.put("msg", "流水号需要是数字");
            log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            return map;
        }

        if (StringUtils.isEmpty(chargeAppendRequest.getOrderNo())) {
            map.put("status", "-1");
            map.put("msg", "订单号不可用");
            log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            return map;
        }

        //获取连接channel 获取不到无法推送
        Channel channel = ChannelMapByEntity.getChannel(chargeAppendRequest.getTradeTypeCode(), chargeAppendRequest.getPileNo());
        if (null == channel) {
            map.put("status", "400");
            map.put("msg", "充电桩链接不可用");
            log.info("return请求追加充电请求fan:" + JSON.toJSONString(map));
            return map;
        }
        return null;

    }

}
