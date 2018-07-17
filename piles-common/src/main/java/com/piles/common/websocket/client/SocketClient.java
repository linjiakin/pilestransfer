package com.piles.common.websocket.client;

import com.piles.common.entity.ClientRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;


//@Service
public class SocketClient implements InitializingBean, DisposableBean {

    public static String host = "127.0.0.1";
    public static int port = 9901;
    public LinkedBlockingQueue<ClientRequest> queue = new LinkedBlockingQueue();

    private Logger logger = LoggerFactory.getLogger(getClass());
    private EventLoopGroup group = null;

    @Autowired
    private ClientChannelInitializer clientChannelInitializer;

    public void start() {
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(clientChannelInitializer);

        try {
            Channel ch = b.connect(host, port).sync().channel();
            for (; ; ) {
                ClientRequest request = queue.take();

                if (request == null || request.getMsg() == null) {
                    continue;
                }
                byte[] msg = request.getMsg();

                ch.writeAndFlush(msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void destroy() throws Exception {
        // 释放资源
        if (group != null) {
            group.shutdownGracefully();
        }
    }
}
