package com.tiger.time.server.handler;

import com.tiger.time.common.MessageType;
import com.tiger.time.common.struct.Header;
import com.tiger.time.common.struct.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by wuyihao on 17-3-3.
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("HeartBeatRespHandler channelRead, object->[" + msg + "]" );
        NettyMessage message = (NettyMessage)msg;

        if (message.getHeader() != null &&
                message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Receive client heart beat message : ---> " + message );
            NettyMessage heardBeat = buildHeartBeat();
            System.out.println("Send heart beat response message to client : ---> " + heardBeat);
            ctx.writeAndFlush(heardBeat);

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
