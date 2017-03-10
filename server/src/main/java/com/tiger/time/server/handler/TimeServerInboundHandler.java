package com.tiger.time.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuyihao on 17-2-27.
 */
public class TimeServerInboundHandler extends ChannelInboundHandlerAdapter  {
    private static Logger logger  = LoggerFactory.getLogger(TimeServerInboundHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8").trim();
        String body = (String)msg;
        logger.info("The time server receive order : " + body);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?
                new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        System.out.println(currentTime);
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
