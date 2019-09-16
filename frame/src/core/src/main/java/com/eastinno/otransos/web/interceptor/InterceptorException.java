package com.eastinno.otransos.web.interceptor;

/**
 * 拦截器异常
 * 
 * @author lengyu
 */
public class InterceptorException extends Exception {

    private static final long serialVersionUID = -6542085034837683608L;

    public InterceptorException(String info) {
        super("Interceptor exception info :" + info);
    }
}
