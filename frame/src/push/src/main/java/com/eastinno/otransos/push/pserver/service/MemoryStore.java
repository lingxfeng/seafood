package com.eastinno.otransos.push.pserver.service;

import java.util.Collection;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.eastinno.otransos.push.nserver.DataMemoryManage;
import com.eastinno.otransos.push.pserver.transport.BlankIO;
import com.eastinno.otransos.push.pserver.transport.IOClient;

/**
 * @author yongboy
 * @time 2012-3-29
 * @version 1.0
 */
public class MemoryStore implements Store {
    @Override
    public IOClient get(String sessionId) {
        IOClient client = DataMemoryManage.socketioClients.get(sessionId);

        if (client == null)
            return client;

        if (client instanceof BlankIO)
            return null;

        return client;
    }

    /*
     * (non-Javadoc)
     * @see com.yongboy.socketio.Store#remove(java.lang.String)
     */
    @Override
    public void remove(String sessionId) {
        DataMemoryManage.socketioClients.remove(sessionId);
    }

    /*
     * (non-Javadoc)
     * @see com.yongboy.socketio.Store#add(java.lang.String, com.yongboy.socketio.transport.IOClient)
     */
    @Override
    public void add(String sessionId, IOClient client) {
        if (sessionId == null || client == null)
            return;
        DataMemoryManage.socketioClients.put(sessionId, client);
    }

    /*
     * (non-Javadoc)
     * @see com.yongboy.socketio.Store#getClients()
     */
    @Override
    public Collection<IOClient> getClients() {
        return DataMemoryManage.socketioClients.values();
    }

    @Override
    public boolean checkExist(String sessionId) {
        return DataMemoryManage.socketioClients.containsKey(sessionId);
    }

    @Override
    public IOClient getByCtx(ChannelHandlerContext ctx) {
        if (ctx == null)
            return null;

        for (IOClient client : getClients()) {
            if (ctx == client.getCTX()) {
                return client;
            }
        }

        return null;
    }
}