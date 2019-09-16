package com.eastinno.otransos.seafood.distribu.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.seafood.distribu.service.ICommissionWithdrawService;
import com.eastinno.otransos.seafood.distribu.dao.ICommissionWithdrawDAO;


/**
 * CommissionWithdrawServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CommissionWithdrawServiceImpl implements ICommissionWithdrawService{
	@Resource
	private ICommissionWithdrawDAO commissionWithdrawDao;
	
	public void setCommissionWithdrawDao(ICommissionWithdrawDAO commissionWithdrawDao){
		this.commissionWithdrawDao=commissionWithdrawDao;
	}
	
	public Long addCommissionWithdraw(CommissionWithdraw commissionWithdraw) {	
		this.commissionWithdrawDao.save(commissionWithdraw);
		if (commissionWithdraw != null && commissionWithdraw.getId() != null) {
			return commissionWithdraw.getId();
		}
		return null;
	}
	
	public CommissionWithdraw getCommissionWithdraw(Long id) {
		CommissionWithdraw commissionWithdraw = this.commissionWithdrawDao.get(id);
		return commissionWithdraw;
		}
	
	public boolean delCommissionWithdraw(Long id) {	
			CommissionWithdraw commissionWithdraw = this.getCommissionWithdraw(id);
			if (commissionWithdraw != null) {
				this.commissionWithdrawDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCommissionWithdraws(List<Serializable> commissionWithdrawIds) {
		
		for (Serializable id : commissionWithdrawIds) {
			delCommissionWithdraw((Long) id);
		}
		return true;
	}
	
	public IPageList getCommissionWithdrawBy(IQueryObject queryObj) {	
		return this.commissionWithdrawDao.findBy(queryObj);		
	}
	
	public boolean updateCommissionWithdraw(Long id, CommissionWithdraw commissionWithdraw) {
		if (id != null) {
			commissionWithdraw.setId(id);
		} else {
			return false;
		}
		this.commissionWithdrawDao.update(commissionWithdraw);
		return true;
	}	
	
}
