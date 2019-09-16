package com.eastinno.otransos.shop.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * CustomCouponService
 * @author ksmwly@gmail.com
 */
public interface ICustomCouponService {
	/**
	 * 保存一个CustomCoupon，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCustomCoupon(CustomCoupon domain);
	
	/**
	 * 根据一个ID得到CustomCoupon
	 * 
	 * @param id
	 * @return
	 */
	CustomCoupon getCustomCoupon(Long id);
	
	/**
	 * 删除一个CustomCoupon
	 * @param id
	 * @return
	 */
	boolean delCustomCoupon(Long id);
	
	/**
	 * 批量删除CustomCoupon
	 * @param ids
	 * @return
	 */
	boolean batchDelCustomCoupons(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到CustomCoupon
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCustomCouponBy(IQueryObject queryObj);
	
	/**
	  * 更新一个CustomCoupon
	  * @param id 需要更新的CustomCoupon的id
	  * @param dir 需要更新的CustomCoupon
	  */
	boolean updateCustomCoupon(Long id,CustomCoupon entity);
	/**
	  * 判断失效优惠券
	  * @param id 用户会员id 
	  */
	void disableCustomCoupon(ShopMember member);
	/**
	  * 判断是否有可用优惠券
	  * @param id 用户会员id 
	  */
	List<CustomCoupon> judgeCustomCoupon(ShopMember member,ShopOrderInfo order);
}
