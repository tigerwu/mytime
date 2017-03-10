package com.tiger.time.server;

import com.tiger.time.common.NettyConstant;

/**
 * Created by wuyihao on 17-2-27.
 */
public class Starter {
    public static void main(String[] args) throws Exception {
        String host = NettyConstant.REMOTEIP;
        int port = NettyConstant.PORT;
        if (args != null && args.length > 0)

        {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                port = NettyConstant.PORT;
            }
        }

        TimeServer timeserver = new TimeServer();
        timeserver.bind(host, port);
    }
}
