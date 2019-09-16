package com.eastinno.otransos.shop.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.usercenter.dao.IMyCouponDAO;
import com.eastinno.otransos.shop.usercenter.domain.MyCoupon;
import com.eastinno.otransos.shop.usercenter.service.IMyCouponService;


/**
 * MyCouponServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class MyCouponServiceImpl implements IMyCouponService{
	@Resource
	private IMyCouponDAO myCouponDao;
	
	public void setMyCouponDao(IMyCouponDAO myCouponDao){
		this.myCouponDao=myCouponDao;
	}
	
	public Long addMyCoupon(MyCoupon myCoupon) {	
		this.myCouponDao.save(myCoupon);
		if (myCoupon != null && myCoupon.getId() != null) {
			return myCoupon.getId();
		}
		return null;
	}
	
	public MyCoupon getMyCoupon(Long id) {
		MyCoupon myCoupon = this.myCouponDao.get(id);
		return myCoupon;
		}
	
	public boolean delMyCoupon(Long id) {	
			MyCoupon myCoupon = this.getMyCoupon(id);
			if (myCoupon != null) {
				this.myCouponDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelMyCoupons(List<Serializable> myCouponIds) {
		
		for (Serializable id : myCouponIds) {
			delMyCoupon((Long) id);
		}
		return true;
	}
	
	public IPageList getMyCouponBy(IQueryObject queryObj) {	
		return this.myCouponDao.findBy(queryObj);		
	}
	
	public boolean updateMyCoupon(Long id, MyCoupon myCoupon) {
		if (id != null) {
			myCoupon.setId(id);
		} else {
			return false;
		}
		this.myCouponDao.update(myCoupon);
		return true;
	}	
	
}
