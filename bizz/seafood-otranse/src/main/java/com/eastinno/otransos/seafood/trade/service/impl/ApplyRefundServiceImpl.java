package com.eastinno.otransos.seafood.trade.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.trade.domain.ApplyRefund;
import com.eastinno.otransos.seafood.trade.service.IApplyRefundService;
import com.eastinno.otransos.seafood.trade.dao.IApplyRefundDAO;


/**
 * ApplyRefundServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ApplyRefundServiceImpl implements IApplyRefundService{
	@Resource
	private IApplyRefundDAO applyRefundDao;
	
	public void setApplyRefundDao(IApplyRefundDAO applyRefundDao){
		this.applyRefundDao=applyRefundDao;
	}
	
	public Long addApplyRefund(ApplyRefund applyRefund) {	
		this.applyRefundDao.save(applyRefund);
		if (applyRefund != null && applyRefund.getId() != null) {
			return applyRefund.getId();
		}
		return null;
	}
	
	public ApplyRefund getApplyRefund(Long id) {
		ApplyRefund applyRefund = this.applyRefundDao.get(id);
		return applyRefund;
		}
	
	public boolean delApplyRefund(Long id) {	
			ApplyRefund applyRefund = this.getApplyRefund(id);
			if (applyRefund != null) {
				this.applyRefundDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelApplyRefunds(List<Serializable> applyRefundIds) {
		
		for (Serializable id : applyRefundIds) {
			delApplyRefund((Long) id);
		}
		return true;
	}
	
	public IPageList getApplyRefundBy(IQueryObject queryObj) {	
		return this.applyRefundDao.findBy(queryObj);		
	}
	
	public boolean updateApplyRefund(Long id, ApplyRefund applyRefund) {
		if (id != null) {
			applyRefund.setId(id);
		} else {
			return false;
		}
		this.applyRefundDao.update(applyRefund);
		return true;
	}

	@Override
	public ApplyRefund getApplyRefundByName(String name, String value) {
		return this.applyRefundDao.getBy(name, value);
	}	
	
}
