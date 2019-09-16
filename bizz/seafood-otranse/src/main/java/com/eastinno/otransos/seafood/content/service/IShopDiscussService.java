package com.eastinno.otransos.seafood.content.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
/**
 * ShopDiscussService
 * @author ksmwly@gmail.com
 */
public interface IShopDiscussService {
	/**
	 * 保存一个ShopDiscuss，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopDiscuss(ShopDiscuss domain);
	
	/**
	 * 根据一个ID得到ShopDiscuss
	 * 
	 * @param id
	 * @return
	 */
	ShopDiscuss getShopDiscuss(Long id);
	
	/**
	 * 删除一个ShopDiscuss
	 * @param id
	 * @return
	 */
	boolean delShopDiscuss(Long id);
	
	/**
	 * 批量删除ShopDiscuss
	 * @param ids
	 * @return
	 */
	boolean batchDelShopDiscusss(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopDiscuss
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopDiscussBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopDiscuss
	  * @param id 需要更新的ShopDiscuss的id
	  * @param dir 需要更新的ShopDiscuss
	  */
	boolean updateShopDiscuss(Long id,ShopDiscuss entity);
}
