package com.eastinno.otransos.seafood.statistics.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.statistics.domain.VisitorReffer;
import com.eastinno.otransos.seafood.statistics.service.IVisitorRefferService;
import com.eastinno.otransos.seafood.statistics.dao.IVisitorRefferDAO;


/**
 * VisitorRefferServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class VisitorRefferServiceImpl implements IVisitorRefferService{
	@Resource
	private IVisitorRefferDAO visitorRefferDao;
	
	public void setVisitorRefferDao(IVisitorRefferDAO visitorRefferDao){
		this.visitorRefferDao=visitorRefferDao;
	}
	
	public Long addVisitorReffer(VisitorReffer visitorReffer) {	
		this.visitorRefferDao.save(visitorReffer);
		if (visitorReffer != null && visitorReffer.getId() != null) {
			return visitorReffer.getId();
		}
		return null;
	}
	
	public VisitorReffer getVisitorReffer(Long id) {
		VisitorReffer visitorReffer = this.visitorRefferDao.get(id);
		return visitorReffer;
		}
	
	public boolean delVisitorReffer(Long id) {	
			VisitorReffer visitorReffer = this.getVisitorReffer(id);
			if (visitorReffer != null) {
				this.visitorRefferDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelVisitorReffers(List<Serializable> visitorRefferIds) {
		
		for (Serializable id : visitorRefferIds) {
			delVisitorReffer((Long) id);
		}
		return true;
	}
	
	public IPageList getVisitorRefferBy(IQueryObject queryObj) {	
		return this.visitorRefferDao.findBy(queryObj);		
	}
	
	public boolean updateVisitorReffer(Long id, VisitorReffer visitorReffer) {
		if (id != null) {
			visitorReffer.setId(id);
		} else {
			return false;
		}
		this.visitorRefferDao.update(visitorReffer);
		return true;
	}	
	
}
