package com.eastinno.otransos.seafood.trade.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.trade.domain.Delivery;
/**
 * DeliveryService
 * @author ksmwly@gmail.com
 */
public interface IDeliveryService {
	/**
	 * 保存一个Delivery，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addDelivery(Delivery domain);
	
	/**
	 * 根据一个ID得到Delivery
	 * 
	 * @param id
	 * @return
	 */
	Delivery getDelivery(Long id);
	
	/**
	 * 删除一个Delivery
	 * @param id
	 * @return
	 */
	boolean delDelivery(Long id);
	
	/**
	 * 批量删除Delivery
	 * @param ids
	 * @return
	 */
	boolean batchDelDeliverys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Delivery
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getDeliveryBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Delivery
	  * @param id 需要更新的Delivery的id
	  * @param dir 需要更新的Delivery
	  */
	boolean updateDelivery(Long id,Delivery entity);
}
