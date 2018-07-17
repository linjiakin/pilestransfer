package socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {

//    public static String host = "127.0.0.1";
    public static String host = "59.110.170.111";
    public static int port = 8080;
    public static LinkedBlockingQueue queue = new LinkedBlockingQueue();
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());

            // 连接服务端
            Channel ch = b.connect(host, port).sync().channel();
            byte[] msg= new byte[]{0x68,0x01,0x00,0x00,0x00,0x1d,0x00,0x00,0x00,0x00,(byte)0x80,0x00,0x00,0x04,0x01,0x02,0x00,0x00,0x00,0x00,0x12,0x34,0x56,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                    0x00,(byte)0xc9,0x7e};

            ch.writeAndFlush(msg);
            while (true){
                Thread.sleep(3000L);
                msg= new byte[]{0x68,0x0c,0x00,0x00,0x00,0x03,0x02,0x00,0x01,(byte)0xf4,0x2a};

                ch.writeAndFlush(msg);

            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
