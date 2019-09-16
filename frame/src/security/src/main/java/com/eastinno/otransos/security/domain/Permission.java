package com.eastinno.otransos.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;
/**
 * 权限
 * @author Administrator
 *
 */
@Entity(name = "Disco_Permission")
public class Permission extends TenantObject implements Serializable, IJsonObject {
    private static final long serialVersionUID = 7600426216028004160L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 200, unique = true)
    @Field(name = "权限名称", validator = @Validator(name = "string", value = "blank;trim;max:50", required = true))
    private String name;

    @Column(length = 50)
    private String sn;

    @Column(length = 250)
    @Field(name = "描述", validator = @Validator(name = "string", value = "trim;max:250"))
    private String description;

    @Column(length = 250)
    @Field(name = "操作值", validator = @Validator(name = "string", value = "trim;max:250"))
    private String operation = "1";

    private Integer status = Integer.valueOf(0);

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "Disco_Permission_Ressource")
    private List<Resource> resources = new ArrayList<Resource>();
    private Integer type=1;//权限类型
    @ManyToMany(fetch=FetchType.LAZY)
    private List<SystemMenu> menus = new ArrayList<SystemMenu>();
    public Object toJSonObject() {
        Map<String,Object> map = CommUtil.obj2mapExcept(this, new String[] {"resources", "permissionGroup","tenant"});
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (Resource p : this.resources) {
            list.add(CommUtil.obj2map(p, new String[] {"id", "type", "resStr"}));
        }
        if (tenant != null){
        	map.put("tenant", CommUtil.obj2map(tenant, new String[] {"id", "title", "code"}));
        }
        return map;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }
    
    public List<SystemMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<SystemMenu> menus) {
		this.menus = menus;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<Resource> getResources() {
        return this.resources;
    }

    protected void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void addResource(Resource res) {
        for(Resource r:this.resources){
            if(r.getId().equals(res.getId())){
                return;
            }
        }
        this.resources.add(res);
    }
    public void delResource(Resource res){
        for(Resource r:this.resources){
            if(r.getId().equals(res.getId())){
                this.resources.remove(r);
                return;
            }
        }
    }
    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Permission other = (Permission) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else
            return this.id.equals(other.id);
        return false;
    }
    public void addSystemMenu(SystemMenu menu){
    	for(SystemMenu temMenu:this.menus){
    		if(temMenu.getId().equals(menu.getId())){
    			return;
    		}
    	}
    	this.menus.add(menu);
    }
    public void delSystemMenu(SystemMenu menu){
    	for(SystemMenu temMenu:this.menus){
    		if(temMenu.getId().equals(menu.getId())){
    			this.menus.remove(temMenu);
    			return;
    		}
    	}
    }
}
