package com.eastinno.otransos.platform.weixin.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IFollowerGroupDAO;
import com.eastinno.otransos.platform.weixin.domain.FollowerGroup;
import com.eastinno.otransos.platform.weixin.service.IFollowerGroupService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * FollowerGroupServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class FollowerGroupServiceImpl implements IFollowerGroupService{
	@Resource
	private IFollowerGroupDAO followerGroupDao;
	
	public void setFollowerGroupDao(IFollowerGroupDAO followerGroupDao){
		this.followerGroupDao=followerGroupDao;
	}
	
	public Long addFollowerGroup(FollowerGroup followerGroup) {	
		this.followerGroupDao.save(followerGroup);
		if (followerGroup != null && followerGroup.getId() != null) {
			return followerGroup.getId();
		}
		return null;
	}
	
	public FollowerGroup getFollowerGroup(Long id) {
		FollowerGroup followerGroup = this.followerGroupDao.get(id);
		return followerGroup;
		}
	
	public boolean delFollowerGroup(Long id) {	
			FollowerGroup followerGroup = this.getFollowerGroup(id);
			if (followerGroup != null) {
				this.followerGroupDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelFollowerGroups(List<Serializable> followerGroupIds) {
		
		for (Serializable id : followerGroupIds) {
			delFollowerGroup((Long) id);
		}
		return true;
	}
	
	public IPageList getFollowerGroupBy(IQueryObject queryObj) {	
		return this.followerGroupDao.findBy(queryObj);		
	}
	
	public boolean updateFollowerGroup(Long id, FollowerGroup followerGroup) {
		if (id != null) {
			followerGroup.setId(id);
		} else {
			return false;
		}
		this.followerGroupDao.update(followerGroup);
		return true;
	}	
	
}
