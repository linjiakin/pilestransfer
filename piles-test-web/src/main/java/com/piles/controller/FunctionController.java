package com.piles.controller;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.GunElecAmountMapUtil;
import com.piles.common.util.GunStatusMapUtil;
import com.piles.common.util.GunWorkStatusMapUtil;
import com.piles.entity.enums.ResponseCode;
import com.piles.entity.vo.CheckConnectionRequest;
import com.piles.entity.vo.ModifyIPRequest;
import com.piles.entity.vo.PileChargeStatusRequest;
import com.piles.entity.vo.PileStatusRequest;
import com.piles.record.entity.XunDaoChargeMonitorRequest;
import com.piles.record.service.IChargeMonitorPushService;
import com.piles.setting.entity.XunDaoModifyIPPushRequest;
import com.piles.setting.service.IXunDaoModifyIPeService;
import com.piles.util.ServiceFactoryUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.piles.common.entity.type.EPushResponseCode.CONNECT_ERROR;
import static com.piles.common.entity.type.EPushResponseCode.READ_OK;

@Slf4j
@Controller
@RequestMapping("/tool")
public class FunctionController {
    @Resource
    ServiceFactoryUtil serviceFactoryUtil;

    @Resource
    IXunDaoModifyIPeService xunDaoModifyIPeService;


