package com.eastinno.otransos.push.server.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.util.internal.ConcurrentHashMap;

/**
 * mina服务端
 * 
 * @author maowei
 * @createDate 2013-12-4下午3:49:48
 */
public class MinaServer {
    // 记录接受数据的次数
    public static int count = 0;
    // 服务器端绑定的端口
    private int port = 9999;

    private static Map<String, IoHandler> map = new ConcurrentHashMap<String, IoHandler>();

    public MinaServer(int port) {
        this.port = port;
    }

    public void run() throws IOException {

        // 创建一个非阻塞的Server端 Socket,用NIO
        IoAcceptor acceptor = new NioSocketAcceptor();
        // 创建接收数据的过滤器
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        // 设定这个过滤器将以对象为单位读取数
        ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        chain.addLast("objectFilter", filter);

        // 设定服务器端的消息处理器:一个ObjectMinaServerHandler对象,
        acceptor.setHandler(new ObjectMinaServerHandler());

        // 绑定端口,启动服务器
        acceptor.bind(new InetSocketAddress(port));

    }

    public static void main(String[] args) throws IOException {
        int port = 9999;
        new MinaServer(port).run();
    }

    /**
     * 对象服务接受处理类
     */
    static class ObjectMinaServerHandler extends IoHandlerAdapter {
        
        @Override
        public void sessionCreated(IoSession session) throws Exception {
            // TODO Auto-generated method stub
            super.sessionCreated(session);
        }

        /**
         * 当一个客户端连接进入时
         */
        @Override
        public void sessionOpened(IoSession session) throws Exception {
            count++;
            System.out.println("客户端成功绑定到服务端  " + count);
        }

        /**
         * 当一个客户端关闭时
         */
        @Override
        public void sessionClosed(IoSession session) throws Exception {
            System.out.println(session.getRemoteAddress() + "客户端关闭");
        }

        /**
         * 当捕获到异常的时候
         */
        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            System.out.println("绑定发生异常^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            super.exceptionCaught(session, cause);
        }

        /**
         * 当客户端 发送 的消息到达时
         */
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
        }

    }
}
