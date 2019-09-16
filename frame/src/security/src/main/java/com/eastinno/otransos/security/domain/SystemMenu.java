package com.eastinno.otransos.security.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * 系统菜单管理
 * 
 * @author 
 */
@Entity
@Table(name = "Disco_SystemMenu")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemMenu extends TenantObject implements IJsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 100)
    @Field(validator = @Validator(name = "string", value = "blank;trim;max:40", required = true))
    private String title;
    @Column(length = 32, unique = true)
    @Field(validator = @Validator(name = "string", value = "blank;trim;max:32", required = true))
    private String sn;
    @Column(length = 250)
    private String url;
    private Integer sequence = 1;
    private String appClass;
    private String pack;
    private Integer status = 0;
    private String imgPath;
    private Integer type=1;//1系统菜单
    @POLoad
    @ManyToOne(fetch = FetchType.LAZY)
    private SystemMenu parent;

    @Column(name = "issystem")
    private Boolean system = Boolean.valueOf(false);

    @OrderBy("sequence asc")
    @OneToMany(mappedBy = "parent")
    private List<SystemMenu> children = new ArrayList<SystemMenu>();

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"tenant","theRoles", "parent", "children", "roles"});
        if (this.parent != null)
            map.put("parent", CommUtil.obj2map(this.parent, new String[] {"id", "sn", "title"}));
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SystemMenu r : this.children) {
            list.add(CommUtil.obj2map(r, new String[] {"id", "name", "title"}));
        }
        if (tenant != null){
        	map.put("tenant", CommUtil.obj2map(tenant, new String[] {"id", "title", "code"}));
        }
        map.put("children", list);
        return map;
    }

    public List<SystemMenu> getChildren() {
        return this.children;
    }

    public void setChildren(List<SystemMenu> children) {
        this.children = children;
    }

    public SystemMenu getParent() {
        return this.parent;
    }

    public void setParent(SystemMenu parent) {
        this.parent = parent;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public Boolean getSystem() {
        return this.system;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getAppClass() {
        return appClass;
    }

    public void setAppClass(String appClass) {
        this.appClass = appClass;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String toString() {
        return "SystemMenu [sn=" + this.sn + ", getId()=" + getId() + ", getTitle()=" + getTitle() + "]";
    }
}
