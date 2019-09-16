package com.eastinno.otransos.web;

import org.apache.velocity.app.Velocity;

import junit.framework.TestCase;

public class VelocityTemplatePathTst extends TestCase {
    public void testFilePath() {
        Velocity.setProperty("resource.loader", "file,class,jar");
        Velocity.setProperty("file.resource.loader.path", "D:/disco/disco/webapps/WEB-INF/disco");
        Velocity.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            org.apache.velocity.Template t = Velocity.getTemplate("hello.html");
            org.apache.velocity.Template t2 = Velocity.getTemplate("classpath:cn/disco/web/util.js");
            System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testClassLoader() {
        java.io.InputStream is = this.getClass().getResourceAsStream("/cn/disco/web/disco-web.xml");
        try {
            System.out.println(is.available());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
