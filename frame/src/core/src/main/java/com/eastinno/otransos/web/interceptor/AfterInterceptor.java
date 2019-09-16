package com.eastinno.otransos.web.interceptor;

import java.lang.reflect.Method;

/**
 * 用来处理后通知
 * 
 * @author lengyu
 */
public interface AfterInterceptor extends Interceptor {
    void after(Object retunValue, Object target, Method method, Object[] args) throws Throwable;
}
