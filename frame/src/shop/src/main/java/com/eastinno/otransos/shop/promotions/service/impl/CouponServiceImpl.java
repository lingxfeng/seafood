package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.Coupon;
import com.eastinno.otransos.shop.promotions.service.ICouponService;
import com.eastinno.otransos.shop.promotions.dao.ICouponDAO;


/**
 * CouponServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class CouponServiceImpl implements ICouponService{
	@Resource
	private ICouponDAO couponDao;
	
	public void setCouponDao(ICouponDAO couponDao){
		this.couponDao=couponDao;
	}
	
	public Long addCoupon(Coupon coupon) {	
		this.couponDao.save(coupon);
		if (coupon != null && coupon.getId() != null) {
			return coupon.getId();
		}
		return null;
	}
	
	public Coupon getCoupon(Long id) {
		Coupon coupon = this.couponDao.get(id);
		return coupon;
		}
	
	public boolean delCoupon(Long id) {	
			Coupon coupon = this.getCoupon(id);
			if (coupon != null) {
				this.couponDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelCoupons(List<Serializable> couponIds) {
		
		for (Serializable id : couponIds) {
			delCoupon((Long) id);
		}
		return true;
	}
	
	public IPageList getCouponBy(IQueryObject queryObj) {	
		return this.couponDao.findBy(queryObj);		
	}
	
	public boolean updateCoupon(Long id, Coupon coupon) {
		if (id != null) {
			coupon.setId(id);
		} else {
			return false;
		}
		this.couponDao.update(coupon);
		return true;
	}	
	
}
