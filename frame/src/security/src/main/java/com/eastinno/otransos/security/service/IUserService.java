package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * UserService
 * @author ksmwly@gmail.com
 */
public interface IUserService {
	/**
	 * 保存一个User，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addUser(User domain);
	
	/**
	 * 根据一个ID得到User
	 * 
	 * @param id
	 * @return
	 */
	User getUser(Long id);
	
	/**
	 * 删除一个User
	 * @param id
	 * @return
	 */
	boolean delUser(Long id);
	
	/**
	 * 批量删除User
	 * @param ids
	 * @return
	 */
	boolean batchDelUsers(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到User
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getUserBy(IQueryObject queryObj);
	
	/**
	  * 更新一个User
	  * @param id 需要更新的User的id
	  * @param dir 需要更新的User
	  */
	boolean updateUser(Long id,User entity);
	/**
	 * 根据租户添加
	 * @param t
	 * @return
	 */
	boolean addUserByTenant(Tenant t);
}
