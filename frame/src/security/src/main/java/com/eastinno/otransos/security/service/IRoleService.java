package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * RoleService
 * @author ksmwly@gmail.com
 */
public interface IRoleService {
	/**
	 * 保存一个Role，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRole(Role domain);
	
	/**
	 * 根据一个ID得到Role
	 * 
	 * @param id
	 * @return
	 */
	Role getRole(Long id);
	
	/**
	 * 删除一个Role
	 * @param id
	 * @return
	 */
	boolean delRole(Long id);
	
	/**
	 * 批量删除Role
	 * @param ids
	 * @return
	 */
	boolean batchDelRoles(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Role
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRoleBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Role
	  * @param id 需要更新的Role的id
	  * @param dir 需要更新的Role
	  */
	boolean updateRole(Long id,Role entity);
}
