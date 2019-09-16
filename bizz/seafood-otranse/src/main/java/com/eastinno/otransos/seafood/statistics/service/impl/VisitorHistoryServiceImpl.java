package com.eastinno.otransos.seafood.statistics.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.statistics.domain.VisitorHistory;
import com.eastinno.otransos.seafood.statistics.service.IVisitorHistoryService;
import com.eastinno.otransos.seafood.statistics.dao.IVisitorHistoryDAO;


/**
 * VisitorHistoryServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class VisitorHistoryServiceImpl implements IVisitorHistoryService{
	@Resource
	private IVisitorHistoryDAO visitorHistoryDao;
	
	public void setVisitorHistoryDao(IVisitorHistoryDAO visitorHistoryDao){
		this.visitorHistoryDao=visitorHistoryDao;
	}
	
	public Long addVisitorHistory(VisitorHistory visitorHistory) {	
		this.visitorHistoryDao.save(visitorHistory);
		if (visitorHistory != null && visitorHistory.getId() != null) {
			return visitorHistory.getId();
		}
		return null;
	}
	
	public VisitorHistory getVisitorHistory(Long id) {
		VisitorHistory visitorHistory = this.visitorHistoryDao.get(id);
		return visitorHistory;
		}
	
	public boolean delVisitorHistory(Long id) {	
			VisitorHistory visitorHistory = this.getVisitorHistory(id);
			if (visitorHistory != null) {
				this.visitorHistoryDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelVisitorHistorys(List<Serializable> visitorHistoryIds) {
		
		for (Serializable id : visitorHistoryIds) {
			delVisitorHistory((Long) id);
		}
		return true;
	}
	
	public IPageList getVisitorHistoryBy(IQueryObject queryObj) {	
		return this.visitorHistoryDao.findBy(queryObj);		
	}
	
	public boolean updateVisitorHistory(Long id, VisitorHistory visitorHistory) {
		if (id != null) {
			visitorHistory.setId(id);
		} else {
			return false;
		}
		this.visitorHistoryDao.update(visitorHistory);
		return true;
	}	
	
}
