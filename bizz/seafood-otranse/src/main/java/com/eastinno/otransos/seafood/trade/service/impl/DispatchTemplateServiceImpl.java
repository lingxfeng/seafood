package com.eastinno.otransos.seafood.trade.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.trade.domain.DispatchTemplate;
import com.eastinno.otransos.seafood.trade.service.IDispatchTemplateService;
import com.eastinno.otransos.seafood.trade.dao.IDispatchTemplateDAO;


/**
 * DispatchTemplateServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class DispatchTemplateServiceImpl implements IDispatchTemplateService{
	@Resource
	private IDispatchTemplateDAO dispatchTemplateDao;
	
	public void setDispatchTemplateDao(IDispatchTemplateDAO dispatchTemplateDao){
		this.dispatchTemplateDao=dispatchTemplateDao;
	}
	
	public Long addDispatchTemplate(DispatchTemplate dispatchTemplate) {	
		this.dispatchTemplateDao.save(dispatchTemplate);
		if (dispatchTemplate != null && dispatchTemplate.getId() != null) {
			return dispatchTemplate.getId();
		}
		return null;
	}
	
	public DispatchTemplate getDispatchTemplate(Long id) {
		DispatchTemplate dispatchTemplate = this.dispatchTemplateDao.get(id);
		return dispatchTemplate;
		}
	
	public boolean delDispatchTemplate(Long id) {	
			DispatchTemplate dispatchTemplate = this.getDispatchTemplate(id);
			if (dispatchTemplate != null) {
				this.dispatchTemplateDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelDispatchTemplates(List<Serializable> dispatchTemplateIds) {
		
		for (Serializable id : dispatchTemplateIds) {
			delDispatchTemplate((Long) id);
		}
		return true;
	}
	
	public IPageList getDispatchTemplateBy(IQueryObject queryObj) {	
		return this.dispatchTemplateDao.findBy(queryObj);		
	}
	
	public boolean updateDispatchTemplate(Long id, DispatchTemplate dispatchTemplate) {
		if (id != null) {
			dispatchTemplate.setId(id);
		} else {
			return false;
		}
		this.dispatchTemplateDao.update(dispatchTemplate);
		return true;
	}	
	
}
