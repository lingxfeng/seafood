package com.eastinno.otransos.web.ajax;

import java.util.HashSet;
import java.util.Set;

/**
 * 远程访问对象的定义信息
 * 
 * @author lengyu
 */
public class RemoteService {
    private String name;

    private Class type;

    private Set allowNames = new HashSet();

    private Set denyNames = new HashSet();

    /**
     * 允许远程暴露的属性名集合
     * 
     * @return 属性名集合
     */
    public Set getAllowNames() {
        return allowNames;
    }

    /**
     * 添加一个远程访问的属性
     * 
     * @param name 属性名
     */
    public void addAllowName(String name) {
        allowNames.add(name);
    }

    /**
     * 禁止一个属性远程访问
     * 
     * @param name 属性名
     */
    public void addDenyName(String name) {
        denyNames.add(name);
    }

    public void setAllowNames(Set allowMethods) {
        this.allowNames = allowMethods;
    }

    /**
     * 获取所有禁止远程访问的属性名集合
     * 
     * @return 所以禁止远程访问的属性集合
     */
    public Set getDenyNames() {
        return denyNames;
    }

    public void setDenyNames(Set denyMethods) {
        this.denyNames = denyMethods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String toString() {
        return "name=" + name + ";type=" + type + ";allowNames=" + allowNames + ";denyNames=" + denyNames;
    }
}
