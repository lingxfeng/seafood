package com.eastinno.otransos.seafood.spokesman.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.spokesman.domain.SpecialAllowance;
import com.eastinno.otransos.seafood.spokesman.service.ISpecialAllowanceService;
import com.eastinno.otransos.seafood.spokesman.dao.ISpecialAllowanceDAO;


/**
 * SpecialAllowanceServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SpecialAllowanceServiceImpl implements ISpecialAllowanceService{
	@Resource
	private ISpecialAllowanceDAO specialAllowanceDao;
	
	public void setSpecialAllowanceDao(ISpecialAllowanceDAO specialAllowanceDao){
		this.specialAllowanceDao=specialAllowanceDao;
	}
	
	public Long addSpecialAllowance(SpecialAllowance specialAllowance) {	
		this.specialAllowanceDao.save(specialAllowance);
		if (specialAllowance != null && specialAllowance.getId() != null) {
			return specialAllowance.getId();
		}
		return null;
	}
	
	public SpecialAllowance getSpecialAllowance(Long id) {
		SpecialAllowance specialAllowance = this.specialAllowanceDao.get(id);
		return specialAllowance;
		}
	
	public boolean delSpecialAllowance(Long id) {	
			SpecialAllowance specialAllowance = this.getSpecialAllowance(id);
			if (specialAllowance != null) {
				this.specialAllowanceDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSpecialAllowances(List<Serializable> specialAllowanceIds) {
		
		for (Serializable id : specialAllowanceIds) {
			delSpecialAllowance((Long) id);
		}
		return true;
	}
	
	public IPageList getSpecialAllowanceBy(IQueryObject queryObj) {	
		return this.specialAllowanceDao.findBy(queryObj);		
	}
	
	public boolean updateSpecialAllowance(Long id, SpecialAllowance specialAllowance) {
		if (id != null) {
			specialAllowance.setId(id);
		} else {
			return false;
		}
		this.specialAllowanceDao.update(specialAllowance);
		return true;
	}	
	
}
