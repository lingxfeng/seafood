package com.eastinno.otransos.ext.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2011年12月16日 下午5:01:40
 * @Intro
 */
@Service
public class SpringHelper implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据beanName名字取得bean
     * 
     * @param beanName
     * @return
     */
    public synchronized static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * 根据接口类取得对应的实现Bean
     * 
     * @param beanName
     * @return
     */
    public synchronized static Object getBean(Class<?> interfaceClz) {
        return context.getBean(interfaceClz);
    }
}
