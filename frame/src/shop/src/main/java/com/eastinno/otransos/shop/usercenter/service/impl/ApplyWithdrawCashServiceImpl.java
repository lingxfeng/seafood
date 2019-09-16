package com.eastinno.otransos.shop.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.shop.usercenter.service.IApplyWithdrawCashService;
import com.eastinno.otransos.shop.usercenter.dao.IApplyWithdrawCashDAO;


/**
 * ApplyWithdrawCashServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ApplyWithdrawCashServiceImpl implements IApplyWithdrawCashService{
	@Resource
	private IApplyWithdrawCashDAO applyWithdrawCashDao;
	
	public void setApplyWithdrawCashDao(IApplyWithdrawCashDAO applyWithdrawCashDao){
		this.applyWithdrawCashDao=applyWithdrawCashDao;
	}
	
	public Long addApplyWithdrawCash(ApplyWithdrawCash applyWithdrawCash) {	
		this.applyWithdrawCashDao.save(applyWithdrawCash);
		if (applyWithdrawCash != null && applyWithdrawCash.getId() != null) {
			return applyWithdrawCash.getId();
		}
		return null;
	}
	
	public ApplyWithdrawCash getApplyWithdrawCash(Long id) {
		ApplyWithdrawCash applyWithdrawCash = this.applyWithdrawCashDao.get(id);
		return applyWithdrawCash;
		}
	
	public boolean delApplyWithdrawCash(Long id) {	
			ApplyWithdrawCash applyWithdrawCash = this.getApplyWithdrawCash(id);
			if (applyWithdrawCash != null) {
				this.applyWithdrawCashDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelApplyWithdrawCashs(List<Serializable> applyWithdrawCashIds) {
		
		for (Serializable id : applyWithdrawCashIds) {
			delApplyWithdrawCash((Long) id);
		}
		return true;
	}
	
	public IPageList getApplyWithdrawCashBy(IQueryObject queryObj) {	
		return this.applyWithdrawCashDao.findBy(queryObj);		
	}
	
	public boolean updateApplyWithdrawCash(Long id, ApplyWithdrawCash applyWithdrawCash) {
		if (id != null) {
			applyWithdrawCash.setId(id);
		} else {
			return false;
		}
		this.applyWithdrawCashDao.update(applyWithdrawCash);
		return true;
	}	
	
}
