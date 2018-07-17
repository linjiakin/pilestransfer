package socket.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        String temp="";
        for (byte b:bytes){
           temp+=" "+ Integer.toHexString(Byte.toUnsignedInt(b));
        }
        System.out.println("Server say : " + temp);
        switch (bytes[1]){
            case 0x07 :
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x87,0x00,0x00,0x00,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x02,0x76} );

            case 0x06 :
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x86,0x00,0x00,0x00,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x69,0x36} );
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }


}
