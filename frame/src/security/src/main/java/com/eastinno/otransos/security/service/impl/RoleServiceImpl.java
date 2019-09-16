package com.eastinno.otransos.security.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.dao.IRoleDAO;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.service.IRoleService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * RoleServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class RoleServiceImpl implements IRoleService{
	@Resource
	private IRoleDAO roleDao;
	
	public void setRoleDao(IRoleDAO roleDao){
		this.roleDao=roleDao;
	}
	
	public Long addRole(Role role) {	
		this.roleDao.save(role);
		if (role != null && role.getId() != null) {
			return role.getId();
		}
		return null;
	}
	
	public Role getRole(Long id) {
		Role role = this.roleDao.get(id);
		return role;
		}
	
	public boolean delRole(Long id) {	
			Role role = this.getRole(id);
			if (role != null) {
				this.roleDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelRoles(List<Serializable> roleIds) {
		
		for (Serializable id : roleIds) {
			delRole((Long) id);
		}
		return true;
	}
	
	public IPageList getRoleBy(IQueryObject queryObj) {	
		return this.roleDao.findBy(queryObj);		
	}
	
	public boolean updateRole(Long id, Role role) {
		if (id != null) {
			role.setId(id);
		} else {
			return false;
		}
		this.roleDao.update(role);
		return true;
	}	
	
}
