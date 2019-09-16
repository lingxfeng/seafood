package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.Couponlist;
/**
 * CouponlistService
 * @author ksmwly@gmail.com
 */
public interface ICouponlistService {
	/**
	 * 保存一个Couponlist，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCouponlist(Couponlist domain);
	
	/**
	 * 根据一个ID得到Couponlist
	 * 
	 * @param id
	 * @return
	 */
	Couponlist getCouponlist(Long id);
	
	/**
	 * 删除一个Couponlist
	 * @param id
	 * @return
	 */
	boolean delCouponlist(Long id);
	
	/**
	 * 批量删除Couponlist
	 * @param ids
	 * @return
	 */
	boolean batchDelCouponlists(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Couponlist
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCouponlistBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Couponlist
	  * @param id 需要更新的Couponlist的id
	  * @param dir 需要更新的Couponlist
	  */
	boolean updateCouponlist(Long id,Couponlist entity);
}
