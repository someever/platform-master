package com.fanfandou.platform.serv.game.connection.netty;

import com.fanfandou.platform.serv.game.connection.codec.ConnectionCodec;
import com.fanfandou.platform.api.game.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Description: 协议适配器.
 * <p/>
 * Date: 2016-05-19 11:03.
 * author: Arvin.
 */
public class NettyCodecAdatpter {

    private final ChannelOutboundHandler encoder = new InternalEncoder();

    //private  ChannelInboundHandler decoder = null;

    private ConnectionCodec<Message> codec;


    //初始化
    public NettyCodecAdatpter(ConnectionCodec<Message> codec) {
        this.codec = codec;
        /*decoder = new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, codec.getLenthFieldOffset(),
                codec.getlenthFieldLenth(), codec.getlenthAdjustment() ,codec.getinitialBytesToTrip(), true);*/
    }

    private class InternalEncoder extends MessageToByteEncoder<Message> {
        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext,
                              Message msg, ByteBuf byteBuf) throws Exception {
            byteBuf.writeBytes(codec.encode(msg));

        }
    }

    public ChannelOutboundHandler getEncoder() {
        return encoder;
    }

    /*public ChannelInboundHandler getDecoder() {
        return decoder;
    }*/
}
