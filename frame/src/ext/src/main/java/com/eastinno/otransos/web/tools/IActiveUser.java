package com.eastinno.otransos.web.tools;

/**
 * 当前用户类
 * 
 * @author lengyu
 */
public interface IActiveUser {
    /**
     * 获取用户名
     * 
     * @return 返回当前操作的用户
     */
    String getUserName();

    /**
     * 攻取用户ip
     * 
     * @return 当前用户的ip地址
     */
    String getIp();
}
