package com.eastinno.otransos.shop.content.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.content.domain.ShopAdvert;
/**
 * ShopAdvertService
 * @author ksmwly@gmail.com
 */
public interface IShopAdvertService {
	/**
	 * 保存一个ShopAdvert，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopAdvert(ShopAdvert domain);
	
	/**
	 * 根据一个ID得到ShopAdvert
	 * 
	 * @param id
	 * @return
	 */
	ShopAdvert getShopAdvert(Long id);
	
	/**
	 * 删除一个ShopAdvert
	 * @param id
	 * @return
	 */
	boolean delShopAdvert(Long id);
	
	/**
	 * 批量删除ShopAdvert
	 * @param ids
	 * @return
	 */
	boolean batchDelShopAdverts(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopAdvert
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopAdvertBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopAdvert
	  * @param id 需要更新的ShopAdvert的id
	  * @param dir 需要更新的ShopAdvert
	  */
	boolean updateShopAdvert(Long id,ShopAdvert entity);
}
