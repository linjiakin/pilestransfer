package socket.client;


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
        switch (bytes[1]){
                //心跳回复
            case (byte)0x8C :
                testHearBeatResponse( bytes);
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

    public void testHearBeatResponse(byte[] msg){
        byte[] bodyBytes = testBaseReponse(msg);
        String s = BytesUtil.bcd2Str(bodyBytes);
        System.out.println("心跳返回的报文体为:"+s);

    }

    public byte[] testBaseReponse(byte[] msg){
        if (CRC16Util.checkMsg( msg )){
            if(msg.length<=8){
                System.out.println("消息头小于8");
                return null;
            }
            byte[] lenBytes = BytesUtil.copyBytes(msg, 4, 2);
            //消息体长度
            int len = BytesUtil.bytesToInt(lenBytes, 0);
            if(msg.length!=(len+8)){
                System.out.println("消息体长度与消息头中标记的长度不一致");
                return null;
            }
            //命令码
            byte[] commanBytes = BytesUtil.copyBytes(msg, 1, 1);
            String comman = BytesUtil.binary(commanBytes, 16);
            System.out.println("命令码:"+comman);
            //消息流水号
            byte[] orderBytes = BytesUtil.copyBytes(msg, 2, 2);
            int order = BytesUtil.bytesToInt(orderBytes, 0);
            System.out.println("消息流水号为:"+order);

            byte[] bodyBytes = BytesUtil.copyBytes(msg, 6, len);
            return bodyBytes;
        }else{
            System.out.println("crc校验出错");
            return null;
        }
    }


}
