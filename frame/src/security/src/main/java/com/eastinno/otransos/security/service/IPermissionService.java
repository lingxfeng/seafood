package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * PermissionService
 * @author ksmwly@gmail.com
 */
public interface IPermissionService {
	/**
	 * 保存一个Permission，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addPermission(Permission domain);
	
	/**
	 * 根据一个ID得到Permission
	 * 
	 * @param id
	 * @return
	 */
	Permission getPermission(Long id);
	
	/**
	 * 删除一个Permission
	 * @param id
	 * @return
	 */
	boolean delPermission(Long id);
	
	/**
	 * 批量删除Permission
	 * @param ids
	 * @return
	 */
	boolean batchDelPermissions(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Permission
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getPermissionBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Permission
	  * @param id 需要更新的Permission的id
	  * @param dir 需要更新的Permission
	  */
	boolean updatePermission(Long id,Permission entity);
}
