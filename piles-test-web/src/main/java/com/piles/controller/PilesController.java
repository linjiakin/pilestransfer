package com.piles.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.util.*;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.control.entity.RemoteClosePushRequest;
import com.piles.control.entity.RemoteStartPushRequest;
import com.piles.control.service.IRemoteClosePushService;
import com.piles.control.service.IRemoteStartPushService;
import com.piles.setting.entity.*;
import com.piles.setting.service.*;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/piles")
public class PilesController {


    @Resource
    private IRemoteUpdatePushService remoteUpdatePushService;
    @Resource
    private IVerifyTimePushService verifyTimePushService;
    @Resource
    private IRebootPushService rebootPushService;
    @Resource
    private IBillRuleSetPushService billRuleSetPushService;

    @RequestMapping("/test")
    @ResponseBody
    public String  test(HttpServletRequest request) {
        String type = request.getParameter("type");

        switch (type) {
            case "1":
                String modle=request.getParameter("modle");
                String data=request.getParameter("data");
                String orderNo=request.getParameter("orderNo");
                RemoteStartPushRequest remoteStartPushRequest = new RemoteStartPushRequest();
                remoteStartPushRequest.setGunNo(1);
                remoteStartPushRequest.setOrderNo(Long.valueOf( orderNo ));
                remoteStartPushRequest.setPileNo("0000000080000004");
                remoteStartPushRequest.setSerial("0");
                remoteStartPushRequest.setChargeData(BigDecimal.valueOf(Double.valueOf(data)));
                remoteStartPushRequest.setChargeModel(Integer.parseInt( modle ));
                remoteStartPushRequest.setChargeStopCode("6464");
                remoteStartPushRequest.setTradeTypeCode(1);
                IRemoteStartPushService remoteStartPushService = serviceFactoryUtil.getService(TradeType.WEI_JING.getCode(), IRemoteStartPushService.class);

                log.info("远程启动充电请求返回报文:{}", JSON.toJSONString(remoteStartPushService.doPush(remoteStartPushRequest)));
                break;
            case "2":
                RemoteClosePushRequest remoteClosePushRequest = new RemoteClosePushRequest();
                remoteClosePushRequest.setGunNo(1);
                remoteClosePushRequest.setOrderNo(123456L);
                remoteClosePushRequest.setPileNo("0000000080000004");
                remoteClosePushRequest.setSerial("0");
                IRemoteClosePushService remoteClosePushService = serviceFactoryUtil.getService(TradeType.WEI_JING.getCode(), IRemoteClosePushService.class);

                log.info("远程结束充电请求返回报文:{}", JSON.toJSONString(remoteClosePushService.doPush(remoteClosePushRequest)));

                break;
            case "3":
                String pileNo = request.getParameter("pileNo");
                String softVersion=request.getParameter( "softVersion" );
                String protocalVersion=request.getParameter( "protocalVersion" );
                File file= FileUtil.getFile();
                if (null==file){
                    log.error( "找不到对应文件" );
                    break;
                }
                RemoteUpdatePushRequest remoteUpdatePushRequest = new RemoteUpdatePushRequest();
                remoteUpdatePushRequest.setPileNo(pileNo);
                remoteUpdatePushRequest.setSerial("0");
                try {
                    remoteUpdatePushRequest.setMd5( Md5Util.getMd5ByFile( file ));
                } catch (FileNotFoundException e) {
                    log.error( "文件找不到",e );
                }
                remoteUpdatePushRequest.setProtocolVersion(protocalVersion);
//                remoteUpdatePushRequest.setProtocolVersion("V1.31");
//                remoteUpdatePushRequest.setSoftVersion("V2.13");
                remoteUpdatePushRequest.setSoftVersion(softVersion);
                String url = "http://59.110.170.111:80/piles-test-web-1.0.0/soft/AcOneV2.13.bin";
                remoteUpdatePushRequest.setUrl(url);
                remoteUpdatePushRequest.setUrlLen(url.length());
                BasePushCallBackResponse<RemoteUpdateRequest> testRs=remoteUpdatePushService.doPush(remoteUpdatePushRequest);

                if (null!=testRs.getObj()&&testRs.getObj().getUpdateType()==2){
                    Util.map.put( pileNo,testRs.getObj().getDownMaxLenLimit() );
                }

                log.info("远程升级请求返回报文:{}", JSON.toJSONString(testRs));

                break;
            case "4":

                VerifyTimePushRequest verifyTimePushRequest = new VerifyTimePushRequest();
                verifyTimePushRequest.setPileNo("0000000080000004");
                verifyTimePushRequest.setSerial("0");
                Date dt= new Date(  );
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat( "yyMMddHHmmss" );

                verifyTimePushRequest.setServerTime(simpleDateFormat.format( dt ));
                log.info("校时请求返回报文:{}", JSON.toJSONString(verifyTimePushService.doPush(verifyTimePushRequest)));

                break;
            case "5":
                RebootPushRequest rebootPushRequest = new RebootPushRequest();
                rebootPushRequest.setPileNo("0000000080000004");
                rebootPushRequest.setSerial("0");

                log.info("重启请求返回报文:{}", JSON.toJSONString(rebootPushService.doPush(rebootPushRequest)));

                break;
            case "6":
                BillRuleSetPushRequest billRuleSetPushRequest = new BillRuleSetPushRequest();
                billRuleSetPushRequest.setPileNo("0000000080000004");
                billRuleSetPushRequest.setSerial("0");
                billRuleSetPushRequest.setBillingRuleId(4);
                billRuleSetPushRequest.setBillingRuleVersion(0);
                billRuleSetPushRequest.setEffectiveTime("171109160712");
                billRuleSetPushRequest.setSubscriptionPriceUnit(new BigDecimal(1));
                billRuleSetPushRequest.setServicePriceUnit(new BigDecimal(2));
                billRuleSetPushRequest.setParkingPriceUnit(new BigDecimal(3));
                billRuleSetPushRequest.setPointElectricPrice(new BigDecimal(4));
                billRuleSetPushRequest.setPeakElectricPrice(new BigDecimal(5));
                billRuleSetPushRequest.setOrdinaryElectricPrice(new BigDecimal(6));
                billRuleSetPushRequest.setDipElectricPrice(new BigDecimal(7));
                billRuleSetPushRequest.setPeriodCount(2);
                List<BillRulePeriod> billRulePeriodList = Lists.newArrayList();
                billRuleSetPushRequest.setBillRulePeriodList(billRulePeriodList);
                BillRulePeriod billRulePeriod1 = new BillRulePeriod();
                BillRulePeriod billRulePeriod2 = new BillRulePeriod();
                billRulePeriodList.add(billRulePeriod1);
                billRulePeriodList.add(billRulePeriod2);
                billRulePeriod1.setStartTime("2230");
                billRulePeriod1.setEndTime("2235");
                billRulePeriod1.setPeriodType(1);
                billRulePeriod2.setStartTime("2330");
                billRulePeriod2.setEndTime("2335");
                billRulePeriod2.setPeriodType(2);

                log.info("计费规则设置请求返回报文:{}", JSON.toJSONString(billRuleSetPushService.doPush(billRuleSetPushRequest)));
                break;
            case "7":
                RemoteStartPushRequest remoteStartPushRequest2 = new RemoteStartPushRequest();
                remoteStartPushRequest2.setGunNo(1);
                remoteStartPushRequest2.setOrderNo(123457L);
                remoteStartPushRequest2.setPileNo("0000000080000004");
                remoteStartPushRequest2.setSerial("0");
                remoteStartPushRequest2.setChargeData(new BigDecimal(2));
                remoteStartPushRequest2.setChargeModel(5);
                remoteStartPushRequest2.setChargeStopCode("6565");
                remoteStartPushService = serviceFactoryUtil.getService(TradeType.WEI_JING.getCode(), IRemoteStartPushService.class);

                log.info("远程启动充电请求返回报文:{}", JSON.toJSONString(remoteStartPushService.doPush(remoteStartPushRequest2)));
                break;
            case "8":
                String pileNo2=request.getParameter("pileNo");
                String tradeType=request.getParameter("tradeType");
                Channel channel= ChannelMapByEntity.getChannel( Integer.parseInt(tradeType),pileNo2 );
                if (null==channel){
                    return "no connection";
                }
                return "connection is:"+channel.remoteAddress();

        }

        return "{\"status\":\"200\"}";
    }

