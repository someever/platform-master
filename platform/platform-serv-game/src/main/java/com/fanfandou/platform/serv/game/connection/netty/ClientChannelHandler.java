package com.fanfandou.platform.serv.game.connection.netty;

import com.fanfandou.platform.api.game.entity.Message;
import com.fanfandou.platform.serv.game.connection.codec.ConnectionCodec;
import com.fanfandou.platform.serv.game.operation.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Description: 处理回调信息.
 * <p/>
 * Date: 2016-05-04 17:35.
 * author: Arvin.
 */
@ChannelHandler.Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MessageHandler messageHandler;
    private NettyGameConnector nettyGameConnector;
    private ConnectionCodec<Message> codec;

    public ClientChannelHandler(MessageHandler messageHandler, ConnectionCodec codec, NettyGameConnector nettyGameConnector) {
        this.messageHandler = messageHandler;
        this.nettyGameConnector = nettyGameConnector;
        this.codec = codec;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] datas = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(datas);
        Message message = codec.decode(datas);
        messageHandler.addResponse(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        nettyGameConnector.setConnected(false);
        logger.info("ClientChannelHandler -> channel inactive, 将在三秒后重连......");
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                nettyGameConnector.doConnect();
            }
        }, 3, TimeUnit.SECONDS);

    }
}
