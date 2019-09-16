package com.eastinno.otransos.push.nserver;

import java.io.Serializable;

/**
 * socket传递的user对象相应参数封装
 * 
 * @author maowei
 * @createDate 2013-10-31上午8:54:33
 */
public class UserArgs implements Serializable {
    private static final long serialVersionUID = 1L;
    private String socketId;// socket session id
    private String args;// user json arguments string
    private Short status = 1;// 1:新增 0删除

    public UserArgs() {
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

}