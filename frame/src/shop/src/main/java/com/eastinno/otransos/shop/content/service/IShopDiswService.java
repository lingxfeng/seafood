package com.eastinno.otransos.shop.content.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.content.domain.ShopDisw;
/**
 * ShopDiswService
 * @author ksmwly@gmail.com
 */
public interface IShopDiswService {
	/**
	 * 保存一个ShopDisw，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopDisw(ShopDisw domain);
	
	/**
	 * 根据一个ID得到ShopDisw
	 * 
	 * @param id
	 * @return
	 */
	ShopDisw getShopDisw(Long id);
	
	/**
	 * 删除一个ShopDisw
	 * @param id
	 * @return
	 */
	boolean delShopDisw(Long id);
	
	/**
	 * 批量删除ShopDisw
	 * @param ids
	 * @return
	 */
	boolean batchDelShopDisws(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopDisw
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopDiswBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopDisw
	  * @param id 需要更新的ShopDisw的id
	  * @param dir 需要更新的ShopDisw
	  */
	boolean updateShopDisw(Long id,ShopDisw entity);
}
