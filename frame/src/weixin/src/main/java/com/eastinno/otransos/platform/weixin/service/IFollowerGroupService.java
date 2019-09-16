package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.FollowerGroup;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * FollowerGroupService
 * @author ksmwly@gmail.com
 */
public interface IFollowerGroupService {
	/**
	 * 保存一个FollowerGroup，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addFollowerGroup(FollowerGroup domain);
	
	/**
	 * 根据一个ID得到FollowerGroup
	 * 
	 * @param id
	 * @return
	 */
	FollowerGroup getFollowerGroup(Long id);
	
	/**
	 * 删除一个FollowerGroup
	 * @param id
	 * @return
	 */
	boolean delFollowerGroup(Long id);
	
	/**
	 * 批量删除FollowerGroup
	 * @param ids
	 * @return
	 */
	boolean batchDelFollowerGroups(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到FollowerGroup
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getFollowerGroupBy(IQueryObject queryObj);
	
	/**
	  * 更新一个FollowerGroup
	  * @param id 需要更新的FollowerGroup的id
	  * @param dir 需要更新的FollowerGroup
	  */
	boolean updateFollowerGroup(Long id,FollowerGroup entity);
}
