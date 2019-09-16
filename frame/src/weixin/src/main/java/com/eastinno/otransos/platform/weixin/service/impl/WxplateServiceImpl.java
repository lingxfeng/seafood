package com.eastinno.otransos.platform.weixin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IAccountDAO;
import com.eastinno.otransos.platform.weixin.dao.IAutoResponseDAO;
import com.eastinno.otransos.platform.weixin.dao.IFollowerDAO;
import com.eastinno.otransos.platform.weixin.dao.IFollowerMessageDAO;
import com.eastinno.otransos.platform.weixin.dao.IMenuDAO;
import com.eastinno.otransos.platform.weixin.dao.INewsItemDAO;
import com.eastinno.otransos.platform.weixin.dao.ISubscribeDAO;
import com.eastinno.otransos.platform.weixin.dao.ITemplateDAO;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.platform.weixin.service.IWxplatService;
import com.eastinno.otransos.web.tools.IPageList;
@Service
public class WxplateServiceImpl implements IWxplatService{
	@Resource
	private IAccountDAO accountDao;
	@Resource
	private IAutoResponseDAO autoResponseDao;
	@Resource
	private IFollowerMessageDAO followerMessageDao;
	@Resource
	private IFollowerDAO followerDao;
	@Resource
	private IMenuDAO menuDao;
	@Resource
	private INewsItemDAO newsItemDao;
	@Resource
	private ISubscribeDAO subscribeDao;
	@Resource
	private ITemplateDAO templateDao;
	@Override
	public Long addFollower(Follower follower) {
		this.followerDao.save(follower);
		if (follower != null && follower.getId() != null) {
			return follower.getId();
		}
		return null;
	}

	@Override
	public Follower getFollower(Long id) {
		Follower follower = this.followerDao.get(id);
		return follower;
	}

	@Override
	public IPageList getFollowerBy(IQueryObject queryObj) {
		return this.followerDao.findBy(queryObj);
	}

	@Override
	public boolean updateFollower(Long id, Follower follower) {
		if (id != null) {
			follower.setId(id);
		} else {
			return false;
		}
		this.followerDao.update(follower);
		return true;
	}

	@Override
	public Long addFollowerMessage(FollowerMessage followerMessage) {
		this.followerMessageDao.save(followerMessage);
		if (followerMessage != null && followerMessage.getId() != null) {
			return followerMessage.getId();
		}
		return null;
	}

	@Override
	public boolean updateAccount(Long id, Account account) {
		if (id != null) {
			account.setId(id);
		} else {
			return false;
		}
		this.accountDao.update(account);
		return true;
	}

	@Override
	public IPageList getNewsItemBy(IQueryObject queryObj) {
		return this.newsItemDao.findBy(queryObj);
	}

	@Override
	public IPageList getAutoResponseBy(IQueryObject queryObj) {
		return this.autoResponseDao.findBy(queryObj);
	}

	@Override
	public IPageList getSubscribeBy(IQueryObject queryObj) {	
		return this.subscribeDao.findBy(queryObj);	
	}

	@Override
	public IPageList getMenuBy(IQueryObject queryObj) {	
		return this.menuDao.findBy(queryObj);	
	}

	@Override
	public IPageList getFollowerMessageBy(IQueryObject queryObj) {	
		return this.followerMessageDao.findBy(queryObj);		
	}

	@Override
	public IPageList getAccountBy(IQueryObject queryObj) {	
		return this.accountDao.findBy(queryObj);	
	}

	@Override
	public boolean updateTemplate(Long id, Template entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.templateDao.update(entity);
		return true;
	}
}
