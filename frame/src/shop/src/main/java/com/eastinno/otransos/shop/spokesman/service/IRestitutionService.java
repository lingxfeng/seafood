package com.eastinno.otransos.shop.spokesman.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.spokesman.domain.Restitution;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
/**
 * RestitutionService
 * @author ksmwly@gmail.com
 */
public interface IRestitutionService {
	/**
	 * 保存一个Restitution，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRestitution(Restitution domain);
	
	/**
	 * 根据一个ID得到Restitution
	 * 
	 * @param id
	 * @return
	 */
	Restitution getRestitution(Long id);
	
	/**
	 * 删除一个Restitution
	 * @param id
	 * @return
	 */
	boolean delRestitution(Long id);
	
	/**
	 * 批量删除Restitution
	 * @param ids
	 * @return
	 */
	boolean batchDelRestitutions(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Restitution
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRestitutionBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Restitution
	  * @param id 需要更新的Restitution的id
	  * @param dir 需要更新的Restitution
	  */
	boolean updateRestitution(Long id,Restitution entity);
	/**
	 * 
	 * @param order
	 */
	void calcuteRestitution(ShopOrderInfo order);
}
