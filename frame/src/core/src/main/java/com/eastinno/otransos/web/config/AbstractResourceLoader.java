package com.eastinno.otransos.web.config;

import java.io.InputStream;

public abstract class AbstractResourceLoader implements ConfigureResourceLoader {

    public InputStream loadResource(String name) {
        if (name.indexOf("classpath:") == 0) {
            return this.getClass().getResourceAsStream(name.substring("classpath:".length()));
        } else
            return load(name);
    }

    public abstract InputStream load(String name);
}
