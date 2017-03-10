package com.tiger.time.client;

import com.tiger.time.client.handler.LoginAuthReqHandler;
import com.tiger.time.common.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tiger.time.common.codec.NettyMessageDecoder;
import com.tiger.time.common.codec.NettyMessageEncoder;
import com.tiger.time.client.handler.HeartBeatReqHandler;

/**
 * Created by wuyihao on 17-3-3.
 */
public class TimeClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) throws Exception {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildChannelHandler());

            System.out.println("connect Remote " + host + ":" + port);
            ChannelFuture future = b.connect(
                    new InetSocketAddress(host, port),
                    new InetSocketAddress(
                            NettyConstant.LOCAL_IP,
                            NettyConstant.LOCAL_PORT));
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            connect(NettyConstant.REMOTEIP, NettyConstant.PORT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
            socketChannel.pipeline().addLast(new NettyMessageEncoder());
            socketChannel.pipeline().addLast("readTimeoutHandler",
                    new ReadTimeoutHandler(50));
            socketChannel.pipeline().addLast("LoginAuthReqHandler",
                    new LoginAuthReqHandler());
            socketChannel.pipeline().addLast("HeartBeatReqHandler",
                    new HeartBeatReqHandler());
        }
    }
}
