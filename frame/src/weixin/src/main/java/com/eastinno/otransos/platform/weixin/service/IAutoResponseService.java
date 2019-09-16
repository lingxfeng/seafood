package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.AutoResponse;
import com.eastinno.otransos.web.tools.IPageList;


public interface IAutoResponseService {
	/**
	 * 保存一个AutoResponse，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addAutoResponse(AutoResponse domain);
	
	/**
	 * 根据一个ID得到AutoResponse
	 * 
	 * @param id
	 * @return
	 */
	AutoResponse getAutoResponse(Long id);
	
	/**
	 * 删除一个AutoResponse
	 * @param id
	 * @return
	 */
	boolean delAutoResponse(Long id);
	
	/**
	 * 批量删除AutoResponse
	 * @param ids
	 * @return
	 */
	boolean batchDelAutoResponses(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到AutoResponse
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getAutoResponseBy(IQueryObject queryObj);
	
	/**
	  * 更新一个AutoResponse
	  * @param id 需要更新的AutoResponse的idO
	  * @param dir 需要更新的AutoResponse
	  */
	boolean updateAutoResponse(Long id,AutoResponse entity);
}
