package com.eastinno.otransos.shop.trade.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.trade.domain.DeliveryDetail;
/**
 * DeliveryDetailService
 * @author ksmwly@gmail.com
 */
public interface IDeliveryDetailService {
	/**
	 * 保存一个DeliveryDetail，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addDeliveryDetail(DeliveryDetail domain);
	
	/**
	 * 根据一个ID得到DeliveryDetail
	 * 
	 * @param id
	 * @return
	 */
	DeliveryDetail getDeliveryDetail(Long id);
	
	/**
	 * 删除一个DeliveryDetail
	 * @param id
	 * @return
	 */
	boolean delDeliveryDetail(Long id);
	
	/**
	 * 批量删除DeliveryDetail
	 * @param ids
	 * @return
	 */
	boolean batchDelDeliveryDetails(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到DeliveryDetail
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getDeliveryDetailBy(IQueryObject queryObj);
	
	/**
	  * 更新一个DeliveryDetail
	  * @param id 需要更新的DeliveryDetail的id
	  * @param dir 需要更新的DeliveryDetail
	  */
	boolean updateDeliveryDetail(Long id,DeliveryDetail entity);
}
