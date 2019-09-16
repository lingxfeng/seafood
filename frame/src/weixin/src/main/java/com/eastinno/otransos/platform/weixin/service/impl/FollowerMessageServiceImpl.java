package com.eastinno.otransos.platform.weixin.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IFollowerMessageDAO;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.platform.weixin.service.IFollowerMessageService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * FollowerMessageServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class FollowerMessageServiceImpl implements IFollowerMessageService{
	@Resource
	private IFollowerMessageDAO followerMessageDao;
	
	public void setFollowerMessageDao(IFollowerMessageDAO followerMessageDao){
		this.followerMessageDao=followerMessageDao;
	}
	
	public Long addFollowerMessage(FollowerMessage followerMessage) {	
		this.followerMessageDao.save(followerMessage);
		if (followerMessage != null && followerMessage.getId() != null) {
			return followerMessage.getId();
		}
		return null;
	}
	
	public FollowerMessage getFollowerMessage(Long id) {
		FollowerMessage followerMessage = this.followerMessageDao.get(id);
		return followerMessage;
		}
	
	public boolean delFollowerMessage(Long id) {	
			FollowerMessage followerMessage = this.getFollowerMessage(id);
			if (followerMessage != null) {
				this.followerMessageDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelFollowerMessages(List<Serializable> followerMessageIds) {
		
		for (Serializable id : followerMessageIds) {
			delFollowerMessage((Long) id);
		}
		return true;
	}
	
	public IPageList getFollowerMessageBy(IQueryObject queryObj) {	
		return this.followerMessageDao.findBy(queryObj);		
	}
	
	public boolean updateFollowerMessage(Long id, FollowerMessage followerMessage) {
		if (id != null) {
			followerMessage.setId(id);
		} else {
			return false;
		}
		this.followerMessageDao.update(followerMessage);
		return true;
	}	
	
}
