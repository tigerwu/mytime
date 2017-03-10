package com.tiger.time.server;

import com.tiger.time.common.codec.NettyMessageDecoder;
import com.tiger.time.common.codec.NettyMessageEncoder;
import com.tiger.time.server.handler.HeartBeatRespHandler;
import com.tiger.time.server.handler.LoginAuthRespHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by wuyihao on 17-2-27.
 */
public class TimeServer {
    public void bind(String host, int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChildChannelHandler());


            ChannelFuture f = b.bind(host, port).sync();
            System.out.println(TimeServer.class.getName() + "started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
            socketChannel.pipeline().addLast(new NettyMessageEncoder());
            socketChannel.pipeline().addLast("readTimeoutHandler",
                    new ReadTimeoutHandler(50));
            socketChannel.pipeline().addLast("LoginAuthRespHandler",
                    new LoginAuthRespHandler());
            socketChannel.pipeline().addLast("HeartBeatRespHandler",
                    new HeartBeatRespHandler());
        }
    }
}
