package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IShareHistoryDAO;
import com.eastinno.otransos.platform.weixin.domain.ShareHistory;
import com.eastinno.otransos.platform.weixin.service.IShareHistoryService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * ShareHistoryServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShareHistoryServiceImpl implements IShareHistoryService{
	@Resource
	private IShareHistoryDAO shareHistoryDao;
	
	public void setshareHistoryDao(IShareHistoryDAO shareHistoryDao){
		this.shareHistoryDao=shareHistoryDao;
	}
	
	public Long addShareHistory(ShareHistory entity) {
		this.shareHistoryDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public ShareHistory getShareHistory(Long id) {
		ShareHistory entity = this.shareHistoryDao.get(id);
		return entity;
		}
	
	
	public IPageList getShareHistoryBy(IQueryObject queryObj) {	
		return this.shareHistoryDao.findBy(queryObj);	
	}
	
	public boolean updateShareHistory(Long id, ShareHistory entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.shareHistoryDao.update(entity);
		return true;
	}

	@Override
	public boolean delShareHistory(Long id) {
		ShareHistory entity = this.getShareHistory(id);
        if (entity != null) {
            this.shareHistoryDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelShareHistorys(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
