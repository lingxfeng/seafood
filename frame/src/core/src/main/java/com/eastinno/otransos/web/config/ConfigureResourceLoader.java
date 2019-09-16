package com.eastinno.otransos.web.config;

import java.io.InputStream;

/**
 * 系统中的资源加载器
 * 
 * @author maowei
 * @createDate 2014-1-16下午3:54:31
 */
public interface ConfigureResourceLoader {
    /**
     * 加载资源
     * 
     * @param name
     * @return
     */
    InputStream loadResource(String name);
}
