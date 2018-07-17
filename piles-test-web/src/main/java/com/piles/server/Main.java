package com.piles.server;


import com.alibaba.fastjson.JSON;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.control.entity.RemoteStartPushRequest;
import com.piles.control.service.IRemoteStartPushService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static final String SRPING_ROOT_XML = "classpath:application-record.xml";


    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(SRPING_ROOT_XML)) {
            context.registerShutdownHook();
            context.start();


            LOGGER.info("启动成功");

            CountDownLatch countDownLatch = new CountDownLatch(1);
            countDownLatch.await();

        } catch (Exception e) {
            LOGGER.error("Spring 启动错误", e);
        }

    }
}
