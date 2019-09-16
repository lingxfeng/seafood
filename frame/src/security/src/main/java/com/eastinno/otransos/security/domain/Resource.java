package com.eastinno.otransos.security.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.Field;
import com.eastinno.otransos.container.annonation.Validator;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;
/**
 * 资源
 * @author Administrator
 *
 */
@Entity(name = "Disco_Resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends TenantObject implements Serializable, IJsonObject {
    private static final long serialVersionUID = -378400808581420978L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 250, unique = true)
    @Field(name = "资源描述", validator = @Validator(name = "string", value = "blank;trim;max:250", required = true))
    private String resStr;

    @Column(name = "vtype", length = 16)
    @Enumerated(EnumType.STRING)
    @Field(name = "资源类型", validator = @Validator(name = "required"))
    private ResourceType type;
    private String actionName;
    private String descName;

    @Column(length = 300)
    @Field(validator = @Validator(name = "string", value = "blank;trim;max:300"))
    private String desciption;
    private String name;
    private Integer status = Integer.valueOf(0);

    @Column(length = 1000)
    private String roles;
    public String getDescName() {
        return this.descName;
    }

    public void setDescName(String descName) {
        this.descName = descName;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

	public Object toJSonObject() {
        Map map = CommUtil.obj2mapExcept(this, new String[] {"permissions", "type","tenant"});
        if (this.type != null) {
            Map typeMap = new HashMap();
            typeMap.put("id", this.type.name());
            typeMap.put("title", this.type.toString());
            map.put("type", typeMap);
        }
        return map;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesciption() {
        return this.desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResStr() {
        return this.resStr;
    }

    public void setResStr(String resStr) {
        this.resStr = resStr;
    }

    public ResourceType getType() {
        return this.type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int hashCode() {
        int prime = 31;
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
        Resource other = (Resource) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }
}
