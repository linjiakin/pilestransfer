package com.piles.common.websocket.xunDaoServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 循道server端
 */
@Service
public class XunDaoSocketServer implements InitializingBean, DisposableBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	@Value( "${xundao.server.port}" )
	private int port;

	//存放桩编号和ip
	public static ConcurrentHashMap<Integer,Channel> clientMap = new ConcurrentHashMap<>();

	@Resource
	private XunDaoServerChannelInitializer xunDaoServerChannelInitializer;

	public void start(int port) {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(xunDaoServerChannelInitializer)
				.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		try {
			logger.info("===start socket:" + port);
			b.bind(port).sync();
		} catch (InterruptedException e) {
			logger.error("socket InterruptedException.", e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		start(port);
	}
	@Override
	public void destroy() throws Exception {
		// 释放资源
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}

		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
	}

}
