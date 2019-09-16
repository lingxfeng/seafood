package com.eastinno.otransos.web;

import java.io.Serializable;

/**
 * 关联对象加载器，主要用来根据主键值加载模型(域)对象中的关联属性
 * 
 * @author lengyu
 */
public interface POLoadDao {
    /**
     * 从持久(或业务)层加载特定id值，类型为clz的对象。
     * 
     * @param clz 类型名称
     * @param id 主键值
     * @return 返回加载的属性对象
     */
    Object get(Class clz, Serializable id);
}
