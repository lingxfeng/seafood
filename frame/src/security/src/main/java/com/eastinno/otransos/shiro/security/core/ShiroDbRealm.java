package com.eastinno.otransos.shiro.security.core;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.web.ActionContext;

/**
 * 权限控制核心类
 * @author nsz
 */
@Component("shiroDbRealm")
public class ShiroDbRealm extends AuthorizingRealm {
    @Autowired
    protected IUserService userService;
    @Autowired
    protected ISystemMenuService systemMenuService;
    @Autowired
    protected ITenantService tenantService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.name", token.getUsername(), "=");
        List<User> pl = userService.getUserBy(qo).getResult();
        User emp = null;
        if (pl != null && pl.size() > 0) {
            emp = pl.get(0);
            ActionContext.getContext().getSession().setAttribute("DISCO_SECURITY_TENANT", emp.getTenant());
        }
        if (emp != null) {
            return new SimpleAuthenticationInfo(ShiroUtils.getShiroUserFromUser(emp),
                    emp.getPassword(), ByteSource.Util.bytes(emp.getSalt()), getName());
        } else {
            return null;
        }
    }

    /**
     * 授权查询回调函数,
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(shiroUser.getRoleNames());
        info.addStringPermissions(shiroUser.getMenuPerStrs());
        info.addStringPermissions(shiroUser.getReslurcePerStrs());
        return info;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ShiroUtils.PASSWORDTYPE);
        matcher.setHashIterations(ShiroUtils.PASSWORDSIZE);
        setCredentialsMatcher(matcher);
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        public Long id;
        public String name;
        public String trueName;
        public String roleIds;
        public String permissionIds;
        public String menuIds;
        public String resourceIds;
        
        public List<String> roleNames;
        public List<String> menuPerStrs;
        public List<String> reslurcePerStrs;
        public ShiroUser(Long id, String name, String trueName, String roleIds,String permissoinIds,String menusIds,String resourceIds,List<String> roleNames,List<String> menuPerStrs,List<String> reslurcePerStrs) {
            this.id = id;
            this.name = name;
            this.trueName = trueName;
            this.roleIds=roleIds;
            this.permissionIds = permissoinIds;
            this.menuIds = menusIds;
            this.resourceIds = resourceIds;
            
            this.roleNames=roleNames;
            this.menuPerStrs=menuPerStrs;
            this.reslurcePerStrs=reslurcePerStrs;
        }

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public String getRoleIds() {
			return roleIds;
		}

		public void setRoleIds(String roleIds) {
			this.roleIds = roleIds;
		}

		public String getPermissionIds() {
			return permissionIds;
		}

		public void setPermissionIds(String permissionIds) {
			this.permissionIds = permissionIds;
		}

		public String getMenuIds() {
			return menuIds;
		}

		public void setMenuIds(String menuIds) {
			this.menuIds = menuIds;
		}

		public String getResourceIds() {
			return resourceIds;
		}

		public void setResourceIds(String resourceIds) {
			this.resourceIds = resourceIds;
		}
		
		public List<String> getRoleNames() {
			return roleNames;
		}

		public void setRoleNames(List<String> roleNames) {
			this.roleNames = roleNames;
		}

		public List<String> getMenuPerStrs() {
			return menuPerStrs;
		}

		public void setMenuPerStrs(List<String> menuPerStrs) {
			this.menuPerStrs = menuPerStrs;
		}

		public List<String> getReslurcePerStrs() {
			return reslurcePerStrs;
		}

		public void setReslurcePerStrs(List<String> reslurcePerStrs) {
			this.reslurcePerStrs = reslurcePerStrs;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		/**
         * 本函数输出将作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return name;
        }

        /**
         * 重载hashCode,只计算loginName;
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        /**
         * 重载equals,只计算loginName;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }
    }
}
