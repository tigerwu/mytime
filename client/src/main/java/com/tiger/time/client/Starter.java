package com.tiger.time.client;

import com.tiger.time.common.NettyConstant;

/**
 * Created by wuyihao on 17-3-3.
 */
public class Starter {
    public static void main(String[] args) throws Exception {
        new TimeClient().connect(NettyConstant.REMOTEIP, NettyConstant.PORT);
    }
}
