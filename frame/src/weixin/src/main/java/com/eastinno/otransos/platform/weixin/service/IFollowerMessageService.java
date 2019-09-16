package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * FollowerMessageService
 * @author ksmwly@gmail.com
 */
public interface IFollowerMessageService {
	/**
	 * 保存一个FollowerMessage，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addFollowerMessage(FollowerMessage domain);
	
	/**
	 * 根据一个ID得到FollowerMessage
	 * 
	 * @param id
	 * @return
	 */
	FollowerMessage getFollowerMessage(Long id);
	
	/**
	 * 删除一个FollowerMessage
	 * @param id
	 * @return
	 */
	boolean delFollowerMessage(Long id);
	
	/**
	 * 批量删除FollowerMessage
	 * @param ids
	 * @return
	 */
	boolean batchDelFollowerMessages(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到FollowerMessage
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getFollowerMessageBy(IQueryObject queryObj);
	
	/**
	  * 更新一个FollowerMessage
	  * @param id 需要更新的FollowerMessage的id
	  * @param dir 需要更新的FollowerMessage
	  */
	boolean updateFollowerMessage(Long id,FollowerMessage entity);
}
