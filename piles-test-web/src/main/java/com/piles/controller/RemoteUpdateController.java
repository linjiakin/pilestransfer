package com.piles.controller;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.entity.vo.UpdateRemoteRequest;
import com.piles.entity.vo.XunDaoUpdateRemoteRequest;
import com.piles.setting.entity.RemoteUpdatePushRequest;
import com.piles.setting.entity.XunDaoFtpUpgradeIssuePushRequest;
import com.piles.setting.service.IRemoteUpdatePushService;
import com.piles.setting.service.IXunDaoFtpUpgradeIssueService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 远程升级接口
 */
@Slf4j
@Controller
@RequestMapping("/remoteUpdate")
public class RemoteUpdateController {
    @Resource
    IRemoteUpdatePushService remoteUpdatePushService;
    @Resource
    IXunDaoFtpUpgradeIssueService xunDaoFtpUpgradeIssueService;

    @Value("${xun.ftp.server.ip}")
    private String serverIp;
    @Value("${xun.ftp.server.port}")
    private String serverPort;
    @Value("${xun.ftp.server.userName}")
    private String serverUserName;
    @Value("${xun.ftp.server.password}")
    private String serverPassword;

    /**
     * 蔚景远程升级
     *
     * @param updateRemoteRequest
     * @return
     */
    @RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> doUpdate(UpdateRemoteRequest updateRemoteRequest) {
        log.info("请求蔚景远程升级请求信息:" + JSON.toJSONString(updateRemoteRequest));
        Map<String, Object> map = checkParams(updateRemoteRequest);
        if (null != map) {
            return Lists.newArrayList(map);
        }

        List<Map> results = Lists.newArrayList();

//        String[] pileArr = StringUtils.split(updateRemoteRequest.getPileNos(), ",");
        String[] pileArr = updateRemoteRequest.getPileNos().split(",");
        List<String> pileList = Arrays.stream(pileArr).collect(Collectors.toList());
        List<RemoteUpdatePushRequest> remoteUpdateList = Lists.newArrayList();
        List<String> noConnectPileNoList = Lists.newArrayList();
        //已经判断过pileNos是否为空
        for (String pileNo : pileList) {
            Map result = Maps.newHashMap();
            result.put("pileNo", pileNo);
            //获取连接channel 获取不到无法推送
            Channel channel = ChannelMapByEntity.getChannel(updateRemoteRequest.getTradeTypeCode(), pileNo);
            if (null == channel) {
                result.put("status", "0");
                result.put("msg", "充电桩链接不可用");
                results.add(result);
                log.info("桩号{}在升级的时候链接不可用", pileNo);
                noConnectPileNoList.add(pileNo);
                //连接不可用的不进行升级
                continue;
            } else {
                result.put("status", "1");
                result.put("msg", "充电桩发起升级");
                results.add(result);
            }
            RemoteUpdatePushRequest remoteUpdatePushRequest = new RemoteUpdatePushRequest();
            remoteUpdatePushRequest.setTradeTypeCode(updateRemoteRequest.getTradeTypeCode());
            remoteUpdatePushRequest.setPileNo(pileNo);
            remoteUpdatePushRequest.setSerial(updateRemoteRequest.getSerial());
            remoteUpdatePushRequest.setSoftVersion(updateRemoteRequest.getSoftVersion());
            remoteUpdatePushRequest.setProtocolVersion(updateRemoteRequest.getProtocolVersion());
            remoteUpdateList.add(remoteUpdatePushRequest);
        }
        if (CollectionUtils.isEmpty(remoteUpdateList)) {
            log.info("远程升级时所有的桩号都未发现可用链接，不进行升级");
        } else {
            remoteUpdatePushService.doBatchPush(remoteUpdateList);
        }
        if (CollectionUtils.isNotEmpty(noConnectPileNoList)) {
            //TODO 是否需要调用没有链接 单独调用后台接口
        }
        //只返回请求连接是否存在
        log.info("return请求蔚景远程升级请求结果{}:", JSON.toJSONString(results));
        return results;

    }