    /**
     * 查询链接是否可用
     *
     * @param checkConnectionRequest
     * @return
     */
    @RequestMapping(value = "/connection", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> charge(CheckConnectionRequest checkConnectionRequest) {
        log.info( "查询链接是否可用信息:" + JSON.toJSONString( checkConnectionRequest ) );
        Map<String, Object> map = new HashedMap();
        map = checkParams( checkConnectionRequest.getTradeTypeCode(), checkConnectionRequest.getPileNo() );
        if (MapUtils.isNotEmpty( map )) {
            return map;
        }
        Channel channel = ChannelMapByEntity.getChannel( checkConnectionRequest.getTradeTypeCode(), checkConnectionRequest.getPileNo() );
        if (null == channel) {
            map.put( "status", ResponseCode.CONNECNTION_ERROR.getCode() );
            map.put( "msg", ResponseCode.CONNECNTION_ERROR.getMsg() );
        } else {
            Map<String, String> data = new HashedMap();
            map.put( "status", ResponseCode.OK.getCode() );
            map.put( "msg", ResponseCode.OK.getMsg() );
            map.put( "data", data );
            data.put( "connection", channel.remoteAddress().toString() );
        }
        log.info( "return查询链接是否可用信息:" + JSON.toJSONString( map ) );
        return map;

    }

    /**
     * 查询枪状态是否可用
     *
     * @param pileStatusRequest
     * @return
     */
    @RequestMapping(value = "/pileStatus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> status(PileStatusRequest pileStatusRequest) {
        log.info( "查询枪状态是否可用信息:" + JSON.toJSONString( pileStatusRequest ) );
        Map<String, Object> map = new HashedMap();
        map = checkParams( pileStatusRequest.getTradeTypeCode(), pileStatusRequest.getPileNo() );
        if (MapUtils.isNotEmpty( map )) {
            log.info( "return查询枪状态是否可用:" + JSON.toJSONString( map ) );
            return map;
        }
        map = checkPileTypeParams( pileStatusRequest.getTradeTypeCode(), pileStatusRequest.getPileNo(), pileStatusRequest.getPileType(), pileStatusRequest.getGunNo() );
        if (MapUtils.isNotEmpty( map )) {
            log.info( "return查询枪状态是否可用:" + JSON.toJSONString( map ) );
            return map;
        }
        Integer status = GunStatusMapUtil.get( pileStatusRequest.getPileNo(), pileStatusRequest.getTradeTypeCode(), pileStatusRequest.getGunNo() );
        String gunStatusStr = GunStatusMapUtil.getDC(pileStatusRequest.getPileNo(), pileStatusRequest.getTradeTypeCode(), pileStatusRequest.getGunNo());

        if (null == status && StringUtils.isEmpty(gunStatusStr)) {
            map.put( "status", ResponseCode.NO_STATUS.getCode() );
            map.put( "msg", ResponseCode.NO_STATUS.getMsg() );
        } else {
            Map<String, Object> data = new HashedMap();
            map.put( "status", ResponseCode.OK.getCode() );
            map.put( "msg", ResponseCode.OK.getMsg() );
            map.put( "data", data );
            boolean canCharged = false;

            switch (pileStatusRequest.getTradeTypeCode()) {
                case 1:
                    if (status == 0) {
                        canCharged = true;
                    }
                    break;
                case 2:
                    Integer pileType = ChannelMapByEntity.getPileType( pileStatusRequest.getPileNo() );
                    switch (pileType) {
                        case 2:
                        case 3:
                        case 4:
                            BigDecimal highestAllowElectricity = GunElecAmountMapUtil.get( pileStatusRequest.getPileNo(), pileStatusRequest.getTradeTypeCode() );
                            String workStatus = GunWorkStatusMapUtil.get( pileStatusRequest.getPileNo(), pileStatusRequest.getTradeTypeCode() );
                            //当电流存在，并且大于0小于等于1的时候返回true
                            if (!"01".equals( workStatus ) &&
                                    (status == 1 || (status == 2 && (highestAllowElectricity != null &&
                                            highestAllowElectricity.compareTo( BigDecimal.ZERO ) >= 0 &&
                                            highestAllowElectricity.compareTo( BigDecimal.ONE ) <= 0)))) {
                                canCharged = true;
                            }
                            break;
                        case 5:
                        case 6:

                            String[] gunStatus = gunStatusStr.split( "," );
                            if ("1".equals( gunStatus[0] ) && "00".equals( gunStatus[1] )) {
                                canCharged = true;
                            }
                            break;
                    }
                    break;
            }
            data.put( "canCharged", canCharged );
            data.put( "gunStatus", status );
        }
        log.info( "return查询枪状态是否可用信息:" + JSON.toJSONString( map ) );
        return map;

    }

    /**
     * 查询枪状态是否可用
     *
     * @param pileChargeStatusRequest
     * @return
     */
    @RequestMapping(value = "/pileChargeStatus", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> chargeStatus(PileChargeStatusRequest pileChargeStatusRequest) {
        log.info( "查询充电桩充电进度:" + JSON.toJSONString( pileChargeStatusRequest ) );

        Map<String, Object> map = new HashedMap();
        map = checkParams( pileChargeStatusRequest.getTradeTypeCode(), pileChargeStatusRequest.getPileNo() );
        if (MapUtils.isNotEmpty( map )) {
            log.info( "return充电桩充电进度fan:" + JSON.toJSONString( map ) );
            return map;
        }
        map = checkPileTypeParams( pileChargeStatusRequest.getTradeTypeCode(), pileChargeStatusRequest.getPileNo(), pileChargeStatusRequest.getPileType(), pileChargeStatusRequest.getGunNo() );
        if (MapUtils.isNotEmpty( map )) {
            log.info( "return充电桩充电进度fan:" + JSON.toJSONString( map ) );
            return map;
        }
        if (TradeType.WEI_JING.getCode() == pileChargeStatusRequest.getTradeTypeCode()) {
            map.put( "status", "-1" );
            map.put( "msg", "充电桩不支持查询充电进度" );
            log.info( "return请充电桩充电进度fan:" + JSON.toJSONString( map ) );
            return map;
        }
        Channel channel = ChannelMapByEntity.getChannel( pileChargeStatusRequest.getTradeTypeCode(), pileChargeStatusRequest.getPileNo() );
        if (null == channel) {
            map.put( "status", ResponseCode.CONNECNTION_ERROR.getCode() );
            map.put( "msg", ResponseCode.CONNECNTION_ERROR.getMsg() );
        } else {
            IChargeMonitorPushService iChargeMonitorPushService = serviceFactoryUtil.getService( pileChargeStatusRequest.getTradeTypeCode(), IChargeMonitorPushService.class );
            BasePushRequest basePushRequest = new BasePushRequest();
            basePushRequest.setPileNo( pileChargeStatusRequest.getPileNo() );
            basePushRequest.setTradeTypeCode( pileChargeStatusRequest.getTradeTypeCode() );
            basePushRequest.setSerial( pileChargeStatusRequest.getSerial() );
            basePushRequest.setPileType( pileChargeStatusRequest.getPileType() );
            basePushRequest.setGunNo( pileChargeStatusRequest.getGunNo() );
            BasePushCallBackResponse<XunDaoChargeMonitorRequest> xunDaoChargeMonitorRequestBasePushCallBackResponse = iChargeMonitorPushService.doPush( basePushRequest );

            if (xunDaoChargeMonitorRequestBasePushCallBackResponse.getCode() != READ_OK) {
                //重试1
                xunDaoChargeMonitorRequestBasePushCallBackResponse = iChargeMonitorPushService.doPush( basePushRequest );
            }
            //交流设置为1
            int type = 1;
            if (pileChargeStatusRequest.getPileType() != 3 && pileChargeStatusRequest.getPileType() != 4) {
                type = 2;//直流设置为2
            }
            switch (xunDaoChargeMonitorRequestBasePushCallBackResponse.getCode()) {
                case READ_OK:
                    map.put( "status", READ_OK.getCode() );
                    map.put( "msg", "查询电量进度成功,详细结果见结果" );
                    map.put( "type", type );
                    map.put( "data", xunDaoChargeMonitorRequestBasePushCallBackResponse.getObj() );
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
        }

        log.info( "return查询充电桩充电进度:" + JSON.toJSONString( map ) );
        return map;

    }


    private Map<String, Object> checkParams(Integer tradeTypeCode, String pileNo) {
        Map<String, Object> map = new HashedMap();
        //check 参数

        if (tradeTypeCode == null) {
            map.put( "status", "-1" );
            map.put( "msg", "充电桩厂商类型为空" );
            return map;
        }
        if (StringUtils.isEmpty( pileNo )) {
            map.put( "status", "-1" );
            map.put( "msg", "充电桩编号为空" );

            return map;
        }
        //获取连接channel 获取不到无法推送
        Channel channel = ChannelMapByEntity.getChannel( tradeTypeCode, pileNo );
        if (null == channel) {
            map.put( "status", "400" );
            map.put( "msg", "充电桩不在线" );
            return map;
        }
        return map;

    }

    private Map<String, Object> checkPileTypeParams(Integer tradeTypeCode, String pileNo, Integer pileType, Integer gunNo) {
        Map<String, Object> map = new HashedMap();
        //check 参数

        //循道的桩类型和枪号不能为空
        if (TradeType.XUN_DAO.getCode() == tradeTypeCode) {
            if (pileType == null) {
                map.put( "status", "-1" );
                map.put( "msg", "充电桩类型不能为空" );
                return map;
            }
            if (pileType != ChannelMapByEntity.getPileType( pileNo )) {
                log.error( "充电桩:{} 传入的充电桩类型:{},系统记录的充电桩桩类型:{}", pileNo, pileType, ChannelMapByEntity.getPileType( pileNo ) );
                map.put( "status", "-1" );
                map.put( "msg", "传入的充电桩类型与系统记录的充电桩类型不一致" );
                return map;
            }

            if (gunNo == null) {
                map.put( "status", "-1" );
                map.put( "msg", "枪号不能为空" );
                return map;
            }
        }

        return map;

    }

    @RequestMapping(value = "/connectAddress", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> connectAddress(ModifyIPRequest request) {
        log.info( "修改ip地址请求信息:" + JSON.toJSONString( request ) );
        Map<String, Object> map = checkXunDaoParams( request );
        if (null != map) {
            return Lists.newArrayList( map );
        }

        List<Map> results = Lists.newArrayList();

        String[] pileArr = request.getPileNos().split( "," );

        List<String> pileList = Arrays.stream( pileArr ).collect( Collectors.toList() );
        List<XunDaoModifyIPPushRequest> modifyIPList = Lists.newArrayList();
        List<String> noConnectPileNoList = Lists.newArrayList();
        //已经判断过pileNos是否为空
        for (String pileNo : pileList) {
            Map result = Maps.newHashMap();
            result.put( "pileNo", pileNo );
            //获取连接channel 获取不到无法推送
            Channel channel = ChannelMapByEntity.getChannel( request.getTradeTypeCode(), pileNo );
            if (null == channel) {
                result.put( "status", "0" );
                result.put( "msg", "充电桩链接不可用" );
                results.add( result );
                log.info( "桩号{}在修改ip地址时不可用", pileNo );
                noConnectPileNoList.add( pileNo );
                //连接不可用的不进行升级
                continue;
            } else {
                result.put( "status", "1" );
                result.put( "msg", "充电桩链接可用" );
                results.add( result );
            }

            XunDaoModifyIPPushRequest pushRequest = new XunDaoModifyIPPushRequest();
            BeanUtils.copyProperties( request, pushRequest );
            pushRequest.setPileNo( pileNo );
            modifyIPList.add( pushRequest );
        }
        if (CollectionUtils.isEmpty( modifyIPList )) {
            log.info( "修改ip地址时时所有的桩号都未发现可用链接，不进行修改IP地址" );
        } else {
            xunDaoModifyIPeService.doBatchPush( modifyIPList );
        }
        if (CollectionUtils.isNotEmpty( noConnectPileNoList )) {
            //TODO 是否需要调用没有链接 单独调用后台接口
        }
        log.info( "return请求修改IP地址请求结果{}:", JSON.toJSONString( results ) );
        return results;

    }

    private Map<String, Object> checkXunDaoParams(ModifyIPRequest request) {
        Map<String, Object> map = new HashedMap();
        //check 参数
        int serial = 0;


        if (StringUtils.isEmpty( request.getTradeTypeCode() )) {
            map.put( "status", "-1" );
            map.put( "msg", "充电桩厂商类型为空" );
            log.info( "return请求修改ip请求,充电桩厂商类型为空:" + JSON.toJSONString( map ) );
            return map;
        }
        if (StringUtils.isEmpty( request.getPileNos() )) {
            map.put( "status", "-1" );
            map.put( "msg", "充电桩编号为空" );
            log.info( "return请求修改ip请求,充电桩编号为空:" + JSON.toJSONString( map ) );
            return map;
        }
        if (StringUtils.isEmpty( request.getSerial() )) {
            map.put( "status", "-1" );
            map.put( "msg", "流水号为空" );
            log.info( "return请求修改ip请求,流水号为空:" + JSON.toJSONString( map ) );
            return map;
        }
        try {
            serial = Integer.parseInt( request.getSerial() );
            if (serial > 65535) {
                map.put( "status", "-1" );
                map.put( "msg", "流水号不能大于65535" );
                log.info( "return请求修改ip请求,流水号不能大于65535:" + JSON.toJSONString( map ) );
            }
        } catch (Exception e) {
            map.put( "status", "-1" );
            map.put( "msg", "流水号需要是数字" );
            log.info( "return请求修改ip请求,流水号需要是数字:" + JSON.toJSONString( map ) );
            return map;
        }

        if (StringUtils.isEmpty( request.getAddr() )) {
            map.put( "status", "-1" );
            map.put( "msg", "服务器ip为空" );
            log.info( "return请求修改ip请求,服务器ip为空:" + JSON.toJSONString( map ) );
            return map;
        }

        if (request.getAddr().getBytes().length > 30) {
            map.put( "status", "-1" );
            map.put( "msg", "服务器ip长度太长" );
            log.info( "return请求修改ip请求,服务器ip长度太长:" + JSON.toJSONString( map ) );
            return map;
        }
        return null;

    }
}
