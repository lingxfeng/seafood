package com.eastinno.otransos.web.interceptor;

import java.lang.reflect.Method;

/**
 * 方法调用连接点
 * 
 * @author lengyu
 */
public interface MethodInvocation extends JoinPoint {
    Object[] getArguments();

    Method getMethod();
}
