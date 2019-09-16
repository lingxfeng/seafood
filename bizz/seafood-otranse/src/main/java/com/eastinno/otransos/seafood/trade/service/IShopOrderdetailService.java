package com.eastinno.otransos.seafood.trade.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
/**
 * ShopOrderdetailService
 * @author ksmwly@gmail.com
 */
public interface IShopOrderdetailService {
	/**
	 * 保存一个ShopOrderdetail，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopOrderdetail(ShopOrderdetail domain);
	
	/**
	 * 根据一个ID得到ShopOrderdetail
	 * 
	 * @param id
	 * @return
	 */
	ShopOrderdetail getShopOrderdetail(Long id);
	
	/**
	 * 删除一个ShopOrderdetail
	 * @param id
	 * @return
	 */
	boolean delShopOrderdetail(Long id);
	
	/**
	 * 批量删除ShopOrderdetail
	 * @param ids
	 * @return
	 */
	boolean batchDelShopOrderdetails(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopOrderdetail
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopOrderdetailBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopOrderdetail
	  * @param id 需要更新的ShopOrderdetail的id
	  * @param dir 需要更新的ShopOrderdetail
	  */
	boolean updateShopOrderdetail(Long id,ShopOrderdetail entity);
	
	/**
	  * 获取一个ShopOrderdetail
	  * @param id 需要更新的ShopOrderdetail的id
	  * @param dir 需要更新的ShopOrderdetail
	  */
	ShopOrderdetail getShopOrderdetailByName(String name,String value);
	
	/**
	  * 取消订单
	  * @param orderDetails
	  */
	boolean cancelOrder(List<ShopOrderdetail> orderDetails);
	/**
	 * 按月获取订单详情
	 * @param first
	 * @param end
	 * @return
	 */
	List<ShopOrderdetail> getOrderDetailByMoth(Date first,Date end);
	/**
	 * 获取历史订单详情
	 * @return
	 */
	List<ShopOrderdetail> getOrderDetailAll();
	/**
	 * 按月获取订单详情(商品)
	 * @return
	 */
	List<ShopOrderdetail> getOrderDetailByMothPro(Date first,Date end,Long proId);
	/**
	 * 获取历史总统计(商品)
	 * @param proId
	 * @return
	 */
	List<ShopOrderdetail> getOrderDetailAllPro(Long proId);
	
}
