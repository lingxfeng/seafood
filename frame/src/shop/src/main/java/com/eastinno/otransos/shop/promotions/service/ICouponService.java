package com.eastinno.otransos.shop.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.promotions.domain.Coupon;
/**
 * CouponService
 * @author ksmwly@gmail.com
 */
public interface ICouponService {
	/**
	 * 保存一个Coupon，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCoupon(Coupon domain);
	
	/**
	 * 根据一个ID得到Coupon
	 * 
	 * @param id
	 * @return
	 */
	Coupon getCoupon(Long id);
	
	/**
	 * 删除一个Coupon
	 * @param id
	 * @return
	 */
	boolean delCoupon(Long id);
	
	/**
	 * 批量删除Coupon
	 * @param ids
	 * @return
	 */
	boolean batchDelCoupons(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Coupon
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCouponBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Coupon
	  * @param id 需要更新的Coupon的id
	  * @param dir 需要更新的Coupon
	  */
	boolean updateCoupon(Long id,Coupon entity);
}
