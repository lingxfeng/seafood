package com.eastinno.otransos.container.impl;

import javax.servlet.ServletContext;

/**
 * 处理Web上下文的容器，必须通过传入servletContext来进行初始化
 * 
 * @author lengyu
 */
public class WebContextContainer extends DefaultContainer {
    private ServletContext servletContext;

    public WebContextContainer(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
