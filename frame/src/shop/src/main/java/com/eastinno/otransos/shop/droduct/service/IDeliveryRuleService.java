package com.eastinno.otransos.shop.droduct.service;

import java.util.Map;

import com.eastinno.otransos.shop.droduct.domain.DeliveryRule;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;

/**
 * 运费相关的事务类
 * @author wb
 *
 */
public interface IDeliveryRuleService {
	/**
	 * 依据主键id获取规则记录
	 * @param id
	 * @return
	 */
	public DeliveryRule getDeliveryRule(Long id);
	
	/**
	 * 添加一条规则记录
	 * @param deliveryRule
	 * @return
	 */
	public DeliveryRule addDeliveryRule(DeliveryRule deliveryRule);
	
	/**
	 * 删除一条规则记录
	 * @param id
	 * @return
	 */
	public boolean delDeliveryRule(Long id);
	
	/**
	 * 更新id所对应的规则记录
	 * @param id
	 * @param deliveryRule
	 * @return
	 */
	public boolean updateDeliveryRule(Long id, DeliveryRule deliveryRule);
	
	/**
	 * 设置该商品的非偏远地区运费
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public boolean setNormalPostCost(ShopProduct shopProduct, Double cost);
	
	/**
	 * 设置该商品的非偏远地区默认运费
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public boolean setDefaultNormalPostCost(Double cost);
	
	/**
	 * 设置该商品的偏远地区快递费
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public boolean setRemoteRegionPostCost(ShopProduct shopProduct, Double cost);
	
	/**
	 * 设置默认的偏远地区快递费
	 * @param cost
	 * @return
	 */
	public boolean setDefaultRemoteRegionPostCost(Double cost);
	
	/**
	 * 设置单一商品包邮总价起点
	 * @param cost
	 * @return
	 */
	public boolean setNormalFreepostStart(ShopProduct shopProduct, Double cost);
	
	/**
	 * 设置默认的商品包邮订单总价起点
	 * @param cost
	 * @return
	 */
	public boolean setDefaultNormalFreepostStart(Double cost);
		
	/**
	 * 依据商品信息，获取非偏远地区的运费规则
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public DeliveryRule getNormalPostCost(ShopProduct shopProduct);

	/**
	 * 设置订单的包邮总价起点
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public boolean setOrderFreepostStart(Double cost);
	
	/**
	 * 获取默认的非偏远地区的运费规则
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public DeliveryRule getDefaultNormalPostCost();
	
	/**
	 * 依据商品信息，获取偏远地区的运费规则
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public DeliveryRule getRemoteRegionPostCost(ShopProduct shopProduct);
	
	/**
	 * 依据商品信息，获取默认的偏远地区的运费规则
	 * @param cost
	 * @return
	 */
	public DeliveryRule getDefaultRemoteRegionPostCost();
	
	/**
	 * 获取订单总价包邮的起点价格
	 * @param shopProduct
	 * @param cost
	 * @return
	 */
	public DeliveryRule getOrderFreepostStart();
	
	/**
	 * 获取非偏远地区订单总价包邮的起点价格
	 * @param cost
	 * @return
	 */
	public DeliveryRule getNormalFreepostStart(ShopProduct shopProduct);
	
	/**
	 * 获取默认的非偏远地区订单总价包邮的起点价格
	 * @param cost
	 * @return
	 */
	public DeliveryRule getDefaultNormalFreepostStart();
	
	/**
	 * 计算邮费
	 * @return
	 */
	public Double getDeliveryCost(ShopOrderInfo orderInfo);
	
	/**
	 * 依据shopOrderInfo对象，获取品牌-运费，键值对
	 * @param orderInfo
	 * @return
	 */
	public Map<Long, Double> getDeliveryCostMap(ShopOrderInfo orderInfo);
	
	/**
	 * 
	 * @param orderInfo
	 * @return
	 */
	public Double getDeliveryByOrderAndBrandId(Long id,Long brandId);
}
