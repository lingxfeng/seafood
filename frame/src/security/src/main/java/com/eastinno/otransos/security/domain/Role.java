package com.eastinno.otransos.security.domain;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastinno.otransos.container.annonation.MultiPOLoad;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * @Creation date: 2011年12月17日 下午3:40:26
 * @Intro 角色
 */
@Entity(name = "Disco_Role")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Role extends TenantObject  implements Principal, Serializable, IJsonObject {
    private static final long serialVersionUID = 4459114061592261741L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;
    private String path;

    @Column(length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    private List<Role> children = new ArrayList<Role>();

    @POLoad(name = "parentId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role parent;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "disco_role_Permissions")
    @MultiPOLoad(targetClz = Permission.class)
    private List<Permission> permissions = new ArrayList<Permission>();

    private Integer status = 0;

    private Integer types = 0;

    @Enumerated(EnumType.ORDINAL)
    private RoleType roleType;

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"tenant","children", "parent", "permissions", "users",
                "normalChildren", "roleGroup"});
        if (this.parent != null) {
            map.put("parent", CommUtil.obj2map(this.parent, new String[] {"id", "name", "title"}));
        }
        if (tenant != null){
        	map.put("tenant", CommUtil.obj2map(tenant, new String[] {"id", "title", "code"}));
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Permission p : this.permissions) {
            list.add(CommUtil.obj2map(p, new String[] {"id", "name", "description"}));
        }
        map.put("permissions", list);

        if (this.roleType != null) {
            Map<String, Object> roleTypeMap = new HashMap<String, Object>();
            roleTypeMap.put("text", this.roleType.getText());
            roleTypeMap.put("value", this.roleType.name());
            map.put("roleType", roleTypeMap);
        }
        return map;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    protected void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getChildren() {
        return this.children;
    }

    public void setChildren(List<Role> children) {
        this.children = children;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addPermission(Permission permission) {
        if (!this.permissions.contains(permission))
            this.permissions.add(permission);
    }

    public void delPermission(Permission permission) {
        for (Permission p : this.permissions) {
            if (p.getId().equals(permission.getId())) {
                this.permissions.remove(p);
                return;
            }
        }
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
        Role other = (Role) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    public Role getParent() {
        return this.parent;
    }

    public void setParent(Role parent) {
        this.parent = parent;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getTypes() {
        return this.types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String toString() {
        return "Role [id=" + this.id + ", name=" + this.name + "]";
    }
}
