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
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.query.SystemMenuQuery;
import com.eastinno.otransos.security.service.IPermissionService;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 租户菜单管理
 * 
 * @author nsz
 */
@Action
public class SystemMenuAction extends AbstractPageCmdAction {
    @Inject
    private ISystemMenuService systemMenuService;
    @Inject
    private IPermissionService permissionService;
    @Inject
    protected ITenantService tenantService;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(SystemMenuQuery.class);
        IPageList pl = systemMenuService.getSystemMenuBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    /**
     * 添加
     * 
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        SystemMenu SystemMenu = form.toPo(SystemMenu.class);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.sn", SystemMenu.getSn(), "=");
        List<?> listMs = this.systemMenuService.getSystemMenuBy(qo).getResult();
        if (listMs != null && listMs.size() > 0) {
            this.addError("msg", "菜单编码已经存在，请重新输入！！！");
        }
        if (!hasErrors()) {
            qo = new QueryObject();
            qo.addQuery("obj.sequence>0");
            qo.setOrderBy("sequence desc");
            qo.setPageSize(1);
            List<SystemMenu> list = this.systemMenuService.getSystemMenuBy(qo).getResult();
            if (list != null && list.size() > 0) {
                SystemMenu.setSequence(list.get(0).getSequence() + 1);
            }
            Tenant tenant = ShiroUtils.getTenant();
            SystemMenu.setTenant(tenant);
            if (!hasErrors()) {
                Long ret = this.systemMenuService.addSystemMenu(SystemMenu);
                if (ret != null) {
                    form.addResult("msg", "添加成功");
                }
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 修改
     * 
     * @param form
     * @return
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SystemMenu systemMenu = systemMenuService.getSystemMenu(id);
        Tenant tenant = systemMenu.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getCode().indexOf(tenant.getCode())!=-1 && !curTenant.getCode().equals(tenant.getCode())) {
            this.addError("msg", "不可操作管理员分配的菜单！！！");
        }
        form.toPo(systemMenu, true);
        if (!hasErrors()) {
            boolean ret = systemMenuService.updateSystemMenu(id, systemMenu);
            if (ret) {
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doGetTree(WebForm form) {
        String permissionId = CommUtil.null2String(form.get("permissionId"));
        List<Long> menuIds = new ArrayList<Long>();
        if (!"".equals(permissionId)) {
            Permission permission = this.permissionService.getPermission(Long.parseLong(permissionId));
            List<SystemMenu> menus = permission.getMenus();
            for (SystemMenu menu : menus) {
                menuIds.add(menu.getId());
            }
        }
        QueryObject qo = form.toPo(SystemMenuQuery.class);
        String isPerm = CommUtil.null2String(form.get("isPerm"));
        List<?> list = this.systemMenuService.getSystemMenuBy(qo).getResult();
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (list != null && list.size() > 0) {
            for (int i = 0, c = list.size(); i < c; i++) {
                SystemMenu sm = (SystemMenu) list.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", sm.getId() + "");
                map.put("text", sm.getTitle());
                map.put("leaf", sm.getChildren().size() == 0);
                if (!"".equals(isPerm)) {
                    map.put("checked", menuIds.contains(sm.getId()) ? true : false);
                }
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

    /**
     * 删除
     * 
     * @param form
     * @return
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        SystemMenu SystemMenu = this.systemMenuService.getSystemMenu(id);
        Tenant tenant = SystemMenu.getTenant();
        Tenant curTenant = ShiroUtils.getTenant();
        if (curTenant.getCode().indexOf(tenant.getCode())!=-1 && !curTenant.getCode().equals(tenant.getCode())) {
            this.addError("msg", "不可操作管理员分配的菜单！！！");
        }
        if (!hasErrors()) {
            this.systemMenuService.delSystemMenu(id);
        }
        return pageForExtForm(form);
    }

    /**
     * 上移下移
     * 
     * @param form
     * @return
     */
    public Page doSwapSequence(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        String down = CommUtil.null2String(form.get("down"));
        SystemMenu curMenu = this.systemMenuService.getSystemMenu(Long.parseLong(id));
        QueryObject qo = new QueryObject();
        qo.setPageSize(1);
        if (curMenu.getParent() == null) {
            qo.addQuery("obj.parent is EMPTY");
        } else {
            qo.addQuery("obj.parent", curMenu.getParent(), "=");
        }
        if (!"".equals(down)) {
            qo.addQuery("obj.sequence", curMenu.getSequence(), ">");
        } else {
            qo.addQuery("obj.sequence", curMenu.getSequence(), "<");
        }
        List<?> list = this.systemMenuService.getSystemMenuBy(qo).getResult();
        if (list != null && list.size() > 0) {
            SystemMenu obj = (SystemMenu) list.get(0);
            int curSequence = curMenu.getSequence();
            curMenu.setSequence(obj.getSequence());
            this.systemMenuService.updateSystemMenu(Long.parseLong(id), curMenu);
            obj.setSequence(curSequence);
            this.systemMenuService.updateSystemMenu(obj.getId(), obj);
        }
        return pageForExtForm(form);
    }

    public ISystemMenuService getsystemMenuService() {
        return systemMenuService;
    }

    public void setsystemMenuService(ISystemMenuService systemMenuService) {
        this.systemMenuService = systemMenuService;
    }

    public void setPermissionService(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void setTenantService(ITenantService tenantService) {
        this.tenantService = tenantService;
    }
}
