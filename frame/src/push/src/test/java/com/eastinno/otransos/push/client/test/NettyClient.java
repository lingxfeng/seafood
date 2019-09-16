package com.eastinno.otransos.push.client.test;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

/**
 * mina测试客户端
 * 
 * @author maowei
 * @createDate 2013-12-4下午3:46:08
 */
public class NettyClient {
    static Long startDate;
    static Long endDate;

    // 服务器端绑定的端口
    private int port = 9988;

    public static int errorCount = 0;

    public NettyClient(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 9999;
        String count = (String) args[0];
        new NettyClient(port).run(Integer.parseInt(count));
    }

    public void run(Integer count) {
        ExecutorService es = Executors.newCachedThreadPool();
        final ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(es, es));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline channelPipeline = Channels.pipeline();
                ObjectDecoder objectDecoder = new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass()
                        .getClassLoader()));
                // channelPipeline.addLast("decoder", objectDecoder);
                // channelPipeline.addLast("encoder", new ObjectEncoder());
                channelPipeline.addLast("handler", new NettyClientHandler());
                return channelPipeline;
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        // for (int i = 0; i < 4000; i++) {
        // bootstrap.connect(new InetSocketAddress("172.24.176.112", port));
        // }

        for (int i = 0; i < count; i++) {
            // 连接到本地的8000端口的服务端
            try {
                bootstrap.connect(new InetSocketAddress("172.24.176.111", port));
                Thread thread = Thread.currentThread();
                thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            // Thread t = new Thread(new Runnable() {
            // @Override
            // public void run() {
            // }
            // });
            // t.start();
        }
        System.out.println("测试完成");

    }

    private static class NettyClientHandler extends SimpleChannelHandler {
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            errorCount++;
            System.out.println(errorCount + "客户端绑定发生异常" + e.getCause());
        }

        /**
         * 当绑定到服务端的时候触发
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws UnknownHostException {
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        }

        // 服务端返回数据
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        }
    }
}
