package com.eastinno.otransos.seafood.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
/**
 * ShopAddressService
 * @author ksmwly@gmail.com
 */
public interface IShopAddressService {
	/**
	 * 保存一个ShopAddress，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopAddress(ShopAddress domain);
	
	/**
	 * 根据一个ID得到ShopAddress
	 * 
	 * @param id
	 * @return
	 */
	ShopAddress getShopAddress(Long id);
	
	/**
	 * 删除一个ShopAddress
	 * @param id
	 * @return
	 */
	boolean delShopAddress(Long id);
	
	/**
	 * 批量删除ShopAddress
	 * @param ids
	 * @return
	 */
	boolean batchDelShopAddresss(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopAddress
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopAddressBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopAddress
	  * @param id 需要更新的ShopAddress的id
	  * @param dir 需要更新的ShopAddress
	  */
	boolean updateShopAddress(Long id,ShopAddress entity);
	
	/**
	 * 通过ShopAddress对象获取上一级地区，并以ShopAddress返回
	 * @param address
	 * @return
	 */
	ShopAddress getParentShopAddressBy(ShopAddress address);
}
