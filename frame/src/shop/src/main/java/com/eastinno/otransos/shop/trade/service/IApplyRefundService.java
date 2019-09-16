package com.eastinno.otransos.shop.trade.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.trade.domain.ApplyRefund;
/**
 * ApplyRefundService
 * @author ksmwly@gmail.com
 */
public interface IApplyRefundService {
	/**
	 * 保存一个ApplyRefund，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addApplyRefund(ApplyRefund domain);
	
	/**
	 * 根据一个ID得到ApplyRefund
	 * 
	 * @param id
	 * @return
	 */
	ApplyRefund getApplyRefund(Long id);
	
	/**
	 * 删除一个ApplyRefund
	 * @param id
	 * @return
	 */
	boolean delApplyRefund(Long id);
	
	/**
	 * 批量删除ApplyRefund
	 * @param ids
	 * @return
	 */
	boolean batchDelApplyRefunds(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ApplyRefund
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getApplyRefundBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ApplyRefund
	  * @param id 需要更新的ApplyRefund的id
	  * @param dir 需要更新的ApplyRefund
	  */
	boolean updateApplyRefund(Long id,ApplyRefund entity);
	
	/**
	  * 获取 ApplyRefund
	  * @param name 
	  * @param value
	  */
	ApplyRefund getApplyRefundByName(String name,String value);
}
