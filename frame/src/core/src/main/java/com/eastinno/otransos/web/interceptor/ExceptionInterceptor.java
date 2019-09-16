package com.eastinno.otransos.web.interceptor;

import java.lang.reflect.Method;

/**
 * 异常处理拦截器，通过这个拦截器来封装系统异常处理
 * 
 * @author lengyu
 */
public interface ExceptionInterceptor extends Interceptor {
    /**
     * @param e 出现的异常
     * @param target 异常出现的对象
     * @param method 异常抛出时调用的方法
     * @param args 相关参数
     * @return 返回true，则表示异常已经成功处理，不再需要作其它的处理，返回false则，表示把异常交给下一级异常处理器进行处理，如果抛出异常，则表示将直接把异常交给外部程序或发给用户。
     */
    boolean handle(Throwable e, Object target, Method method, Object[] args) throws Exception;
}
