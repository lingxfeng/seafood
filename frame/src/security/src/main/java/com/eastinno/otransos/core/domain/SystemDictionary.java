package com.eastinno.otransos.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.FormPO;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

@Entity(name = "Disco_SystemDictionary")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@FormPO(inject = "sn,title,intro")
public class SystemDictionary implements Serializable, IJsonObject {
    private static final long serialVersionUID = 202324195614478378L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Field(name = "编号", validator = @Validator(name = "string", value = "blank;trim;max:100", required = true))
    private String sn;

    @Field(name = "名称", validator = @Validator(name = "string", value = "blank;trim;max:100", required = true))
    private String title;

    @Field(name = "简介", validator = @Validator(name = "string", value = "blank;trim;max:200"))
    private String intro;

    @Column
    private String category;

    @OrderBy("sequence asc")
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    private List<SystemDictionaryDetail> children = new ArrayList();

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"children"});
        if ((this.children != null) && (!this.children.isEmpty())) {
            List list = new ArrayList();
            for (int i = 0; i < this.children.size(); i++) {
                list.add(((SystemDictionaryDetail) this.children.get(i)).toJSonObject());
            }
            map.put("children", list);
        }
        return map;
    }

    public List<SystemDictionaryDetail> getChildren() {
        return this.children;
    }

    public void setChildren(List<SystemDictionaryDetail> children) {
        this.children = children;
    }

    public void addChild(SystemDictionaryDetail detail) {
        if (detail.getSequence() == null) {
            List list = new ArrayList();
            if (list.size() > 0)
                detail.setSequence(Integer.valueOf(((SystemDictionaryDetail) list.get(list.size() - 1)).getSequence().intValue() + 1));
            else {
                detail.setSequence(Integer.valueOf(1));
            }
        }
        detail.setParent(this);
        this.children.add(detail);
    }

    public SystemDictionaryDetail getChild(String value) {
        SystemDictionaryDetail ret = null;
        for (SystemDictionaryDetail detail : this.children) {
            if (detail.getTvalue().equals(value)) {
                ret = detail;
                break;
            }
        }
        return ret;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean equals(Object obj) {
        if ((obj == null) || (!(obj instanceof SystemDictionary)))
            return false;
        SystemDictionary o = (SystemDictionary) obj;
        if ((this.id != null) && (o.getId() != null))
            return this.id.equals(o.getId());
        return super.equals(obj);
    }
}
