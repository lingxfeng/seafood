package com.eastinno.otransos.core.domain;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

@Entity(name = "Disco_SystemDictionaryDetail")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(inject = "title,tvalue,sequence,parent,intro")
public class SystemDictionaryDetail implements Serializable, Comparable, IJsonObject {
    private static final long serialVersionUID = -1056725870247898052L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotBlank(message = "数据字典项标题不能为空")
    private String title;

    @ManyToOne
    @POLoad(name = "parentId")
    private SystemDictionary parent;

    @NotBlank(message = "数据字典项值不能为空")
    private String tvalue;// value为系统关键字所以改为tavlue
    private Integer sequence;
    private String intro;

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"parent"});
        return map;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemDictionary getParent() {
        return this.parent;
    }

    public void setParent(SystemDictionary parent) {
        this.parent = parent;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTvalue() {
        return tvalue;
    }

    public void setTvalue(String tvalue) {
        this.tvalue = tvalue;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean equals(Object obj) {
        if ((obj == null) || (!(obj instanceof SystemDictionaryDetail)))
            return false;
        SystemDictionaryDetail o = (SystemDictionaryDetail) obj;
        if ((this.id != null) && (o.getId() != null))
            return this.id.equals(o.getId());
        return super.equals(obj);
    }

    public int compareTo(Object o) {
        SystemDictionaryDetail m = (SystemDictionaryDetail) o;
        if (this.sequence == null)
            return 1;
        if (m.sequence == null)
            return -1;
        return this.sequence.intValue() - m.sequence.intValue();
    }
}
