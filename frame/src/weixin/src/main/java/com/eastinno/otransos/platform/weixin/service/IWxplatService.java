package com.eastinno.otransos.platform.weixin.service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.web.tools.IPageList;

public interface IWxplatService {
	Long addFollower(Follower domain);
	
	Follower getFollower(Long id);
	IPageList getFollowerBy(IQueryObject queryObj);
	boolean updateFollower(Long id,Follower entity);
	
	Long addFollowerMessage(FollowerMessage domain);
	
	boolean updateAccount(Long id,Account entity);
	
	IPageList getNewsItemBy(IQueryObject queryObj);
	
	IPageList getAutoResponseBy(IQueryObject queryObj);
	IPageList getSubscribeBy(IQueryObject queryObj);
	IPageList getMenuBy(IQueryObject queryObj);
	IPageList getFollowerMessageBy(IQueryObject queryObj);
	IPageList getAccountBy(IQueryObject queryObj);
	boolean updateTemplate(Long id,Template entity);
}
