package com.eastinno.otransos.push.server.test;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.util.internal.ConcurrentHashMap;

/**
 * mina服务端
 * 
 * @author maowei
 * @createDate 2013-12-4下午3:49:48
 */
public class NettyServer {
    // 记录接受数据的次数
    public static int count = 0;
    // 服务器端绑定的端口
    private int port = 9988;

    private static Map<String, ChannelHandler> map = new ConcurrentHashMap<String, ChannelHandler>();

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() {
        // Server服务启动器
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        // 设置一个处理客户端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline channelPipeline = Channels.pipeline();
                ObjectDecoder objectDecoder = new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass()
                        .getClassLoader()));
                channelPipeline.addLast("decoder", objectDecoder);
                channelPipeline.addLast("encoder", new ObjectEncoder());
                channelPipeline.addLast("handler", new NettyServerHandler());
                return channelPipeline;
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.bind(new InetSocketAddress(port));
    }

    public static void main(String[] args) {
        int port = 9988;
        new NettyServer(port).run();
    }

    private static class NettyServerHandler extends SimpleChannelHandler {

        /*
         * 当有客户端绑定到服务端的时候触发
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            count++;
            System.out.println("客户端成功绑定到服务端  " + count);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println("绑定发生异常^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            super.exceptionCaught(ctx, e);
        }

        /**
         * 连接关闭时触发
         */
        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        }

        /**
         * netty客户端 发送过来信息后 将触发此方法
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        }

    }
}