    /**
     * 循道远程开启充电
     *
     * @param remoteStartRequest
     * @return
     */
    @RequestMapping("/test2")
    @ResponseBody
    public String test2(RemoteStartPushRequest remoteStartRequest) {
        IRemoteStartPushService iRemoteStartPushService = serviceFactoryUtil.getService(TradeType.XUN_DAO.getCode(), IRemoteStartPushService.class);

        return JSONObject.toJSONString(iRemoteStartPushService.doPush(remoteStartRequest));
    }

    /**
     * 循道远程结束充电
     *
     * @param remoteStartRequest
     * @return
     */
    @RequestMapping("/test3")
    @ResponseBody
    public String test3(RemoteClosePushRequest remoteStartRequest) {
        IRemoteClosePushService iRemoteStartPushService = serviceFactoryUtil.getService(TradeType.XUN_DAO.getCode(), IRemoteClosePushService.class);

        return JSONObject.toJSONString(iRemoteStartPushService.doPush(remoteStartRequest));
    }

    /**
     * 循道获取当前软件版本号
     *
     * @param remoteStartRequest
     * @return
     */
    @RequestMapping("/test4")
    @ResponseBody
    public String test4(GetPileVersionPushReqeust remoteStartRequest) {
        IGetPileVersionPushService iRemoteStartPushService = serviceFactoryUtil.getService(TradeType.XUN_DAO.getCode(), IGetPileVersionPushService.class);

        return JSONObject.toJSONString(iRemoteStartPushService.doPush(remoteStartRequest));
    }

    /**
     * 循道远程结束充电
     *
     * @param remoteStartRequest
     * @return
     */
    @RequestMapping("/test5")
    @ResponseBody
    public String test5(XunDaoFtpUpgradeIssuePushRequest remoteStartRequest) {
        IXunDaoFtpUpgradeIssueService iRemoteStartPushService = serviceFactoryUtil.getService(TradeType.XUN_DAO.getCode(), IXunDaoFtpUpgradeIssueService.class);

        return JSONObject.toJSONString(iRemoteStartPushService.doPush(remoteStartRequest));
    }

    @Resource
    ServiceFactoryUtil serviceFactoryUtil;

    public static void main(String[] args) {
        String data="0.1";
        BigDecimal decimal=BigDecimal.valueOf(Double.valueOf(data));
        System.out.println(decimal.byteValue());
    }
}
