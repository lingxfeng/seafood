package com.eastinno.otransos.security.mvc.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.domain.Resource;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.IResourceService;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shiro.security.core.ShiroDbRealm.ShiroUser;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * 后台访问控制类
 * 
 * @author ningsz
 */
public class ManageAction extends AbstractPageCmdAction {
    @Inject
    private IUserService userService;
    @Inject
    private ISystemMenuService systemMenuService;
    @Inject
    private IResourceService resourceService;
    public Object doBefore(WebForm form, Module module) {
        User emp = ShiroUtils.getUser();
        if (emp == null) {
            emp.setName("匿名用户");
        }
        form.addResult("user", emp);
        return super.doBefore(form, module);
    }

    /**
     * 用户自己修改密码
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doChangePasssword(WebForm form, Module module) {
        String newPSW = CommUtil.null2String(form.get("password"));
        String rnewPSW = CommUtil.null2String(form.get("password1"));
        String oldPSW = CommUtil.null2String(form.get("oldPassword"));
        ShiroUser shiroUser = ShiroUtils.getShiroUser();
        User em = this.userService.getUser(shiroUser.getId());

        String oldpassword = ShiroUtils.getPassWord(oldPSW, em.getSalt());
        if ("".equals(oldPSW))
            addError("oldPassword", "旧密码不能为空！！！");
        else if ("".equals(newPSW))
            addError("password", "新密码不能为空！！！");
        else if (!em.getPassword().equals(oldpassword))
            addError("oldPassword", "您输入的旧密码不正确！！！");
        else if (!newPSW.equals(rnewPSW)) {
            addError("password1", "两次输入的新密码不一致！！！");
        }
        if (!hasErrors()) {
            String newPassword = ShiroUtils.getPassWord(newPSW, em.getSalt());
            em.setPassword(newPassword);
            this.userService.updateUser(em.getId(), em);
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }
        return pageForExtForm(form);
    }

    /**
     * 初始化密码
     * 
     * @param form
     * @return
     */
    public Page doInitPassword(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        String name = CommUtil.null2String(form.get("name"));
        User em = null;
        if (StringUtils.hasText(id)) {
            em = this.userService.getUser(Long.parseLong(id));
        } else {
            QueryObject qo = new QueryObject();
            qo.addQuery("obj.name", name, "=");
            List<User> list = this.userService.getUserBy(qo).getResult();
            if (list != null && list.size() > 0) {
                em = list.get(0);
            }
        }
        if (em != null) {
            String salt = ShiroUtils.getSalt();
            String password = ShiroUtils.getPassWord(ShiroUtils.PASSWORD, salt);
            em.setSalt(salt);
            em.setPassword(password);
            this.userService.updateUser(em.getId(), em);
        }
        return pageForExtForm(form);
    }

    /**
     * 查询左侧菜单
     * 
     * @param form
     * @return
     */
    public Page doGetUserMenuTree(WebForm form) {
        String sn = CommUtil.null2String(form.get("sn"));
        String id = CommUtil.null2String(form.get("id"));
        String all = CommUtil.null2String(form.get("all"));

        QueryObject qo = new QueryObject();
        qo.addQuery("obj.system", Boolean.valueOf(true), "=");
        qo.addQuery("obj.status", Integer.valueOf(0), "=");
        qo.addQuery("obj.type",Integer.parseInt("1"),"=");
        if (!"".equals(id)) {
            qo.addQuery("obj.parent.id", new Long(id), "=");
        } else if (!"".equals(sn)) {
            qo.addQuery("obj.parent.sn", sn, "=");
        } else {
            qo.addQuery("(obj.parent is EMPTY)", null);
        }

        qo.setPageSize(Integer.valueOf(-1));
        qo.setOrderBy("sequence");
        List<SystemMenu> list = this.systemMenuService.getSystemMenuBy(qo).getResult();
        List nodes = new ArrayList();
        if ((list != null) && (!list.isEmpty())) {
            for (int i = 0; i < list.size(); i++) {
            	SystemMenu menu = list.get(i);
                if (ShiroUtils.hasMenu(menu)) {
                    Map map = useMenu2treemap(menu, "true".equals(all));
                    if (map != null) {
                        nodes.add(map);
                    }
                }
            }
        }
        form.jsonResult(nodes);
        return Page.JSPage;
    }

    /**
     * 把当前菜单对象及子菜单对象转为MAP对象
     * 
     * @param menu
     * @param loadAll 是否加载全部的子菜单对象
     * @return
     */
    protected Map useMenu2treemap(SystemMenu menu, boolean loadAll) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", menu.getId());
        map.put("text", menu.getTitle());
        if (!"".equals(menu.getAppClass()))
            map.put("appClass", menu.getAppClass());
        if (!"".equals(menu.getUrl()))
            map.put("script", menu.getUrl());
        if (!"".equals(menu.getPack()))
            map.put("package", menu.getPack());
        boolean leaf = (menu.getChildren() == null) || (menu.getChildren().isEmpty());
        map.put("leaf", Boolean.valueOf(leaf));
        if ((loadAll) && (!leaf)) {
            if ((menu.getChildren() != null) && (!menu.getChildren().isEmpty())) {
                List children = new ArrayList();
                for (SystemMenu sm : menu.getChildren()) {
                    if (ShiroUtils.hasMenu(sm) && sm.getStatus() == 0) {
                        Map temp = useMenu2treemap(sm, loadAll);
                        if (temp != null)
                            children.add(temp);
                    }
                }
                if (children.size() > 0)
                    map.put("children", children);
            }
        } else
            map.put("leaf", Boolean.valueOf((menu.getChildren() == null) || (menu.getChildren().isEmpty())));
        return map;
    }

    public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setSystemMenuService(ISystemMenuService systemMenuService) {
		this.systemMenuService = systemMenuService;
	}

	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
     * 检查按钮
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doCheckButtons(WebForm form, Module module) {
        Map map = new HashMap();
        List<String> list = new ArrayList<String>();
        String[] names = CommUtil.getStringArray(form.get("names"));
        String[] urls = CommUtil.getStringArray(form.get("actions"));
        String[] cmds = CommUtil.getStringArray(form.get("cmds"));
        for (int i = 0, c = names.length; i < c; i++) {
            String permissioncheck = urls[i] + ":" + cmds[i].toUpperCase();
            Tenant tenant = ShiroUtils.getTenant();
            String tCode = tenant.getCode();
            if (ShiroUtils.hasPermission(tCode+":"+permissioncheck)) {
                list.add(names[i]);
            }
        }
        map.put("permissions", list);
        form.jsonResult(map);
        return Page.JSONPage;
    }
}
