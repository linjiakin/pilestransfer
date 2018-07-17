package com.piles.common.websocket.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lgc48027
 * @version Id: StartAndLenthFieldFrameDecoder, v 0.1 2018/5/15 11:54 lgc48027 Exp $
 */
public class StartAndLenthFieldFrameDecoder extends ByteToMessageDecoder {
    private  int HEAD_DATA;


    public StartAndLenthFieldFrameDecoder(int HEAD_DATA) {
        this.HEAD_DATA = HEAD_DATA;
    }

    /**
     * <pre>
     * 协议开始的标准head_data，int类型，占据1个字节.
     * 表示数据的长度contentLength，int类型，占据1个字节.
     * </pre>
     */
    public final int BASE_LENGTH = 1 + 1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
                          List<Object> out) throws Exception {
        // 可读长度必须大于基本长度
        if (buffer.readableBytes() >= BASE_LENGTH) {
            // 防止socket字节流攻击
            // 防止，客户端传来的数据过大
            // 因为，太大的数据，是不合理的
//            if (buffer.readableBytes() > 2048) {
//                buffer.skipBytes(buffer.readableBytes());
//            }

            // 记录包头开始的index
            int beginReader;

            while (true) {
                // 获取包头开始的index
                beginReader = buffer.readerIndex();
                // 标记包头开始的index
                buffer.markReaderIndex();
                // 读到了协议的开始标志，结束while循环
                if (buffer.getUnsignedByte( beginReader ) == HEAD_DATA) {
                    break;
                }

                // 未读到包头，略过一个字节
                // 每次略过，一个字节，去读取，包头信息的开始标记
                buffer.resetReaderIndex();
                buffer.readByte();

                // 当略过，一个字节之后，
                // 数据包的长度，又变得不满足
                // 此时，应该结束。等待后面的数据到达
                if (buffer.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            // 消息的长度

            int length = buffer.getUnsignedByte( beginReader+1 );
            // 判断请求数据包数据是否到齐
            if (buffer.readableBytes() < length) {
                // 还原读指针
                buffer.readerIndex(beginReader);
                return;
            }

            // 读取data数据
            byte[] data = new byte[length];
            buffer.readBytes(data);
            ByteBuf frame= buffer.retainedSlice(beginReader, length+2);
            buffer.readerIndex(beginReader+length + 2);
            out.add(frame);
        }
    }

}
