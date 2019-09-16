
package com.eastinno.otransos.shop.trade.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * ShopOrderInfoService
 * @author ksmwly@gmail.com
 */
public interface IShopOrderInfoService {
	/**
	 * 保存一个ShopOrderInfo，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopOrderInfo(ShopOrderInfo domain);
	
	/**
	 * 根据一个ID得到ShopOrderInfo
	 * 
	 * @param id
	 * @return
	 */
	ShopOrderInfo getShopOrderInfo(Long id);
	
	/**
	 * 删除一个ShopOrderInfo
	 * @param id
	 * @return
	 */
	boolean delShopOrderInfo(Long id);
	
	/**
	 * 批量删除ShopOrderInfo
	 * @param ids
	 * @return
	 */
	boolean batchDelShopOrderInfos(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopOrderInfo
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopOrderInfoBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopOrderInfo
	  * @param id 需要更新的ShopOrderInfo的id
	  * @param dir 需要更新的ShopOrderInfo
	  */
	boolean updateShopOrderInfo(Long id,ShopOrderInfo entity);
	/**
	 * 查询订单
	 * @param status
	 * @return
	 */
	List<ShopOrderInfo> getOrderByStatus(int status,int num);
	/**
	 * 交易成功处理佣金
	 * @param order
	 */
	void disTributorAmt(ShopOrderInfo order);
	/**
	 * 处理订单支付成功修改订单状态及其他相关设置
	 * @param order
	 */
	void disPaySuccess(ShopOrderInfo order);
	/**
	 * 交易成功处理团队业绩
	 * @param order
	 */
	void spokesmanTeam(ShopOrderInfo order);
	
	/**
	 * 交易成功处理团队业绩
	 * @param order
	 */
	Map<Long,List<ShopOrderdetail>> getOrderDetailsByBrand(ShopOrderInfo order);
	
	/**
	 * 查询订单个数
	 * @param form
	 * @param shopMember
	 */
	void queryOrderCount(WebForm form, ShopMember shopMember);
	
	/**
	 * 查询订单
	 * @param form
	 * @param shopMember
	 */
	List<ShopOrderInfo> queryMyOrder(ShopMember shopMember);
	
	/**
	 * 查询订单
	 * @param form
	 * @param shopMember
	 */
	ShopOrderInfo getShopOrderByName(String name,String code);
	
	/**
	  * 商品总价格
	  * @param order 根据订单计算零售价
	  */
	Double getShopOrderAmt(ShopOrderInfo order);
}
