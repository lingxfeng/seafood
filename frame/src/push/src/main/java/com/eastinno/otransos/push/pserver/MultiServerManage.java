package com.eastinno.otransos.push.pserver;

import java.util.ResourceBundle;

import com.eastinno.otransos.push.nserver.NettyServer;
import com.eastinno.otransos.push.pserver.service.MainServer;

/**
 * 服务器启动调用入口
 * 
 * @author maowei
 * @createDate 2013-10-17下午3:12:36
 */
public class MultiServerManage {
    private static int netty_port = 8500;
    private static int socket_io_port = 8600;
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        netty_port = Integer.parseInt(bundle.getString("netty_port"));
        socket_io_port = Integer.parseInt(bundle.getString("socket_io_port"));
    }

    public static void main(String[] args) {
        MainServer mainServer = new MainServer(socket_io_port);
        mainServer.addNamespace("/fotonpush", new PushHandler());
        mainServer.start();

        NettyServer.start(netty_port);
    }
}