package com.eastinno.otransos.seafood.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.usercenter.domain.MyCoupon;
/**
 * MyCouponService
 * @author ksmwly@gmail.com
 */
public interface IMyCouponService {
	/**
	 * 保存一个MyCoupon，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addMyCoupon(MyCoupon domain);
	
	/**
	 * 根据一个ID得到MyCoupon
	 * 
	 * @param id
	 * @return
	 */
	MyCoupon getMyCoupon(Long id);
	
	/**
	 * 删除一个MyCoupon
	 * @param id
	 * @return
	 */
	boolean delMyCoupon(Long id);
	
	/**
	 * 批量删除MyCoupon
	 * @param ids
	 * @return
	 */
	boolean batchDelMyCoupons(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到MyCoupon
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getMyCouponBy(IQueryObject queryObj);
	
	/**
	  * 更新一个MyCoupon
	  * @param id 需要更新的MyCoupon的id
	  * @param dir 需要更新的MyCoupon
	  */
	boolean updateMyCoupon(Long id,MyCoupon entity);
}
