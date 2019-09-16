package com.eastinno.otransos.web.interceptor;

/**
 * 用来处理环绕通知
 * 
 * @author lengyu
 */
public interface AroundInterceptor extends Interceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
