/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.eastinno.otransos.ext.platform.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.domain.Persistable;

import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 抽象实体基类，如果主键是数据库端自动生成 请使用{@link BaseEntity}，如果是Oracle 请使用{@link BaseOracleEntity}
 * <p/>
 * <p>
 * User: lengyu
 * <p>
 * Date: 13-3-20 下午8:38
 * <p>
 * Version: 1.0
 */
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>, Serializable, IJsonObject {

    public abstract ID getId();

    public abstract void setId(final ID id);

    public boolean isNew() {
        return null == getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        AbstractEntity<?> that = (AbstractEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

    @Override
    public Object toJSonObject() {
        return null;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
