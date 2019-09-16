package com.eastinno.otransos.web;

import java.util.List;

/**
 * <p>
 * Title:表单属性配置信息
 * </p>
 * <p>
 * Description: 处理disco-web.xml文件中的配置有关表单字段属性的信息
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author lengyu
 * @version 1.0
 */
public class FormProperty {
    private String name;

    private String initial;

    private String size;

    private String type;

    private String notNull;

    private List event;

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial == null ? "" : initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull == null ? "" : notNull;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? "" : size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? "" : type;
    }

    public List getEvent() {
        return event;
    }

    public void setEvent(List event) {
        this.event = event;
    }

    public String toString() {
        String s;
        s = "name=" + name + ";rinitial=" + initial + ";size=" + size + ";type=" + type + ";notNull=" + notNull;
        return s;
    }
}
