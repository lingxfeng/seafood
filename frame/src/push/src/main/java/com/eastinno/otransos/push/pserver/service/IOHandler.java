package com.eastinno.otransos.push.pserver.service;

import com.eastinno.otransos.push.pserver.transport.IOClient;

/**
 * @author yongboy
 * @time 2012-3-23
 * @version 1.0
 */
public interface IOHandler {
    /**
     * 客户端连接成功时
     * 
     * @param client
     */
    void OnConnect(IOClient client);

    /**
     * 客户端发送消息时
     * 
     * @param client
     * @param oriMessage
     */
    void OnMessage(IOClient client, String oriMessage);

    /**
     * 客户端断开连接时
     * 
     * @param client
     */
    void OnDisconnect(IOClient client);

    /**
     * 服务器关闭时
     */
    void OnShutdown();
}
