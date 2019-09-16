package com.eastinno.otransos.container;

import java.util.Map;

/**
 * 构造子参数封装
 * 
 * @author lengyu
 */
public class ConstructorArguments {
    private Map values = new java.util.TreeMap();

    public ConstructorArguments concat(ConstructorArguments other) {
        this.values.putAll(other.getArguments());
        return this;
    }

    /*
     * 添加参数
     */
    public void addArgument(ConstructorArgumentValue value) {
        values.put(value.getIndex(), value);
    }

    public void addArgument(Integer index, Object value) {
        addArgument(index, value.getClass(), value);
    }

    public void addArgument(Integer index, Class type, Object value) {
        ConstructorArgumentValue v = new ConstructorArgumentValue(index, type, value);
        addArgument(v);
    }

    public Map getArguments() {
        return values;
    }

    public boolean isEmpty() {
        return values.size() > 0;
    }

    public int getArgCount() {
        return values.size();
    }
}
