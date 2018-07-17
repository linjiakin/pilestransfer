package socket.client;


import com.google.common.primitives.Bytes;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
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

        System.out.println("CRC  校验"+CRC16Util.checkMsg( bytes ));
        switch (bytes[1]){

            case 0x07 :
                //远程结束充电
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x87,0x00,0x00,0x00,0x09,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x02,0x76} );
                break;

            case 0x06 :
                byte byte1=bytes[2];
                byte byte2=bytes[3];
                byte order14=bytes[14];
                byte order15=bytes[15];
                byte order16=bytes[16];
                byte order17=bytes[17];
                byte order18=bytes[18];
                byte order19=bytes[19];
                byte order20=bytes[20];
                byte order21=bytes[21];
//                远程启动充电
                byte[] temp2=new byte[]{(byte)0x86,byte1,byte2,0x00,0x09,order14,order15,order16,order17,order18,order19,order20,order21,0x00};

                int crcInt = CRC16Util.getCRC(temp2);
                byte[] crc=BytesUtil.intToBytes(crcInt);
                byte[] newByte=Bytes.concat(new byte[]{0x68},temp2,crc);
                channelHandlerContext.channel().writeAndFlush(newByte );
break;
            //校时
            case 0x0E :
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x8E,0x00,0x00,0x00,0x01,0x01,0x77,(byte)0x93} );
                break;

            //重启
            case 0x1D :
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x9D,0x00,0x00,0x00,0x01,0x01,(byte)0xa3,(byte)0xf7} );
                break;

                //远程升级
                case 0x1E :
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x9E,0x00,0x00,0x00,0x01,0x01,0x6d,0x17} );
                    break;

                //计费规则设置
                case 0x0B :
                channelHandlerContext.writeAndFlush( new byte[]{0x68,(byte)0x8B,0x00,0x00,0x00,0x09,0x00,0x00,0x00,0x04,0x00,0x00,0x00,0x01,0x00,0x21,0x28} );
                    break;


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
