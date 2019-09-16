package com.eastinno.otransos.shop.spokesman.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.domain.Subsidy;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
/**
 * SubsidyService
 * @author ksmwly@gmail.com
 */
public interface ISubsidyService {
	/**
	 * 保存一个Subsidy，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSubsidy(Subsidy domain);
	
	/**
	 * 根据一个ID得到Subsidy
	 * 
	 * @param id
	 * @return
	 */
	Subsidy getSubsidy(Long id);
	
	/**
	 * 删除一个Subsidy
	 * @param id
	 * @return
	 */
	boolean delSubsidy(Long id);
	
	/**
	 * 批量删除Subsidy
	 * @param ids
	 * @return
	 */
	boolean batchDelSubsidys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Subsidy
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSubsidyBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Subsidy
	  * @param id 需要更新的Subsidy的id
	  * @param dir 需要更新的Subsidy
	  */
	boolean updateSubsidy(Long id,Subsidy entity);
	/**
	 * 计算补贴
	 * 
	 * @param properties
	 * @return
	 */
	 void calculateSubsidyFirst(ShopOrderInfo order);
	/**
	 * 判定补贴人及补贴金额
	 * @param spokesman
	 * @param pspokesman
	 * @param subsidy
	 * @param order
	 */
	void judgeSubsidy(Spokesman spokesman,Spokesman pspokesman,Subsidy subsidy,ShopOrderInfo order);
	/**
	 * 分配补贴
	 * @param subsidy
	 * @param order
	 */
	void disPatchSubsidy(ShopOrderInfo order);
}
