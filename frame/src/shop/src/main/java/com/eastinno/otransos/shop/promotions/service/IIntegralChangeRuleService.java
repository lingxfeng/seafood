package com.eastinno.otransos.shop.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.promotions.domain.IntegralChangeRule;
/**
 * IntegralChangeRuleService
 * @author ksmwly@gmail.com
 */
public interface IIntegralChangeRuleService {
	/**
	 * 保存一个IntegralChangeRule，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addIntegralChangeRule(IntegralChangeRule domain);
	
	/**
	 * 根据一个ID得到IntegralChangeRule
	 * 
	 * @param id
	 * @return
	 */
	IntegralChangeRule getIntegralChangeRule(Long id);
	
	/**
	 * 删除一个IntegralChangeRule
	 * @param id
	 * @return
	 */
	boolean delIntegralChangeRule(Long id);
	
	/**
	 * 批量删除IntegralChangeRule
	 * @param ids
	 * @return
	 */
	boolean batchDelIntegralChangeRules(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到IntegralChangeRule
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getIntegralChangeRuleBy(IQueryObject queryObj);
	
	/**
	  * 更新一个IntegralChangeRule
	  * @param id 需要更新的IntegralChangeRule的id
	  * @param dir 需要更新的IntegralChangeRule
	  */
	boolean updateIntegralChangeRule(Long id,IntegralChangeRule entity);
	
	/**
	 * 设置注册赠送积分数量
	 * @param integral
	 * @return
	 */
	public boolean setIntegralRuleByRegister(Long integral);
	
	/**
	 * 获取注册赠送积分数量
	 * @param integral
	 * @return
	 */
	public Long getIntegralRuleByRegister();
	
	/**
	 * 设置一积分等于多少元现金
	 * @param rate
	 * @return
	 */
	public boolean setIntegralCashRate(Long rate);
	
	/**
	 * 获取一积分等于多少元现金
	 * @return
	 */
	public Long getIntegralCashRate();
}
