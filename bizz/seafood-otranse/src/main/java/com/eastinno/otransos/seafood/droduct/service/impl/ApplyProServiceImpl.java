package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ApplyPro;
import com.eastinno.otransos.seafood.droduct.service.IApplyProService;
import com.eastinno.otransos.seafood.droduct.dao.IApplyProDAO;


/**
 * ApplyProServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ApplyProServiceImpl implements IApplyProService{
	@Resource
	private IApplyProDAO applyProDao;
	
	public void setApplyProDao(IApplyProDAO applyProDao){
		this.applyProDao=applyProDao;
	}
	
	public Long addApplyPro(ApplyPro applyPro) {	
		this.applyProDao.save(applyPro);
		if (applyPro != null && applyPro.getId() != null) {
			return applyPro.getId();
		}
		return null;
	}
	
	public ApplyPro getApplyPro(Long id) {
		ApplyPro applyPro = this.applyProDao.get(id);
		return applyPro;
		}
	
	public boolean delApplyPro(Long id) {	
			ApplyPro applyPro = this.getApplyPro(id);
			if (applyPro != null) {
				this.applyProDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelApplyPros(List<Serializable> applyProIds) {
		
		for (Serializable id : applyProIds) {
			delApplyPro((Long) id);
		}
		return true;
	}
	
	public IPageList getApplyProBy(IQueryObject queryObj) {	
		return this.applyProDao.findBy(queryObj);		
	}
	
	public boolean updateApplyPro(Long id, ApplyPro applyPro) {
		if (id != null) {
			applyPro.setId(id);
		} else {
			return false;
		}
		this.applyProDao.update(applyPro);
		return true;
	}	
	
}
