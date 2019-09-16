package com.eastinno.otransos.security.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.dao.IPermissionDAO;
import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.security.service.IPermissionService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * PermissionServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class PermissionServiceImpl implements IPermissionService{
	@Resource
	private IPermissionDAO permissionDao;
	
	public void setPermissionDao(IPermissionDAO permissionDao){
		this.permissionDao=permissionDao;
	}
	
	public Long addPermission(Permission permission) {	
		this.permissionDao.save(permission);
		if (permission != null && permission.getId() != null) {
			return permission.getId();
		}
		return null;
	}
	
	public Permission getPermission(Long id) {
		Permission permission = this.permissionDao.get(id);
		return permission;
		}
	
	public boolean delPermission(Long id) {	
			Permission permission = this.getPermission(id);
			if (permission != null) {
				this.permissionDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelPermissions(List<Serializable> permissionIds) {
		
		for (Serializable id : permissionIds) {
			delPermission((Long) id);
		}
		return true;
	}
	
	public IPageList getPermissionBy(IQueryObject queryObj) {	
		return this.permissionDao.findBy(queryObj);		
	}
	
	public boolean updatePermission(Long id, Permission permission) {
		if (id != null) {
			permission.setId(id);
		} else {
			return false;
		}
		this.permissionDao.update(permission);
		return true;
	}	
	
}
