package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.ISubscribeDAO;
import com.eastinno.otransos.platform.weixin.domain.Subscribe;
import com.eastinno.otransos.platform.weixin.service.ISubscribeService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * SubscribeServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SubscribeServiceImpl implements ISubscribeService{
	@Resource
	private ISubscribeDAO subscribeDao;
	
	public void setsubscribeDao(ISubscribeDAO subscribeDao){
		this.subscribeDao=subscribeDao;
	}
	
	public Long addSubscribe(Subscribe entity) {
		this.subscribeDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public Subscribe getSubscribe(Long id) {
		Subscribe entity = this.subscribeDao.get(id);
		return entity;
		}
	
	
	public IPageList getSubscribeBy(IQueryObject queryObj) {	
		return this.subscribeDao.findBy(queryObj);	
	}
	
	public boolean updateSubscribe(Long id, Subscribe entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.subscribeDao.update(entity);
		return true;
	}

	@Override
	public boolean delSubscribe(Long id) {
		Subscribe entity = this.getSubscribe(id);
        if (entity != null) {
            this.subscribeDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelSubscribes(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
