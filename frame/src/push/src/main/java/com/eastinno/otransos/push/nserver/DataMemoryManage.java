package com.eastinno.otransos.push.nserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;

import com.eastinno.otransos.push.pserver.transport.IOClient;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据存储器管理
 * 
 * @author maowei
 * @createDate 2013-10-24下午4:45:52
 */
public class DataMemoryManage {
    /**
     * 1、用户连接时保存当前客户端的连接线程到socketioClients集合中<br />
     * 2、连接成功后用户客户端发送用户的参数信息到userArgsList集合中<br />
     * 3、socketio所对应的连接断开时清除socketioClients、userArgsList中所对应的数据<br />
     * 4、netty客户端连接后服务端保存连接信息到nettyClients中<br />
     * 5、netty客户端连接后服务端返回当前userArgsList集合<br />
     * 6、netty客户端断开连接时清除nettyClients中所对应的数据<br />
     * 7、当userArgsList集合有变化后要主动推送此集合信息到nettyClients中所有的客户端去<br />
     * 8、定时任务获取userArgsList中的userId、类别等调用任务，并返回数据给netty client<br />
     * 9、netty client组装数据提交到netty server服务器上<br />
     * 10、netty server根据userId查找对应的sessionId所对应的socket client,推送数据到浏览器
     */
    // key:sessionId value:jsonString
    public static final ConcurrentHashMap<String, String> userArgsList = new ConcurrentHashMap<String, String>();// 保存当前连接用户所对应的参数信息
    public static final ConcurrentHashMap<String, IOClient> socketioClients = new ConcurrentHashMap<String, IOClient>();// 保存当前socketio连接所对应的客户端对象
    public static Set<Channel> nettyClients = Collections.synchronizedSet(new HashSet<Channel>());// 保存当前NETTY服务器所对应的连接客户端

    /**
     * 同步数据池
     */
    public static void removeUserInfo(IOClient client) {
        String sessionId = client.getSessionID();
        userArgsList.remove(sessionId);// 删除对应的用户参数信息
        pushUserListToNettyClient(sessionId, null);
    }

    /**
     * 添加一个sessionId对应的用户信息及参数信息到集合中
     * 
     * @param sessionId
     * @param jsonStr 格式:{userId:'1',types:[1,2,3]}
     */
    public static void putUserInfo(String sessionId, String jsonStr) {
        userArgsList.put(sessionId, jsonStr);
        pushUserListToNettyClient(sessionId, jsonStr);
    }

    /**
     * 根据UserId查找对应的sessionId
     * 
     * @param userId
     * @return
     */
    public static List<String> findSessionIdByUserId(String userId) {
        List<String> sessionIds = new ArrayList<String>();
        if (userArgsList != null && userArgsList.size() > 0) {
            Iterator<Entry<String, String>> it = userArgsList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                String sessionId = entry.getKey();
                String userInfo = entry.getValue();// user对应的推送JSON信息
                JSONObject jsonObj = JSONObject.parseObject(userInfo);
                String uid = jsonObj.getString(userId);
                if (StringUtils.isNotBlank(uid)) {// userId存在此集合中则获取对应的sessionId
                    sessionIds.add(sessionId);
                }
            }
        }
        return sessionIds;
    }

    private static void pushUserListToNettyClient(String socketioId, String jsonStr) {
        if (nettyClients != null && nettyClients.size() > 0) {
            Iterator<Channel> it = nettyClients.iterator();// 所有netty客户端
            while (it.hasNext()) {
                Channel channel = it.next();
                if (channel != null && channel.isOpen()) {
                    UserArgs userArgs = new UserArgs();
                    userArgs.setSocketId(socketioId);
                    if (jsonStr == null) {// 表明是删除一个用户参数
                        userArgs.setStatus(new Short("0"));
                    } else {
                        userArgs.setArgs(jsonStr);
                    }
                    channel.write(userArgs);
                } else {
                    it.remove();
                }
            }
        }
    }

}
