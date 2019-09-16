package com.eastinno.otransos.seafood.trade.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.trade.domain.BusinessAddress;
/**
 * BusinessAddressService
 * @author ksmwly@gmail.com
 */
public interface IBusinessAddressService {
	/**
	 * 保存一个BusinessAddress，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addBusinessAddress(BusinessAddress domain);
	
	/**
	 * 根据一个ID得到BusinessAddress
	 * 
	 * @param id
	 * @return
	 */
	BusinessAddress getBusinessAddress(Long id);
	
	/**
	 * 删除一个BusinessAddress
	 * @param id
	 * @return
	 */
	boolean delBusinessAddress(Long id);
	
	/**
	 * 批量删除BusinessAddress
	 * @param ids
	 * @return
	 */
	boolean batchDelBusinessAddresss(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到BusinessAddress
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getBusinessAddressBy(IQueryObject queryObj);
	
	/**
	  * 更新一个BusinessAddress
	  * @param id 需要更新的BusinessAddress的id
	  * @param dir 需要更新的BusinessAddress
	  */
	boolean updateBusinessAddress(Long id,BusinessAddress entity);
}
