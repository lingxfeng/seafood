package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.Couponlist;
import com.eastinno.otransos.seafood.droduct.service.ICouponlistService;
import com.eastinno.otransos.seafood.droduct.dao.ICouponlistDAO;


/**
 * CouponlistServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CouponlistServiceImpl implements ICouponlistService{
	@Resource
	private ICouponlistDAO couponlistDao;
	
	public void setCouponlistDao(ICouponlistDAO couponlistDao){
		this.couponlistDao=couponlistDao;
	}
	
	public Long addCouponlist(Couponlist couponlist) {	
		this.couponlistDao.save(couponlist);
		if (couponlist != null && couponlist.getId() != null) {
			return couponlist.getId();
		}
		return null;
	}
	
	public Couponlist getCouponlist(Long id) {
		Couponlist couponlist = this.couponlistDao.get(id);
		return couponlist;
		}
	
	public boolean delCouponlist(Long id) {	
			Couponlist couponlist = this.getCouponlist(id);
			if (couponlist != null) {
				this.couponlistDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCouponlists(List<Serializable> couponlistIds) {
		
		for (Serializable id : couponlistIds) {
			delCouponlist((Long) id);
		}
		return true;
	}
	
	public IPageList getCouponlistBy(IQueryObject queryObj) {	
		return this.couponlistDao.findBy(queryObj);		
	}
	
	public boolean updateCouponlist(Long id, Couponlist couponlist) {
		if (id != null) {
			couponlist.setId(id);
		} else {
			return false;
		}
		this.couponlistDao.update(couponlist);
		return true;
	}	
	
}
