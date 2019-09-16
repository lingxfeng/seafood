package com.eastinno.otransos.security.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
/**
 * 初始化
 * @author nsz
 */
@Component
public class CommonService {
	@Autowired
	private IUserService userService;
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private ISystemMenuService systemMenuService;

	@PostConstruct
	public void init() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.name", ShiroUtils.ROOT, "=");
		List<User> users = this.userService.getUserBy(qo).getResult();
		if (users == null || users.size() == 0) {
			/**
			 * 初始化一级租户
			 */
			Tenant t = new Tenant();
			t.setCode(ShiroUtils.ROOT);
			t.setTitle("一级租户");
			t.setUrl("http://localhost");
			t.setStatus(1);
			this.tenantService.addTenant(t);
			/**
			 * 初始化一级租户结束
			 * 初始化超级管理员
			 */
			if (t.getId() != null) {
				User u = new User();
				u.setName(ShiroUtils.ROOT);
				u.setTrueName("超级管理员");
				u.setNickname("超级管理员");
				u.setCode(new Date().getTime() + "");
				u.setIsTenantAdmin(true);
				u.setTenant(t);
				this.userService.addUser(u);
			}
			/**
			 * 初始化超级管理员结束
			 * 初始化菜单
			 */
			SystemMenu menusys = new SystemMenu();
			menusys.setSn("sys");
			menusys.setTitle("系统基础配置");
			menusys.setSystem(true);
			menusys.setTenant(t);
			menusys.setType(1);
			menusys.setSequence(1);
			this.systemMenuService.addSystemMenu(menusys);
			//菜单
			SystemMenu menumenu = new SystemMenu();
			menumenu.setSn("sys_menu");
			menumenu.setTitle("菜单管理");
			menumenu.setPack("sys");
			menumenu.setAppClass("SystemMenuManagePanel");
			menumenu.setUrl("SystemMenuManagePanel.js");
			menumenu.setTenant(t);
			menumenu.setSystem(true);
			menumenu.setParent(menusys);
			menumenu.setType(1);
			menumenu.setSequence(1);
			this.systemMenuService.addSystemMenu(menumenu);
			
			SystemMenu menutenant = new SystemMenu();
			menutenant.setSn("sys_tenant");
			menutenant.setTitle("租户管理");
			menutenant.setPack("sys");
			menutenant.setAppClass("TenantManagePanel");
			menutenant.setUrl("TenantManagePanel.js");
			menutenant.setTenant(t);
			menutenant.setSystem(true);
			menutenant.setParent(menusys);
			menutenant.setType(1);
			menutenant.setSequence(2);
			this.systemMenuService.addSystemMenu(menutenant);
			
			SystemMenu menuuser = new SystemMenu();
			menuuser.setSn("sys_user");
			menuuser.setTitle("用户管理");
			menuuser.setPack("sys");
			menuuser.setAppClass("UserManagePanel");
			menuuser.setUrl("UserManagePanel.js");
			menuuser.setTenant(t);
			menuuser.setSystem(true);
			menuuser.setParent(menusys);
			menuuser.setType(1);
			menuuser.setSequence(3);
			this.systemMenuService.addSystemMenu(menuuser);
			
			SystemMenu menurole = new SystemMenu();
			menurole.setSn("sys_role");
			menurole.setTitle("角色管理");
			menurole.setPack("sys");
			menurole.setAppClass("RoleManagePanel");
			menurole.setUrl("RoleManagePanel.js");
			menurole.setTenant(t);
			menurole.setSystem(true);
			menurole.setParent(menusys);
			menurole.setType(1);
			menurole.setSequence(4);
			this.systemMenuService.addSystemMenu(menurole);
			
			SystemMenu menupermission = new SystemMenu();
			menupermission.setSn("sys_permission");
			menupermission.setTitle("权限管理");
			menupermission.setPack("sys");
			menupermission.setAppClass("PermissionManagePanel");
			menupermission.setUrl("PermissionManagePanel.js");
			menupermission.setTenant(t);
			menupermission.setSystem(true);
			menupermission.setParent(menusys);
			menupermission.setType(1);
			menupermission.setSequence(5);
			this.systemMenuService.addSystemMenu(menupermission);
			
			SystemMenu menuperresource = new SystemMenu();
			menuperresource.setSn("sys_resource");
			menuperresource.setTitle("资源管理");
			menuperresource.setPack("sys");
			menuperresource.setAppClass("SystemResourcePanel");
			menuperresource.setUrl("SystemResourcePanel.js");
			menuperresource.setTenant(t);
			menuperresource.setSystem(true);
			menuperresource.setParent(menusys);
			menuperresource.setType(1);
			menuperresource.setSequence(6);
			this.systemMenuService.addSystemMenu(menuperresource);
		}
	}
}
