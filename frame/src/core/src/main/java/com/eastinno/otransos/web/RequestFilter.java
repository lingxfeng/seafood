package com.eastinno.otransos.web;

/**
 * 访问请求过滤器
 * 
 * @author Disco Framework
 */
public interface RequestFilter {
    /**
     * 执行过滤操作，对特定的对象进行过滤
     * 
     * @param value 要过滤的对象
     * @return 返回过滤处理后的对象
     */
    Object doFilter(Object value);
}
