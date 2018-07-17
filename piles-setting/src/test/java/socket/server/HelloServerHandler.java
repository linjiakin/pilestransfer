package socket.server;

import java.net.InetAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloServerHandler extends SimpleChannelInboundHandler<byte[]> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

//        ByteBuf result = (ByteBuf) msg;
//        byte[] result1 = new byte[result.readableBytes()];
//        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
//        result.readBytes(result1);
//        String resultStr = new String(result1);
        // 收到消息直接打印输出
        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);
        
        // 返回客户端消息 - 我已经接收到了你的消息
        byte[] bytes= new byte[]{0x1C,0x00,0x02,0x54,0x00,0x17,0x18,0x35,0x02,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x00,0x01,0x02,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x01};
//
//        ByteBuf encoded = ctx.alloc().buffer(result1.length);
//        encoded.writeBytes(result1);
        ctx.writeAndFlush(bytes);
    }
    
    /*
     * 
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     * 
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        
//        ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
        Channel channel = ctx.channel();
        ChannelFuture future = channel.closeFuture().await();
//        future.

        super.channelActive(ctx);
    }
}