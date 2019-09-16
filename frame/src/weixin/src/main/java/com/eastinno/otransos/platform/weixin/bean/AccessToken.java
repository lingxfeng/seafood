package com.eastinno.otransos.platform.weixin.bean;

import java.io.Serializable;

public class AccessToken implements Serializable {
    private String token;
    private int expiresIn;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return this.expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}