package com.eastinno.otransos.shop.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * IntegralHistoryService
 * @author ksmwly@gmail.com
 */
public interface IIntegralHistoryService {
	/**
	 * 保存一个IntegralHistory，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addIntegralHistory(IntegralHistory domain);
	
	/**
	 * 根据一个ID得到IntegralHistory
	 * 
	 * @param id
	 * @return
	 */
	IntegralHistory getIntegralHistory(Long id);
	
	/**
	 * 删除一个IntegralHistory
	 * @param id
	 * @return
	 */
	boolean delIntegralHistory(Long id);
	
	/**
	 * 批量删除IntegralHistory
	 * @param ids
	 * @return
	 */
	boolean batchDelIntegralHistorys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到IntegralHistory
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getIntegralHistoryBy(IQueryObject queryObj);
	
	/**
	  * 更新一个IntegralHistory
	  * @param id 需要更新的IntegralHistory的id
	  * @param dir 需要更新的IntegralHistory
	  */
	boolean updateIntegralHistory(Long id,IntegralHistory entity);
	
	/**
	  * 保存积分记录
	  * @param integral 积分
	  * @param description 描述
	  */
	boolean saveIntegralHistory(Long integral, ShopMember member, String description,Integer type);
	
	/**
	  * 保存积分记录
	  * @param shopOrderInfo 订单
	  * @param description 描述
	  */
	boolean saveIntegralHistory(ShopOrderInfo shopOrderInfo,String description);
	
	/**
	 * 获取会员的积分变更记录
	 * @param member
	 * @return
	 */
	public IPageList getAllIntegralHistoryByMember(ShopMember member);
}

