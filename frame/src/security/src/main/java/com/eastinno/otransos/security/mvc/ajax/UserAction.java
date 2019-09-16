package com.eastinno.otransos.security.mvc.ajax;

import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.query.UserQuery;
import com.eastinno.otransos.security.service.IRoleService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 员工管理
 */
@Action
public class UserAction extends AbstractPageCmdAction {
	@Inject
    private IUserService userService;
    @Inject
    private IRoleService roleService;

    public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public Page doList(WebForm form) {
        QueryObject qo = form.toPo(UserQuery.class);
        IPageList pl = this.userService.getUserBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        User User = this.userService.getUser(id);
        if (User.getType() == 2) {
            this.addError("msg", "租户管理员不可删除");
        }
        if (!hasErrors()) {
            userService.delUser(id);
        }
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        ShiroUser user = ShiroUtils.getShiroUser();
        Tenant tenant = ShiroUtils.getTenant();
        if (tenant == null && !user.getName().equals(ShiroUtils.ADMIN) && !user.getName().equals(ShiroUtils.ROOT)) {
            this.addError("msg", "普通平台管理员不可添加平台管理员！！！");
        } else {
            User e = form.toPo(User.class);
            QueryObject qo = new QueryObject();
            qo.addQuery("(obj.code='" + e.getCode() + "' or obj.name='" + e.getName() + "')");
            List<?> list = this.userService.getUserBy(qo).getResult();
            if (list != null && list.size() > 0) {
                this.addError("msg", "用户名或用户登录名已经存在！！！");
            }
            if (!hasErrors()) {
                e.setTenant(tenant);
                e.setType(tenant == null ? 1 : 3);
                this.userService.addUser(e);
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        User entry = this.userService.getUser(id);
        form.toPo(entry);
        if (!hasErrors()) {
            this.userService.updateUser(id, entry);
        }
        return pageForExtForm(form);
    }

    public Page doAddRole(WebForm form) {
        String roleId = CommUtil.null2String(form.get("roleId"));
        if (!"".equals(roleId)) {
            Role role = this.roleService.getRole(Long.parseLong(roleId));
            String userId = CommUtil.null2String(form.get("userId"));
            User e = this.userService.getUser(Long.parseLong(userId));
            e.addRole(role);
            this.userService.updateUser(Long.parseLong(userId), e);
        }
        return pageForExtForm(form);
    }

    /**
     * 删除权限
     * 
     * @param form
     * @return
     */
    public Page doDelRole(WebForm form) {
        String roleId = CommUtil.null2String(form.get("roleId"));
        if (!"".equals(roleId)) {
            Role role = this.roleService.getRole(Long.parseLong(roleId));
            String UserId = CommUtil.null2String(form.get("userId"));
            User e = this.userService.getUser(Long.parseLong(UserId));
            e.delRole(role);
            this.userService.updateUser(Long.parseLong(UserId), e);
        }
        return pageForExtForm(form);
    }

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
}