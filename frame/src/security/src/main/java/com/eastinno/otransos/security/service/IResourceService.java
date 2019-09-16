package com.eastinno.otransos.security.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.security.domain.Resource;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * ResourceService
 * @author ksmwly@gmail.com
 */
public interface IResourceService {
	/**
	 * 保存一个Resource，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addResource(Resource domain);
	
	/**
	 * 根据一个ID得到Resource
	 * 
	 * @param id
	 * @return
	 */
	Resource getResource(Long id);
	
	/**
	 * 删除一个Resource
	 * @param id
	 * @return
	 */
	boolean delResource(Long id);
	
	/**
	 * 批量删除Resource
	 * @param ids
	 * @return
	 */
	boolean batchDelResources(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Resource
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getResourceBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Resource
	  * @param id 需要更新的Resource的id
	  * @param dir 需要更新的Resource
	  */
	boolean updateResource(Long id,Resource entity);
	com.eastinno.otransos.security.domain.Resource getResource(String resString);
}
