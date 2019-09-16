package com.eastinno.otransos.email;

import java.util.Properties;

import com.eastinno.otransos.core.service.IConfigService;
import com.eastinno.otransos.core.service.impl.ConfigServiceImpl;

import junit.framework.TestCase;

public class ConfigServiceTest extends TestCase {
    public void testLoadDatabaseConfig() {
        IConfigService service = new ConfigServiceImpl();
        Properties p = service.getApplicationConfig();
        System.out.println(p.getProperty("email.domain"));
        p.setProperty("email.domain", "disco.cn");
        service.updateApplicationConfig(p);
    }

    public void testSaveDatabaseConfig() {
        IConfigService service = new ConfigServiceImpl();
        Properties p = service.getDatabaseConfig();
        System.out.println(p.getProperty("database.url"));

        service.updateDatabaseConfig(p);
    }

    public void testConnectionDatabase() {
        IConfigService service = new ConfigServiceImpl();
        service.checkConnection(service.getDatabaseConfig());
    }
}
