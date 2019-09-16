package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Subscribe;
import com.eastinno.otransos.web.tools.IPageList;


public interface ISubscribeService {
	/**
	 * 保存一个Subscribe，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSubscribe(Subscribe domain);
	
	/**
	 * 根据一个ID得到Subscribe
	 * 
	 * @param id
	 * @return
	 */
	Subscribe getSubscribe(Long id);
	
	/**
	 * 删除一个Subscribe
	 * @param id
	 * @return
	 */
	boolean delSubscribe(Long id);
	
	/**
	 * 批量删除Subscribe
	 * @param ids
	 * @return
	 */
	boolean batchDelSubscribes(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Subscribe
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSubscribeBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Subscribe
	  * @param id 需要更新的Subscribe的idO
	  * @param dir 需要更新的Subscribe
	  */
	boolean updateSubscribe(Long id,Subscribe entity);
}
