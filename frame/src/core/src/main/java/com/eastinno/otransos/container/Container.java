package com.eastinno.otransos.container;

import java.util.Collection;
import java.util.List;

/**
 * 框架核心容器,用来存放系统中所要使用的各种Bean。包括Web层的Action、Module、WebForm,业务层的Bean及DAO层的上下文等
 * 
 * @author lengyu
 * @since 2006-12
 */
public interface Container {
    /**
     * 根据名称从容器中返回一个Bean
     * 
     * @param name 要查找的bean名称
     * @return 若存在该名称的bean则返回该bean对象，若没有找到指定名称的bean，则返回null
     */
    Object getBean(String name);

    /**
     * 从容器中查询一个类型为type的Bean
     * 
     * @param type 要查找的Bean类型
     * @return 若存在该类型的bean则返回第一个符合该类型的bean，若没有找到指定类型的bean，则返回null
     */
    <T> T getBean(Class<T> type);

    /**
     * 在容器中查找所有类型为type的Bean
     * 
     * @param type 要查找的bean类型，可以是接口、抽象类或具体的类
     * @return 若找到符合条件Bean，则返回一个List列表，否则返回null
     */

    <T> List<T> getBeans(Class<T> type);

    /**
     * 判断容器中是否包含指定名称的Bean
     * 
     * @param name 需要查找的bean名称
     * @return 若存在指定名称的bean则返回true，否则返回false
     */
    boolean containsBean(String name);

    /**
     * 得到容器中所有Bean的名称
     * 
     * @return 返回容器中存在的所有Bean的名称集合，包括匿名Bean
     */
    Collection getBeansName();

}
