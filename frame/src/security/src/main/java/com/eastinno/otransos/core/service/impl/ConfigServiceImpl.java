package com.eastinno.otransos.core.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.exception.LogicException;
import com.eastinno.otransos.core.service.IConfigService;

@Service
public class ConfigServiceImpl implements IConfigService {
    private String databaseConfig = "/db.properties";
    private String applicationConfig = "/application.properties";
    private static final Logger logger = Logger.getLogger(ConfigServiceImpl.class);

    public boolean checkConnection(Properties p) {
        String driver = p.getProperty("database.driverClassName");
        String connStr = p.getProperty("database.url");
        String userName = p.getProperty("database.username");
        String password = p.getProperty("database.password");
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connStr, userName, password);
            conn.close();
            conn = null;
        } catch (Exception e) {
            throw new LogicException("数据库连接失败:");
        }
        return true;
    }

    public Properties getApplicationConfig() {
        Properties p = new Properties();
        try {
            p.load(getClass().getResourceAsStream(this.applicationConfig));
        } catch (IOException e) {
            logger.error("加载数据库配置文件出错，请确认/application.properties是否存在!");
            p = null;
        }
        return p;
    }

    public Properties getDatabaseConfig() {
        Properties p = new Properties();
        try {
            p.load(getClass().getResourceAsStream(this.databaseConfig));
        } catch (IOException e) {
            logger.error("加载数据库配置文件出错，请确认/db.properties是否存在!");
            p = null;
        }
        return p;
    }

    public void updateApplicationConfig(Properties p) {
        try {
            OutputStream out = new FileOutputStream(getClass().getResource(this.applicationConfig).getFile());
            p.store(out, "application gblobal config information");
        } catch (IOException e) {
            System.out.println("保存配置文件出错");
            e.printStackTrace();
        }
    }

    public void updateDatabaseConfig(Properties p) {
        try {
            OutputStream out = new FileOutputStream(getClass().getResource(this.databaseConfig).getFile());
            p.store(out, "database config information");
        } catch (IOException e) {
            System.out.println("保存配置文件出错");
            e.printStackTrace();
        }
    }

    public void setDatabaseConfig(String databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void setApplicationConfig(String applicationConfig) {
        this.applicationConfig = applicationConfig;
    }
}
