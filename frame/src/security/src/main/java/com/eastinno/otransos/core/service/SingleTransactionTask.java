package com.eastinno.otransos.core.service;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.eastinno.otransos.container.Container;
import com.eastinno.otransos.ext.util.SpringHelper;
import com.eastinno.otransos.web.core.FrameworkEngine;

/**
 * 简单事务处理,开始一个新的事务执行完业务逻辑后立即提交事务
 * 
 * @version 2.0
 * @author lengyu
 * @date 2009年12月12日-下午11:12:16
 */
public abstract class SingleTransactionTask implements Runnable {
    public void run() {
        DataSource dataSource = null;
        Container c = FrameworkEngine.getContainer();
        if (c != null) {
            dataSource = (DataSource) c.getBean("dataSource");
            if (dataSource == null) {
                dataSource = c.getBean(DataSource.class);
            }
        } else {
            dataSource = (DataSource) SpringHelper.getBean("dataSource");
        }
        if (dataSource != null) {
            PlatformTransactionManager tm = new DataSourceTransactionManager(dataSource);
            TransactionStatus tmStatus = tm.getTransaction(null);
            try {
                execute();
                tm.commit(tmStatus);
            } catch (Exception e) {
                tm.rollback(tmStatus);
            }
        }
    }

    public abstract void execute();
}
