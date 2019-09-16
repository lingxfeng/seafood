package com.eastinno.otransos.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

@Entity(name = "Disco_BaseUIConfig")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaseUIConfig implements IJsonObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String appClass;
    private String scripts;

    @POLoad
    @ManyToOne(fetch = FetchType.LAZY)
    private BaseUIConfig parent;

    @OneToMany(mappedBy = "parent")
    private List<BaseUIConfig> children = new ArrayList();

    public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"parent", "children"});
        if (this.parent != null) {
            map.put("parent", CommUtil.obj2map(this.parent, new String[] {"id", "title", "appClass"}));
        }
        return map;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAppClass() {
        return this.appClass;
    }

    public String getScripts() {
        return this.scripts;
    }

    public BaseUIConfig getParent() {
        return this.parent;
    }

    public List<BaseUIConfig> getChildren() {
        return this.children;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAppClass(String appClass) {
        this.appClass = appClass;
    }

    public void setScripts(String scripts) {
        this.scripts = scripts;
    }

    public void setParent(BaseUIConfig parent) {
        this.parent = parent;
    }

    public void setChildren(List<BaseUIConfig> children) {
        this.children = children;
    }
}
