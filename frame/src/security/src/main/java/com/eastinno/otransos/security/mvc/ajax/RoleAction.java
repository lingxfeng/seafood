package com.eastinno.otransos.security.mvc.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.query.RoleQuery;
import com.eastinno.otransos.security.service.IPermissionService;
import com.eastinno.otransos.security.service.IRoleService;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * 租户角色管理
 */
@Action
public class RoleAction extends AbstractPageCmdAction {

    @Inject
    private IRoleService roleService;

    @Inject
    private IPermissionService permissionService;
    @Inject
    protected IUserService userService;
    @Inject
    private ISystemMenuService systemMenuService;
    @Inject
    private ITenantService tenantService;

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(RoleQuery.class);
        IPageList pageList = roleService.getRoleBy(qo);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }


    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        Role role = this.roleService.getRole(id);
        Tenant tenant = role.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getDepthPath().indexOf(tenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(tenant.getDepthPath())) {
            this.addError("msg", "不可删除管理员分配的角色");
        }
        roleService.delRole(id);
        return pageForExtForm(form);
    }


    public Page doSave(WebForm form) {
        Role object = form.toPo(Role.class);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.name", object.getName(), "=");
        List<?> list = this.roleService.getRoleBy(qo).getResult();
        if (list != null && list.size() > 0) {
            this.addError("msg", "角色代码已经存在!!!");
        }
        object.setTenant(ShiroUtils.getTenant());
        if (!hasErrors()) {
        	roleService.addRole(object);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }


    /**
     * 添加权限
     * @param form
     * @return
     */
    public Page doAddPermission(WebForm form) {
        String permissionId = CommUtil.null2String(form.get("permissionId"));
        if (!"".equals(permissionId)) {
            Permission permission = this.permissionService.getPermission(Long.parseLong(permissionId));
            String roleId = CommUtil.null2String(form.get("roleId"));
            Role role = this.roleService.getRole(Long.parseLong(roleId));
            Tenant tenant = role.getTenant();
            Tenant curTenant = ShiroUtils.getTenant();
            if (curTenant.getDepthPath().indexOf(tenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(tenant.getDepthPath())) {
                this.addError("msg", "不可操作管理员分配的角色");
            }
            if (!hasErrors()) {
                role.addPermission(permission);
            }
            this.roleService.updateRole(Long.parseLong(roleId), role);
        }
        return pageForExtForm(form);
    }


    /**
     * 删除权限
     * 
     * @param form
     * @return
     */
    public Page doDelPermission(WebForm form) {
        String id = CommUtil.null2String(form.get("permissionId"));
        if (!"".equals(id)) {
            Permission permission = this.permissionService.getPermission(Long.parseLong(id));
            String roleId = CommUtil.null2String(form.get("roleId"));
            Role role = this.roleService.getRole(Long.parseLong(roleId));
            Tenant tenant = role.getTenant();
            Tenant curTenant = ShiroUtils.getTenant();
            if (curTenant.getDepthPath().indexOf(tenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(tenant.getDepthPath())) {
                this.addError("msg", "不可操作管理员分配的角色");
            }
            if (!hasErrors()) {
                role.delPermission(permission);
            }
            this.roleService.updateRole(Long.parseLong(roleId), role);
        }
        return pageForExtForm(form);
    }


    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        Role object = roleService.getRole(id);
        Tenant tenant = object.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getDepthPath().indexOf(tenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(tenant.getDepthPath())) {
            this.addError("msg", "不可删除管理员分配的角色");
        }
        form.toPo(object, true);
        if (!hasErrors()) {
            this.roleService.updateRole(object.getId(), object);
        }
        return pageForExtForm(form);
    }


    /**
     * 获取角色树
     * 
     * @param form
     * @return
     */
    public Page doGetTree(WebForm form) {
        String userId = CommUtil.null2String(form.get("userId"));
        QueryObject query = form.toPo(RoleQuery.class);
        query.setPageSize(-1);
        List<Long> ids = new ArrayList<Long>();
        if (!"".equals(userId)) {
            User e = this.userService.getUser(Long.parseLong(userId));
            for (Role r : e.getRoles()) {
                ids.add(r.getId());
            }
        }
        String tenantId = CommUtil.null2String(form.get("tenantId"));
        if (!"".equals(tenantId)) {
            Tenant tenant = this.tenantService.getTenant(Long.parseLong(tenantId));
            for (Role r : tenant.getRoles()) {
                ids.add(r.getId());
            }
        }
        IPageList pageList = this.roleService.getRoleBy(query);
        List<Map> nodes = new java.util.ArrayList<Map>();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                Role category = (Role) pageList.getResult().get(i);
                Map map = new HashMap();
                map.put("id", category.getId() + "");
                map.put("text", category.getTitle());
                map.put("leaf", category.getChildren().size() < 1);
                map.put("checked", ids.contains(category.getId()) ? true : false);
                nodes.add(map);
            }
        }
        else {
            Map map = new HashMap();
            map.put("text", "无角色");
            map.put("id", "0");
            map.put("leaf", true);
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}


	public void setPermissionService(IPermissionService permissionService) {
		this.permissionService = permissionService;
	}


	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	public void setSystemMenuService(ISystemMenuService systemMenuService) {
		this.systemMenuService = systemMenuService;
	}


	public void setTenantService(ITenantService tenantService) {
        this.tenantService = tenantService;
    }
	
}