package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
/**
 * ShopProductService
 * @author ksmwly@gmail.com
 */
public interface IShopProductService {
	/**
	 * 保存一个ShopProduct，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopProduct(ShopProduct domain);
	
	/**
	 * 根据一个ID得到ShopProduct
	 * 
	 * @param id
	 * @return
	 */
	ShopProduct getShopProduct(Long id);
	
	/**
	 * 删除一个ShopProduct
	 * @param id
	 * @return
	 */
	boolean delShopProduct(Long id);
	
	/**
	 * 批量删除ShopProduct
	 * @param ids
	 * @return
	 */
	boolean batchDelShopProducts(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopProduct
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopProductBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopProduct
	  * @param id 需要更新的ShopProduct的id
	  * @param dir 需要更新的ShopProduct
	  */
	boolean updateShopProduct(Long id,ShopProduct entity);
	
	/**
	  * 支付成功后 修改商品库存量 销售量
	  * @param shopProduct
	  * @param specId 规格如果 没有规格<0
	  * @param count 购买数量
	  */
	boolean updateShopProductAfterPay(ShopProduct shopProduct,Long specId,Integer count);
	
}
