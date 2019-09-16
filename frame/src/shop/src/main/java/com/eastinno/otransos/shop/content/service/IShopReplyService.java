package com.eastinno.otransos.shop.content.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.content.domain.ShopReply;
/**
 * ShopReplyService
 * @author ksmwly@gmail.com
 */
public interface IShopReplyService {
	/**
	 * 保存一个ShopReply，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopReply(ShopReply domain);
	
	/**
	 * 根据一个ID得到ShopReply
	 * 
	 * @param id
	 * @return
	 */
	ShopReply getShopReply(Long id);
	
	/**
	 * 删除一个ShopReply
	 * @param id
	 * @return
	 */
	boolean delShopReply(Long id);
	
	/**
	 * 批量删除ShopReply
	 * @param ids
	 * @return
	 */
	boolean batchDelShopReplys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopReply
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopReplyBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopReply
	  * @param id 需要更新的ShopReply的id
	  * @param dir 需要更新的ShopReply
	  */
	boolean updateShopReply(Long id,ShopReply entity);
}
