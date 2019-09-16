package com.eastinno.otransos.platform.weixin.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
/**
 * 初始化
 * @author nsz
 */
@Component
public class InitWxMenuService {
	@Autowired
	private IUserService userService;
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private ISystemMenuService systemMenuService;

	@PostConstruct
	public void init() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.sn","wx","=");
		qo.setPageSize(1);
		List<?> wxMenus = this.systemMenuService.getSystemMenuBy(qo).getResult();
		if(wxMenus==null || wxMenus.size()==0){
			qo = new QueryObject();
			qo.addQuery("obj.parent is EMPTY");
			qo.setPageSize(1);
			List<Tenant> ts = this.tenantService.getTenantBy(qo).getResult();
			if(ts!=null && ts.size()>0){
				Tenant t = ts.get(0);
				SystemMenu menu3 = new SystemMenu();
				menu3.setSn("wx");
				menu3.setTitle("微信基础配置");
				menu3.setSystem(true);
				menu3.setTenant(t);
				menu3.setType(1);
				menu3.setSequence(3);
				this.systemMenuService.addSystemMenu(menu3);
				
				SystemMenu menu31 = new SystemMenu();
				menu31.setSn("wx_account");
				menu31.setTitle("公众账号管理");
				menu31.setPack("weixin");
				menu31.setAppClass("AccountGridListPanel");
				menu31.setUrl("AccountManagePanel.js");
				menu31.setTenant(t);
				menu31.setSystem(true);
				menu31.setParent(menu3);
				menu31.setType(1);
				menu31.setSequence(1);
				this.systemMenuService.addSystemMenu(menu31);
				
				SystemMenu menu32 = new SystemMenu();
				menu32.setSn("wx_template");
				menu32.setTitle("消息模板管理");
				menu32.setPack("weixin");
				menu32.setAppClass("TemplateManagePanel");
				menu32.setUrl("TemplateManagePanel.js");
				menu32.setTenant(t);
				menu32.setSystem(true);
				menu32.setParent(menu3);
				menu32.setType(1);
				menu32.setSequence(2);
				this.systemMenuService.addSystemMenu(menu32);
				
				SystemMenu menu33 = new SystemMenu();
				menu33.setSn("wx_subscribe");
				menu33.setTitle("首次关注回复");
				menu33.setPack("weixin");
				menu33.setAppClass("SubscribeManagePanel");
				menu33.setUrl("SubscribeManagePanel.js");
				menu33.setTenant(t);
				menu33.setSystem(true);
				menu33.setParent(menu3);
				menu33.setType(1);
				menu33.setSequence(3);
				this.systemMenuService.addSystemMenu(menu33);
				
				SystemMenu menu34 = new SystemMenu();
				menu34.setSn("wx_autoResponse");
				menu34.setTitle("关键字词回复");
				menu34.setPack("weixin");
				menu34.setAppClass("AutoResponseManagePanel");
				menu34.setUrl("AutoResponseManagePanel.js");
				menu34.setTenant(t);
				menu34.setSystem(true);
				menu34.setParent(menu3);
				menu34.setType(1);
				menu34.setSequence(4);
				this.systemMenuService.addSystemMenu(menu34);
				
				SystemMenu menu35 = new SystemMenu();
				menu35.setSn("wx_menu");
				menu35.setTitle("微信菜单管理");
				menu35.setPack("weixin");
				menu35.setAppClass("MenuManagePanel");
				menu35.setUrl("MenuManagePanel.js");
				menu35.setTenant(t);
				menu35.setSystem(true);
				menu35.setParent(menu3);
				menu35.setType(1);
				menu35.setSequence(5);
				this.systemMenuService.addSystemMenu(menu35);
				
				SystemMenu menu36 = new SystemMenu();
				menu36.setSn("wx_follower");
				menu36.setTitle("我的粉丝群体");
				menu36.setPack("weixin");
				menu36.setAppClass("FollowerManagePanel");
				menu36.setUrl("FollowerManagePanel.js");
				menu36.setTenant(t);
				menu36.setSystem(true);
				menu36.setParent(menu3);
				menu36.setType(1);
				menu36.setSequence(6);
				this.systemMenuService.addSystemMenu(menu36);
				
				SystemMenu menu37 = new SystemMenu();
				menu37.setSn("wx_kefuxiaoxi");
				menu37.setTitle("客服消息管理");
				menu37.setPack("weixin");
				menu37.setAppClass("FollowerMsgManagePanel");
				menu37.setUrl("FollowerMsgManagePanel.js");
				menu37.setTenant(t);
				menu37.setSystem(true);
				menu37.setParent(menu3);
				menu37.setType(1);
				menu37.setSequence(7);
				this.systemMenuService.addSystemMenu(menu37);
				
				SystemMenu menu38 = new SystemMenu();
				menu38.setSn("wx_timing");
				menu38.setTitle("微信定时推送");
				menu38.setPack("weixin");
				menu38.setAppClass("TimedTaskManagePanel");
				menu38.setUrl("TimedTaskManagePanel.js");
				menu38.setTenant(t);
				menu38.setSystem(true);
				menu38.setParent(menu3);
				menu38.setType(1);
				menu38.setSequence(8);
				this.systemMenuService.addSystemMenu(menu38);
				
				SystemMenu menu39 = new SystemMenu();
				menu39.setSn("wx_followerGroup");
				menu39.setTitle("用户分组");
				menu39.setPack("weixin");
				menu39.setAppClass("GroupManagePanel");
				menu39.setUrl("GroupManagePanel.js");
				menu39.setTenant(t);
				menu39.setSystem(true);
				menu39.setParent(menu3);
				menu39.setType(1);
				menu39.setSequence(9);
				this.systemMenuService.addSystemMenu(menu39);
			}
		}
	}
}
