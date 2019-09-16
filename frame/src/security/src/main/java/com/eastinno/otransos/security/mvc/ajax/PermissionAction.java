package com.eastinno.otransos.security.mvc.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.security.domain.Resource;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.query.PermissionQuery;
import com.eastinno.otransos.security.service.IPermissionService;
import com.eastinno.otransos.security.service.IResourceService;
import com.eastinno.otransos.security.service.IRoleService;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 租户权限管理
 * 
 * @author ksmwly@gmail.com
 */
@Action
public class PermissionAction extends AbstractPageCmdAction {
    @Inject
    private IPermissionService service;
    @Inject
    private IResourceService resourceService;
    @Inject
    private ISystemMenuService systemMenuservice;
    @Inject
    private IRoleService roleService;

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(PermissionQuery.class);
        IPageList pageList = service.getPermissionBy(qo);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
        Permission object = service.getPermission(id);
        Tenant perTenant = object.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getDepthPath().indexOf(perTenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(perTenant.getDepthPath())) {
            this.addError("msg", "不可操作管理员分配的权限");
        }
        if (!hasErrors()) {
            service.delPermission(id);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doSave(WebForm form) {
        Permission object = form.toPo(Permission.class);
        Tenant tenant = ShiroUtils.getTenant();
        object.setTenant(tenant);
        if (!hasErrors())
            service.addPermission(object);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        Long id = Long.parseLong(CommUtil.null2String(form.get("id")));
        Permission object = service.getPermission(id);
        Tenant perTenant = object.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getDepthPath().indexOf(perTenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(perTenant.getDepthPath())) {
            this.addError("msg", "不可操作管理员分配的权限");
        }
        form.toPo(object, true);
        if (!hasErrors())
            service.updatePermission(id, object);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 添加删除资源
     * 
     * @param form
     * @return
     */
    public Page doDisResource(WebForm form) {
        String permissionId = CommUtil.null2String(form.get("permissionId"));
        Permission Permission = this.service.getPermission(Long.parseLong(permissionId));
        Tenant perTenant = Permission.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getDepthPath().indexOf(perTenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(perTenant.getDepthPath())) {
            this.addError("msg", "不可操作管理员分配的权限");
        }
        if (!hasErrors()) {
            String idsstr = CommUtil.null2String(form.get("ids"));
            String[] ids = idsstr.split(",");
            String del = CommUtil.null2String(form.get("del"));
            if ("".equals(del)) {
                for (String id : ids) {
                    Resource resource = this.resourceService.getResource(Long.parseLong(id));
                    Permission.addResource(resource);
                }
            } else {
                for (String id : ids) {
                    Resource resource = this.resourceService.getResource(Long.parseLong(id));
                    Permission.delResource(resource);
                }
            }
        }
        this.service.updatePermission(Long.parseLong(permissionId), Permission);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 添加删除菜单
     * 
     * @param form
     * @return
     */
    public Page doDisMenu(WebForm form) {
        String permissionId = CommUtil.null2String(form.get("permissionId"));
        String menuId = CommUtil.null2String(form.get("menuId"));
        String del = CommUtil.null2String(form.get("del"));
        Permission Permission = this.service.getPermission(Long.parseLong(permissionId));
        Tenant perTenant = Permission.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getDepthPath().indexOf(perTenant.getDepthPath())!=-1 && !curTenant.getDepthPath().equals(perTenant.getDepthPath())) {
            this.addError("msg", "不可操作管理员分配的权限");
        }
        if (!hasErrors()) {
            SystemMenu menu = this.systemMenuservice.getSystemMenu(Long.parseLong(menuId));
            if ("".equals(del)) {
                Permission.addSystemMenu(menu);
            } else {
                Permission.delSystemMenu(menu);
            }
            this.service.updatePermission(Long.parseLong(permissionId), Permission);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doGetTree(WebForm form) {
        String roleId = CommUtil.null2String(form.get("roleId"));
        Role role = this.roleService.getRole(Long.parseLong(roleId));
        Set<Long> ids = new HashSet<Long>();
        for (Permission p : role.getPermissions()) {
            ids.add(p.getId());
        }
        QueryObject qo = form.toPo(PermissionQuery.class);
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        List<?> permissions = this.service.getPermissionBy(qo).getResult();
        if (permissions != null && permissions.size() > 0) {
            for (Object obj : permissions) {
                Map<String, Object> map = new HashMap<String, Object>();
                Permission p = (Permission) obj;
                map.put("id", p.getId() + "");
                map.put("text", p.getName());
                map.put("leaf", true);
                map.put("checked", ids.contains(p.getId()) ? true : false);
                map.put("readonly", true);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "无菜单");
            map.put("id", "0");
            map.put("leaf", Boolean.valueOf(true));
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    public void setService(IPermissionService service) {
        this.service = service;
    }

    public void setResourceService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void setsystemMenuservice(ISystemMenuService systemMenuservice) {
        this.systemMenuservice = systemMenuservice;
    }

    public void setroleService(IRoleService roleService) {
        this.roleService = roleService;
    }

}