package com.wemall.apis.jdutil;

/**
 * Created by zhangjun on 2017-07-16.
 */
public class AccessToken {
    private String token;
    private int expiresIn;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getExpiresIn() {
        return expiresIn;
    }
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
