package com.eastinno.otransos.security.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.dao.IUserDAO;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * UserServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class UserServiceImpl implements IUserService{
	@Resource
	private IUserDAO userDao;
	
	public void setUserDao(IUserDAO userDao){
		this.userDao=userDao;
	}
	
	public Long addUser(User user) {	
		String sale = ShiroUtils.getSalt();
		user.setSalt(sale);
		String password = ShiroUtils.getPassWord(ShiroUtils.PASSWORD, sale);
		user.setPassword(password);
		this.userDao.save(user);
		if (user != null && user.getId() != null) {
			return user.getId();
		}
		return null;
	}
	
	public User getUser(Long id) {
		User user = this.userDao.get(id);
		return user;
		}
	
	public boolean delUser(Long id) {	
			User user = this.getUser(id);
			if (user != null) {
				this.userDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelUsers(List<Serializable> userIds) {
		for (Serializable id : userIds) {
			delUser((Long) id);
		}
		return true;
	}
	
	public IPageList getUserBy(IQueryObject queryObj) {
		return this.userDao.findBy(queryObj);		
	}
	
	public boolean updateUser(Long id, User user) {
		if (id != null) {
			user.setId(id);
		} else {
			return false;
		}
		this.userDao.update(user);
		return true;
	}

	@Override
	public boolean addUserByTenant(Tenant t) {
		if(t!=null){
			String code = t.getCode();
			User user = new User();
			user.setName(code);
			user.setType(2);
			user.setCode(code);
			user.setNickname(t.getTitle()+"管理员");
			user.setTrueName(t.getTitle()+"管理员");
			String salt = ShiroUtils.getSalt();
			user.setSalt(salt);
			String password = ShiroUtils.getPassWord("123456", salt);
			user.setPassword(password);
			user.setTenant(t);
			user.setIsTenantAdmin(true);
			this.addUser(user);
		}
		return false;
	}	
	
}
