package com.eastinno.otransos.application.core.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 统一定义id的entity基类. <br />
 *        基类统一定义id的属性名称、数据类型、列名映射及生成策略. <br />
 *        Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * @version v_0.1
 * @author lengyu
 * @since 2012-10-25 下午10:06:58
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class IdEntity implements Serializable, Cloneable, IJsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Object toJSonObject() {
        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}