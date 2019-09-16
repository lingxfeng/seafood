package com.eastinno.otransos.web.interceptor;

import java.lang.reflect.Method;

/**
 * 处理连接点前通知
 * 
 * @author lengyu　
 */
public interface BeforeInterceptor extends Interceptor {
    void before(Object target, Method method, Object[] args) throws Throwable;
}
