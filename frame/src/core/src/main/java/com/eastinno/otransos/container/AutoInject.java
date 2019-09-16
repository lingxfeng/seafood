package com.eastinno.otransos.container;

/**
 * 自动注入属性的配置信息
 * 
 * @author lengyu
 */
public class AutoInject {

    private Class type;

    private String name;

    /**
     * 自动注入名
     * 
     * @param name 注入的bean名称
     */
    public AutoInject(String name) {
        this.name = name;
    }

    /**
     * 自动注入type指定的类
     * 
     * @param type 需要注入的类型
     */
    public AutoInject(Class type) {
        this.type = type;
    }

    /**
     * @return 返回自动注入的Bean的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置自动注入类型Bean的名称
     * 
     * @param name 注入的bean名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 返回自动注入Bean的类别
     */
    public Class getType() {
        return type;
    }

    /**
     * 设置自动注入Bean的类别
     * 
     * @param type 注入的类别
     */
    public void setType(Class type) {
        this.type = type;
    }
}