    private Map<String, Object> checkParams(UpdateRemoteRequest remoteUpdateRequest) {
        Map<String, Object> map = new HashedMap();
        //check 参数
        int serial = 0;


        if (StringUtils.isEmpty(remoteUpdateRequest.getTradeTypeCode())) {
            map.put("status", "-1");
            map.put("msg", "充电桩厂商类型为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteUpdateRequest.getPileNos())) {
            map.put("status", "-1");
            map.put("msg", "充电桩编号为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteUpdateRequest.getSerial())) {
            map.put("status", "-1");
            map.put("msg", "流水号为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
        try {
            serial = Integer.parseInt(remoteUpdateRequest.getSerial());
            if (serial > 65535) {
                map.put("status", "-1");
                map.put("msg", "流水号不能大于65535");
                log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            }
        } catch (Exception e) {
            map.put("status", "-1");
            map.put("msg", "流水号需要是数字");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }

        if (StringUtils.isEmpty(remoteUpdateRequest.getSoftVersion())) {
            map.put("status", "-1");
            map.put("msg", "软件版本号为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }

        return null;

    }

    /**
     * 循道远程升级
     *
     * @param updateRemoteRequest
     * @return
     */
    @RequestMapping(value = "/doXunDaoUpdate", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> doXunDaoUpdate(XunDaoUpdateRemoteRequest updateRemoteRequest) {
        log.info("请求循道远程升级请求信息:" + JSON.toJSONString(updateRemoteRequest));
        Map<String, Object> map = checkXunDaoParams(updateRemoteRequest);
        if (null != map) {
            return Lists.newArrayList(map);
        }

        List<Map> results = Lists.newArrayList();

//        String[] pileArr = StringUtils.split(updateRemoteRequest.getPileNos(), ",");
        String[] pileArr = updateRemoteRequest.getPileNos().split(",");

        List<String> pileList = Arrays.stream(pileArr).collect(Collectors.toList());
        List<XunDaoFtpUpgradeIssuePushRequest> remoteUpdateList = Lists.newArrayList();
        List<String> noConnectPileNoList = Lists.newArrayList();
        //已经判断过pileNos是否为空
        for (String pileNo : pileList) {
            Map result = Maps.newHashMap();
            result.put("pileNo", pileNo);
            //获取连接channel 获取不到无法推送
            Channel channel = ChannelMapByEntity.getChannel(updateRemoteRequest.getTradeTypeCode(), pileNo);
            if (null == channel) {
                result.put("status", "0");
                result.put("msg", "充电桩链接不可用");
                results.add(result);
                log.info("桩号{}在升级的时候链接不可用", pileNo);
                noConnectPileNoList.add(pileNo);
                //连接不可用的不进行升级
                continue;
            } else {
                result.put("status", "1");
                result.put("msg", "充电桩发起升级");
                results.add(result);
            }

            XunDaoFtpUpgradeIssuePushRequest remoteUpdate = new XunDaoFtpUpgradeIssuePushRequest();
            BeanUtils.copyProperties(updateRemoteRequest, remoteUpdate);
            remoteUpdate.setServerIp(serverIp);
            remoteUpdate.setServerPort(serverPort);
            remoteUpdate.setUserName(serverUserName);
            remoteUpdate.setPassword(serverPassword);
            remoteUpdate.setPileNo(pileNo);

            //3、4 交流   5、6直流
//            Integer pileType = ChannelMapByEntity.getPileType(pileNo);
//            if(pileType == 3 || pileType == 4){
//                remoteUpdate.setDownloadUrl("/piletransfer/soft/" + updateRemoteRequest.getSoftVersion() + ".bin");
//            }else{
//                remoteUpdate.setDownloadUrl("/piletransfer/soft/" + updateRemoteRequest.getSoftVersion());
//            }
            remoteUpdate.setDownloadUrl("/piletransfer/soft/" + updateRemoteRequest.getSoftVersion());
            remoteUpdate.setSoftVersion(updateRemoteRequest.getSoftVersion());
            remoteUpdateList.add(remoteUpdate);
        }
        if (CollectionUtils.isEmpty(remoteUpdateList)) {
            log.info("远程升级时所有的桩号都未发现可用链接，不进行升级");
        } else {
            xunDaoFtpUpgradeIssueService.doBatchPush(remoteUpdateList);
        }
        if (CollectionUtils.isNotEmpty(noConnectPileNoList)) {
            //TODO 是否需要调用没有链接 单独调用后台接口
        }
        log.info("return请求远程升级请求结果{}:", JSON.toJSONString(results));
        return results;

    }

    private Map<String, Object> checkXunDaoParams(XunDaoUpdateRemoteRequest remoteUpdateRequest) {
        Map<String, Object> map = new HashedMap();
        //check 参数
        int serial = 0;


        if (StringUtils.isEmpty(remoteUpdateRequest.getTradeTypeCode())) {
            map.put("status", "-1");
            map.put("msg", "充电桩厂商类型为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteUpdateRequest.getPileNos())) {
            map.put("status", "-1");
            map.put("msg", "充电桩编号为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
        if (StringUtils.isEmpty(remoteUpdateRequest.getSerial())) {
            map.put("status", "-1");
            map.put("msg", "流水号为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
        try {
            serial = Integer.parseInt(remoteUpdateRequest.getSerial());
            if (serial > 65535) {
                map.put("status", "-1");
                map.put("msg", "流水号不能大于65535");
                log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            }
        } catch (Exception e) {
            map.put("status", "-1");
            map.put("msg", "流水号需要是数字");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }

        if (StringUtils.isEmpty(remoteUpdateRequest.getSoftVersion())) {
            map.put("status", "-1");
            map.put("msg", "软件版本号为空");
            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
            return map;
        }
//        if (StringUtils.isEmpty(remoteUpdateRequest.getServerIp())) {
//            map.put("status", "-1");
//            map.put("msg", "服务器ip为空");
//            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
//            return map;
//        }
//        if (StringUtils.isEmpty(remoteUpdateRequest.getServerPort())) {
//            map.put("status", "-1");
//            map.put("msg", "服务器端口为空");
//            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
//            return map;
//        }
//        if (StringUtils.isEmpty(remoteUpdateRequest.getUserName())) {
//            map.put("status", "-1");
//            map.put("msg", "用户名为空");
//            log.info("return请求远程升级请求fan:" + JSON.toJSONString(map));
//            return map;
//        }

        return null;

    }

}
