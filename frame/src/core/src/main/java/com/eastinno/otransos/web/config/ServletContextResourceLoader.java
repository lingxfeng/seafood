package com.eastinno.otransos.web.config;

import java.io.InputStream;

import javax.servlet.ServletContext;

public class ServletContextResourceLoader extends AbstractResourceLoader {
    private ServletContext servletContext;

    public ServletContextResourceLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public InputStream load(String name) {
        return servletContext.getResourceAsStream(name);
    }

}
