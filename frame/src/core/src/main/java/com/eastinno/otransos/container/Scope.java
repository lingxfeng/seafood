package com.eastinno.otransos.container;

/**
 * Bean的类型，主要用来指定Bean的生命周期
 * 
 * @author lengyu
 */
public interface Scope {
    /**
     * 往容器中注册一个Bean
     * 
     * @param name　 Bean的名称
     * @param bean　 Bean的配置信息
     */
    Object getBean(String name, BeanDefinition beanDefinition);
}
