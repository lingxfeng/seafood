package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IAutoResponseDAO;
import com.eastinno.otransos.platform.weixin.domain.AutoResponse;
import com.eastinno.otransos.platform.weixin.service.IAutoResponseService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * AutoResponseServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class AutoResponseServiceImpl implements IAutoResponseService{
	@Resource
	private IAutoResponseDAO autoResponseDao;
	
	public void setautoResponseDao(IAutoResponseDAO autoResponseDao){
		this.autoResponseDao=autoResponseDao;
	}
	
	public Long addAutoResponse(AutoResponse entity) {
		this.autoResponseDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public AutoResponse getAutoResponse(Long id) {
		AutoResponse entity = this.autoResponseDao.get(id);
		return entity;
		}
	
	
	public IPageList getAutoResponseBy(IQueryObject queryObj) {	
		return this.autoResponseDao.findBy(queryObj);	
	}
	
	public boolean updateAutoResponse(Long id, AutoResponse entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.autoResponseDao.update(entity);
		return true;
	}

	@Override
	public boolean delAutoResponse(Long id) {
		AutoResponse entity = this.getAutoResponse(id);
        if (entity != null) {
            this.autoResponseDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelAutoResponses(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
