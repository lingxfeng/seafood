package com.eastinno.otransos.web.config;

import java.util.Iterator;

import com.eastinno.otransos.core.util.ResolverUtil;
import com.eastinno.otransos.web.IWebAction;
import com.eastinno.otransos.web.config.DefaultWebConfig;
import com.eastinno.otransos.web.config.FileResourceLoader;

import junit.framework.TestCase;

public class DefaultConfigTest extends TestCase {
    public void testLoadConfig() {
        // System.out.println(new File(".").getAbsolutePath());
        String[] confs = new String[] {"classpath:/cn/disco/web/config/mvc-app.xml"};
        DefaultWebConfig config = new DefaultWebConfig();
        config.setConfigures(confs);
        config.setResourceLoader(new FileResourceLoader());
        config.init();
        // assertTrue(config.getModules().size()==2);
    }

    public void testScanPackage() {
        ResolverUtil<IWebAction> r = new ResolverUtil();
        r.findImplementations(IWebAction.class, "com");
        Iterator it = r.getClasses().iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        // Thread.currentThread().getContextClassLoader().loadClass("");
    }
}
