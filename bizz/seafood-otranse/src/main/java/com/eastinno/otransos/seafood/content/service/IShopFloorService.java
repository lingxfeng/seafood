package com.eastinno.otransos.seafood.content.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.content.domain.ShopFloor;
/**
 * ShopFloorService
 * @author ksmwly@gmail.com
 */
public interface IShopFloorService {
	/**
	 * 保存一个ShopFloor，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopFloor(ShopFloor domain);
	
	/**
	 * 根据一个ID得到ShopFloor
	 * 
	 * @param id
	 * @return
	 */
	ShopFloor getShopFloor(Long id);
	
	/**
	 * 删除一个ShopFloor
	 * @param id
	 * @return
	 */
	boolean delShopFloor(Long id);
	
	/**
	 * 批量删除ShopFloor
	 * @param ids
	 * @return
	 */
	boolean batchDelShopFloors(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopFloor
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopFloorBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopFloor
	  * @param id 需要更新的ShopFloor的id
	  * @param dir 需要更新的ShopFloor
	  */
	boolean updateShopFloor(Long id,ShopFloor entity);
}
