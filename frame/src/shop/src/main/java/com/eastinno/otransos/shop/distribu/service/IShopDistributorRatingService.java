package com.eastinno.otransos.shop.distribu.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributorRating;
/**
 * ShopDistributorRatingService
 * @author ksmwly@gmail.com
 */
public interface IShopDistributorRatingService {
	/**
	 * 保存一个ShopDistributorRating，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopDistributorRating(ShopDistributorRating domain);
	
	/**
	 * 根据一个ID得到ShopDistributorRating
	 * 
	 * @param id
	 * @return
	 */
	ShopDistributorRating getShopDistributorRating(Long id);
	
	/**
	 * 删除一个ShopDistributorRating
	 * @param id
	 * @return
	 */
	boolean delShopDistributorRating(Long id);
	
	/**
	 * 批量删除ShopDistributorRating
	 * @param ids
	 * @return
	 */
	boolean batchDelShopDistributorRatings(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopDistributorRating
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopDistributorRatingBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopDistributorRating
	  * @param id 需要更新的ShopDistributorRating的id
	  * @param dir 需要更新的ShopDistributorRating
	  */
	boolean updateShopDistributorRating(Long id,ShopDistributorRating entity);
}
