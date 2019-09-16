package com.eastinno.otransos.push.nserver;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.eastinno.otransos.push.pserver.transport.IOClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Netty 服务端代码
 * 
 * @author maowei
 * @createDate 2013-10-15下午1:17:37
 */
public class NettyServer {
    private static final Logger log = Logger.getLogger(NettyServer.class);

    public static void start(Integer port) {
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
        // ServerBootstrap对象的bind方法返回了一个绑定了本地地址的服务端Channel通道对象
        bootstrap.bind(new InetSocketAddress(port));
        log.info("start the netty with port : " + port);
    }

    private static class NettyServerHandler extends SimpleChannelHandler {

        /*
         * 当有客户端绑定到服务端的时候触发
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            Channel channel = e.getChannel();
            DataMemoryManage.nettyClients.add(channel);
            channel.write(DataMemoryManage.userArgsList);
        }

        /**
         * 连接关闭时触发
         */
        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            DataMemoryManage.nettyClients.remove(e.getChannel());
        }

        /**
         * netty客户端 发送过来信息后 将触发此方法
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            Object obj = e.getMessage();
            if (obj == null) {
                return;
            }
            if (obj instanceof HashMap) {
                this.parseLogicData(obj);
            }
        }

        /**
         * 处理推送到NETTY服务端的业务数据
         * 
         * @param obj
         */
        private void parseLogicData(Object obj) {
            @SuppressWarnings("unchecked")
            HashMap<String, String> userData = (HashMap<String, String>) obj;
            if (userData.size() > 0) {
                Iterator<Entry<String, String>> it = userData.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    String sessionId = entry.getKey();//
                    String value = entry.getValue();// sessionId对应的需推送的JSON数据
                    IOClient socketClient = DataMemoryManage.socketioClients.get(sessionId);
                    if (!socketClient.isOpen()) {
                        DataMemoryManage.socketioClients.remove(sessionId);
                        log.debug(socketClient.getSessionID() + "对应的socket客户端连接丢失，本次无法推送数据");
                        continue;
                    }
                    if (!sessionId.equals(socketClient.getSessionID())) {
                        log.debug(sessionId + " " + socketClient.getSessionID() + " 两个sessionID不相匹配");
                        continue;
                    }

                    JSONArray arrayObj = new JSONArray();
                    arrayObj.add("");
                    arrayObj.add(value);
                    JSONObject content = new JSONObject();
                    content.put("name", "user message");
                    content.put("args", arrayObj);
                    socketClient.send(content.toJSONString());
                }
            }
        }
    }
}
