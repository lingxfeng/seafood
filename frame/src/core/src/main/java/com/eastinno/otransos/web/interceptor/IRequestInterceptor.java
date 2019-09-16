package com.eastinno.otransos.web.interceptor;

/**
 * RequestInterceptor主要是拦截用户的request请求，若返回SUCCESS则继续执行， 返回Page，则跳转到相应的Page中执行 若抛出Exception则直接使用
 * 
 * @author lengyu
 */
public interface IRequestInterceptor {
    public final String SUCCESS = "SUCCESS";

    public Object doIntercept() throws Exception;
}
