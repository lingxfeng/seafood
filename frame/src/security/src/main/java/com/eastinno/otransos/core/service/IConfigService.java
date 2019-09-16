package com.eastinno.otransos.core.service;

import java.util.Properties;

public interface IConfigService {
    Properties getDatabaseConfig();

    Properties getApplicationConfig();

    void updateDatabaseConfig(Properties paramProperties);

    void updateApplicationConfig(Properties paramProperties);

    boolean checkConnection(Properties paramProperties);
}
