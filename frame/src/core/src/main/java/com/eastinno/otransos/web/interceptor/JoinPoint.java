package com.eastinno.otransos.web.interceptor;

/**
 * 连接点
 * 
 * @author lengyu
 */
public interface JoinPoint {
    Object getTarget();

    Object proceed() throws Throwable;
}
