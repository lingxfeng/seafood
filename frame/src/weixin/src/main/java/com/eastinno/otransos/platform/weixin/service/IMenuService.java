package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Menu;
import com.eastinno.otransos.web.tools.IPageList;


public interface IMenuService {
	/**
	 * 保存一个Menu，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addMenu(Menu domain);
	
	/**
	 * 根据一个ID得到Menu
	 * 
	 * @param id
	 * @return
	 */
	Menu getMenu(Long id);
	
	/**
	 * 删除一个Menu
	 * @param id
	 * @return
	 */
	boolean delMenu(Long id);
	
	/**
	 * 批量删除Menu
	 * @param ids
	 * @return
	 */
	boolean batchDelMenus(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Menu
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getMenuBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Menu
	  * @param id 需要更新的Menu的idO
	  * @param dir 需要更新的Menu
	  */
	boolean updateMenu(Long id,Menu entity);
}
