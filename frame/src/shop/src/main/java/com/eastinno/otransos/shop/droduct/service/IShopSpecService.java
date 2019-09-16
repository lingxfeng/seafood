package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
/**
 * ShopSpecService
 * @author ksmwly@gmail.com
 */
public interface IShopSpecService {
	/**
	 * 保存一个ShopSpec，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopSpec(ShopSpec domain);
	
	/**
	 * 删除一个ShopSpec
	 * @param id
	 * @return
	 */
	boolean delShopSpec(Long id);
	
	/**
	 * 批量删除ShopSpec
	 * @param ids
	 * @return
	 */
	boolean batchDelShopSpecs(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopSpec
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopSpecBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopSpec
	  * @param id 需要更新的ShopSpec的id
	  * @param dir 需要更新的ShopSpec
	  */
	boolean updateShopSpec(Long id,ShopSpec entity);
	
	/**
	 * 依据product对象获取对应的规格分组列表
	 * @param product
	 * @return
	 */
	Map<String, List<String>> getSpecGroupByProduct(ShopProduct product);

	/**
	 * 依据product对象，获取该商品所对应的所有商品规格
	 * @param product
	 * @return
	 */
	String getSpecJsonByProduct(ShopProduct product);

	
	/**
	 * 根据一个ID得到ShopSpec
	 * 
	 * @param id
	 * @return
	 */
	ShopSpec getShopSpec(Long id);
}
