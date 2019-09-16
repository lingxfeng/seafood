package com.eastinno.otransos.push.client.test;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * mina测试客户端
 * 
 * @author maowei
 * @createDate 2013-12-4下午3:46:08
 */
public class MinaClient {
    // 服务器端绑定的端口
    private int port = 9999;

    public static int errorCount = 0;

    public MinaClient(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 9999;
        String count = (String) args[0];
        new MinaClient(port).run(Integer.parseInt(count));
    }

    public void run(Integer count) {
        // Create TCP/IP connector.
        final IoConnector connector = new NioSocketConnector();
        // 创建接收数据的过滤器
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
        // 设定这个过滤器将以对象为单位读取数
        ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        chain.addLast("objectFilter", filter);
        // 设定客户端的消息处理器:一个ObjectMinaClientHandler对象,
        connector.setHandler(new ObjectMinaClientHandler());

        // for (int i = 0; i < 5000; i++) {
        // // 连结到服务器:
        // connector.connect(new InetSocketAddress("localhost", port));
        // }
        for (int i = 0; i < count; i++) {
            connector.connect(new InetSocketAddress("172.24.176.111", port));
            Thread thread = Thread.currentThread();
            // try {
            // thread.sleep(1000);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // 连接到本地的8000端口的服务端
            // Thread t = new Thread(new Runnable() {
            // @Override
            // public void run() {
            // connector.connect(new InetSocketAddress("172.24.176.111", port));
            // }
            // });
            // t.start();
        }
    }

    private static class ObjectMinaClientHandler extends IoHandlerAdapter {
        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            errorCount++;
            System.out.println(errorCount + "客户端绑定发生异常" + cause.getCause());
        }

        // 当一个服务端连结进入时
        @Override
        public void sessionOpened(IoSession session) throws Exception {
        }

        // 当一个服务端关闭时
        @Override
        public void sessionClosed(IoSession session) {
            System.out.println("客户端连接断开********************");
        }

        // 当服务器发送的消息到达时:
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {

        }

    }
}
