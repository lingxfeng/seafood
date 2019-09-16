package com.eastinno.otransos.shop.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShopMemberRating;
/**
 * ShopMemberRatingService
 * @author ksmwly@gmail.com
 */
public interface IShopMemberRatingService {
	/**
	 * 保存一个ShopMemberRating，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopMemberRating(ShopMemberRating domain);
	
	/**
	 * 根据一个ID得到ShopMemberRating
	 * 
	 * @param id
	 * @return
	 */
	ShopMemberRating getShopMemberRating(Long id);
	
	/**
	 * 删除一个ShopMemberRating
	 * @param id
	 * @return
	 */
	boolean delShopMemberRating(Long id);
	
	/**
	 * 批量删除ShopMemberRating
	 * @param ids
	 * @return
	 */
	boolean batchDelShopMemberRatings(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopMemberRating
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopMemberRatingBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopMemberRating
	  * @param id 需要更新的ShopMemberRating的id
	  * @param dir 需要更新的ShopMemberRating
	  */
	boolean updateShopMemberRating(Long id,ShopMemberRating entity);
	
	/**
	 * 通过shopmember对象查找ShopMemberRating对象
	 *  
	 * @param member
	 * @return
	 */
	ShopMemberRating getShopMemberRatingByShopMember(ShopMember member);
	
	/**
	 * 查询用户等级
	 *  
	 * @param member
	 * @return
	 */
	ShopMemberRating queryRatingByShopMember(ShopMember member);
	
}
