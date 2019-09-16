package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.SystemMenu;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * SystemMenuService
 * @author ksmwly@gmail.com
 */
public interface ISystemMenuService {
	/**
	 * 保存一个SystemMenu，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSystemMenu(SystemMenu domain);
	
	/**
	 * 根据一个ID得到SystemMenu
	 * 
	 * @param id
	 * @return
	 */
	SystemMenu getSystemMenu(Long id);
	
	/**
	 * 删除一个SystemMenu
	 * @param id
	 * @return
	 */
	boolean delSystemMenu(Long id);
	
	/**
	 * 批量删除SystemMenu
	 * @param ids
	 * @return
	 */
	boolean batchDelSystemMenus(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SystemMenu
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSystemMenuBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SystemMenu
	  * @param id 需要更新的SystemMenu的id
	  * @param dir 需要更新的SystemMenu
	  */
	boolean updateSystemMenu(Long id,SystemMenu entity);
}
