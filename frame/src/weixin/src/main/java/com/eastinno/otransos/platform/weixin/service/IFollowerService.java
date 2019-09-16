package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;


public interface IFollowerService {
	/**
	 * 保存一个Follower，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addFollower(Follower domain);
	
	/**
	 * 根据一个ID得到Follower
	 * 
	 * @param id
	 * @return
	 */
	Follower getFollower(Long id);
	
	/**
	 * 删除一个Follower
	 * @param id
	 * @return
	 */
	boolean delFollower(Long id);
	
	/**
	 * 批量删除Follower
	 * @param ids
	 * @return
	 */
	boolean batchDelFollowers(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Follower
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getFollowerBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Follower
	  * @param id 需要更新的Follower的id
	  * @param dir 需要更新的Follower
	  */
	boolean updateFollower(Long id,Follower entity);
	/**
	 * 同步微信服务器上所有用户
	 * @param account
	 * @param next_openid
	 */
	void updateWxUser(Account account, String next_openid);
	/**
	 * 通过微信code获取粉丝
	 * @param form
	 * @return
	 */
	Follower getFollowerByCode(Account account,String code);
	/**
	 * 通过session获取follower
	 * @return
	 */
	Follower getFollowerBySession();
}
