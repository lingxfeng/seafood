package com.eastinno.otransos.security.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.dao.ISystemMenuDAO;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.security.service.ISystemMenuService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * SystemMenuServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SystemMenuServiceImpl implements ISystemMenuService{
	@Resource
	private ISystemMenuDAO systemMenuDao;
	
	public void setSystemMenuDao(ISystemMenuDAO systemMenuDao){
		this.systemMenuDao=systemMenuDao;
	}
	
	public Long addSystemMenu(SystemMenu systemMenu) {	
		this.systemMenuDao.save(systemMenu);
		if (systemMenu != null && systemMenu.getId() != null) {
			return systemMenu.getId();
		}
		return null;
	}
	
	public SystemMenu getSystemMenu(Long id) {
		SystemMenu systemMenu = this.systemMenuDao.get(id);
		return systemMenu;
		}
	
	public boolean delSystemMenu(Long id) {	
			SystemMenu systemMenu = this.getSystemMenu(id);
			if (systemMenu != null) {
				this.systemMenuDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSystemMenus(List<Serializable> systemMenuIds) {
		
		for (Serializable id : systemMenuIds) {
			delSystemMenu((Long) id);
		}
		return true;
	}
	
	public IPageList getSystemMenuBy(IQueryObject queryObj) {	
		return this.systemMenuDao.findBy(queryObj);		
	}
	
	public boolean updateSystemMenu(Long id, SystemMenu systemMenu) {
		if (id != null) {
			systemMenu.setId(id);
		} else {
			return false;
		}
		this.systemMenuDao.update(systemMenu);
		return true;
	}	
	
}
