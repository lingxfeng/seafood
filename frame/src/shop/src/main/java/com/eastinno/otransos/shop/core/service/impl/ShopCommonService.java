package com.eastinno.otransos.shop.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.security.service.ITenantService;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * 初始化电商菜单
 * @author Administrator
 */
@Component
public class ShopCommonService {
	@Autowired
	private ITenantService tenantService;
	@Autowired
	private ISystemMenuService systemMenuService;
	@PostConstruct
	public void initMenu() throws IOException{
		createShopMenu();
	}
	public void createShopMenu() throws FileNotFoundException, UnsupportedEncodingException{
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.type",Integer.parseInt("2"),"=");
		List<?> list = this.systemMenuService.getSystemMenuBy(qo).getResult();
		if(list==null || list.size()==0){
			String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();  
	        path= path.replace("file:", ""); //去掉file:  
	        path=path.replace("classes/", ""); //去掉class\
			File f = new File(path+"shopmenu.xml");
			if(f.exists()){
				qo = new QueryObject();
				qo.addQuery("obj.parent IS EMPTY");
				List<Tenant> ts = tenantService.getTenantBy(qo).getResult();
				Tenant t=null;
				if(ts!=null && ts.size()>0){
					t = ts.get(0);
				}
				createSystemShopMenu(t);
				//解析xml
				InputStreamReader o =  new InputStreamReader(new FileInputStream(f),"UTF-8");
				XStream xstream = new XStream(new DomDriver ());
				xstream.alias("menu", SystemMenu.class);
				SystemMenu obj = (SystemMenu) xstream.fromXML(o);
				List<SystemMenu> menus = obj.getChildren();
				createMenu(null,menus, t);
			}
		}
	}
	public void createMenu(SystemMenu parent,List<SystemMenu> menus,Tenant t){
		if(menus!=null && menus.size()>0){
			int si=1;
			for(SystemMenu shm:menus){
				shm.setSystem(true);
				shm.setTenant(t);
				shm.setSequence(si);
				shm.setStatus(0);
				shm.setParent(parent);
				shm.setType(2);
				si++;
				this.systemMenuService.addSystemMenu(shm);
				List<SystemMenu> chilren = shm.getChildren();
				createMenu(shm,chilren, t);
			}
		}
	}
	public void createSystemShopMenu(Tenant t){
		SystemMenu sm = new SystemMenu();
		sm.setTitle("电商基础配置");
		sm.setSn("shop");
		sm.setSystem(true);
		sm.setSequence(2);
		sm.setTenant(t);
		this.systemMenuService.addSystemMenu(sm);
		SystemMenu shopMenu = new SystemMenu();
		shopMenu.setTitle("电商菜单管理");
		shopMenu.setSn("shop_menu");
		shopMenu.setSystem(true);
		shopMenu.setSequence(1);
		shopMenu.setTenant(t);
		shopMenu.setParent(sm);
		shopMenu.setPack("shop");
		shopMenu.setAppClass("ShopMenuManagePanel");
		shopMenu.setUrl("ShopMenuManagePanel.js");
		this.systemMenuService.addSystemMenu(shopMenu);
		SystemMenu shopMember = new SystemMenu();
		shopMember.setTitle("电商权限管理");
		shopMember.setSn("shop_userMenu");
		shopMember.setSystem(true);
		shopMember.setSequence(2);
		shopMember.setTenant(t);
		shopMember.setParent(sm);
		shopMember.setPack("shop");
		shopMember.setAppClass("BusinessManagePanel");
		shopMember.setUrl("BusinessManagePanel.js");
		this.systemMenuService.addSystemMenu(shopMember);
		
	}
}
