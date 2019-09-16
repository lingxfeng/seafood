package com.eastinno.otransos.seafood.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * ShopSinceSomeService
 * @author ksmwly@gmail.com
 */
public interface IShopSinceSomeService {
	/**
	 * 保存一个ShopSinceSome，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopSinceSome(ShopSinceSome domain);
	
	/**
	 * 根据一个ID得到ShopSinceSome
	 * 
	 * @param id
	 * @return
	 */
	ShopSinceSome getShopSinceSome(Long id);
	
	/**
	 * 删除一个ShopSinceSome
	 * @param id
	 * @return
	 */
	boolean delShopSinceSome(Long id);
	
	/**
	 * 批量删除ShopSinceSome
	 * @param ids
	 * @return
	 */
	boolean batchDelShopSinceSomes(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopSinceSome
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopSinceSomeBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopSinceSome
	  * @param id 需要更新的ShopSinceSome的id
	  * @param dir 需要更新的ShopSinceSome
	  */
	boolean updateShopSinceSome(Long id,ShopSinceSome entity);
	
	/**
	  * 设置默认
	  * @param id 需要更新的SinceSome的id
	  * @param dir 需要更新的SinceSome
	  */
	boolean setDefault(Long id,ShopMember member);
	
	/**
	  * 获取自提点
	  * @param id 需要更新的SinceSome的id
	  * @param dir 需要更新的SinceSome
	  */
	List<ShopSinceSome> getShopSinceSomeListByMember(ShopMember member);
}
