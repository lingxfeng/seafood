package com.eastinno.otransos.web.ajax;

/**
 * JS远程脚本调用参数封装
 * 
 * @author lengyu
 */
public class CallParameter {
    private int index;
    private Class type;
    private String name;
    private java.util.Map propetys = new java.util.HashMap();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public void addProperty(String name, Object value) {
        this.propetys.put(name, value);
    }

    public java.util.Map getPropetys() {
        return propetys;
    }

    public void setPropetys(java.util.Map propetys) {
        this.propetys = propetys;
    }
}
