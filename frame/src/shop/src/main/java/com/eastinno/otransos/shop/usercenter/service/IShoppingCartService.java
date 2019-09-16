package com.eastinno.otransos.shop.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShoppingCart;
/**
 * ShoppingCartService
 * @author ksmwly@gmail.com
 */
public interface IShoppingCartService {
	/**
	 * 保存一个ShoppingCart，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShoppingCart(ShoppingCart domain);
	
	/**
	 * 根据一个ID得到ShoppingCart
	 * 
	 * @param id
	 * @return
	 */
	ShoppingCart getShoppingCart(Long id);
	
	/**
	 * 删除一个ShoppingCart
	 * @param id
	 * @return
	 */
	boolean delShoppingCart(Long id);
	
	/**
	 * 批量删除ShoppingCart
	 * @param ids
	 * @return
	 */
	boolean batchDelShoppingCarts(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShoppingCart
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShoppingCartBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShoppingCart
	  * @param id 需要更新的ShoppingCart的id
	  * @param dir 需要更新的ShoppingCart
	  */
	boolean updateShoppingCart(Long id,ShoppingCart entity);
	
	/**
	  * 下单成功后清除购物车
	  * @param id 需要更新的ShoppingCart的id
	  * @param dir 需要更新的ShoppingCart
	  */
	boolean clearShoppingCart(ShopOrderInfo shopOrderInfo);
	/**
	 * 登录成功清空cookie中购物车信息，添加到数据库中
	 * @return
	 */
	boolean liulanqitoCar();
	
	/**
	 * 清除购物车
	 * @return
	 */
	boolean clearShoppingCart(String ccId);
}
