package com.eastinno.otransos.push.nclient;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class NettyClient {
    protected static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static ChannelFuture cf = null;
    private static ResourceBundle bundle = null;

    NettyClient() {
        bundle = this.readConfig();
    }

    public static void main(String args[]) throws InterruptedException {
        bundle = readConfig();
        // 实例化一个客户端Bootstrap实例，其中NioClientSocketChannelFactory实例由Netty提供
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        // 设置一个处理服务端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline channelPipeline = Channels.pipeline();
                ObjectDecoder objectDecoder = new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass()
                        .getClassLoader()));
                channelPipeline.addLast("decoder", objectDecoder);
                channelPipeline.addLast("encoder", new ObjectEncoder());
                channelPipeline.addLast("handler", new NettyClientHandler());
                return channelPipeline;
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        // 向目标地址发起一个连接
        ChannelFuture cfu = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8500));
        cf = cfu;
    }

    private static class NettyClientHandler extends SimpleChannelHandler {
        /**
         * 当绑定到服务端的时候触发
         * 
         * @alia OneCoder
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            String remoteIp = "127.0.0.1";
            String localIp = InetAddress.getLocalHost().getHostAddress();
            logger.debug("client ip: " + localIp + " ################# netty server ip: " + remoteIp
                    + " Successfully connected.");
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            Object obj = (Object) e.getMessage();
            if (obj instanceof ConcurrentHashMap) {
                @SuppressWarnings("unchecked")
                ConcurrentHashMap<String, String> userList = (ConcurrentHashMap<String, String>) obj;
                System.out.println(userList);
               /* DataMemoryManage.userArgsList.clear();
                DataMemoryManage.userArgsList.putAll(userList);*/

                // 发送数据给服务端
                Map<String, String> data = new HashMap<String, String>();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("name", "maowei");
                jsonObj.put("sex", "男女");
                data.put(userList.keys().nextElement(), jsonObj.toJSONString());
                e.getChannel().write(data);
            }

        }
    }

    static class User implements Serializable {
        private String id;
        private String userName;

        public User(String id, String userName) {
            this.id = id;
            this.userName = userName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }

    public static ResourceBundle readConfig() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("application");
        }
        return bundle;
    }

    public static ChannelFuture getCf() {
        return cf;
    }

    public static void setCf(ChannelFuture cf) {
        NettyClient.cf = cf;
    }
}