package com.eastinno.otransos.container;

public class ConstructorArgumentValue implements java.lang.Comparable<ConstructorArgumentValue> {
    private Integer index;

    private Class type;

    private Object value;

    public ConstructorArgumentValue() {

    }

    public ConstructorArgumentValue(Integer index, Class type, Object value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int hashCode() {
        return index != null ? index.hashCode() : value.hashCode();
    }

    public int compareTo(ConstructorArgumentValue o) {
        return this.index - o.index;
    }

}