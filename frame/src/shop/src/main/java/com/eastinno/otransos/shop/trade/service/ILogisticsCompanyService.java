package com.eastinno.otransos.shop.trade.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.trade.domain.LogisticsCompany;
/**
 * LogisticsCompanyService
 * @author ksmwly@gmail.com
 */
public interface ILogisticsCompanyService {
	/**
	 * 保存一个LogisticsCompany，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addLogisticsCompany(LogisticsCompany domain);
	
	/**
	 * 根据一个ID得到LogisticsCompany
	 * 
	 * @param id
	 * @return
	 */
	LogisticsCompany getLogisticsCompany(Long id);
	
	/**
	 * 删除一个LogisticsCompany
	 * @param id
	 * @return
	 */
	boolean delLogisticsCompany(Long id);
	
	/**
	 * 批量删除LogisticsCompany
	 * @param ids
	 * @return
	 */
	boolean batchDelLogisticsCompanys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到LogisticsCompany
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getLogisticsCompanyBy(IQueryObject queryObj);
	
	/**
	  * 更新一个LogisticsCompany
	  * @param id 需要更新的LogisticsCompany的id
	  * @param dir 需要更新的LogisticsCompany
	  */
	boolean updateLogisticsCompany(Long id,LogisticsCompany entity);
}
