package com.eastinno.otransos.shop.distribu.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.distribu.domain.CommissionWithdraw;
/**
 * CommissionWithdrawService
 * @author ksmwly@gmail.com
 */
public interface ICommissionWithdrawService {
	/**
	 * 保存一个CommissionWithdraw，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCommissionWithdraw(CommissionWithdraw domain);
	
	/**
	 * 根据一个ID得到CommissionWithdraw
	 * 
	 * @param id
	 * @return
	 */
	CommissionWithdraw getCommissionWithdraw(Long id);
	
	/**
	 * 删除一个CommissionWithdraw
	 * @param id
	 * @return
	 */
	boolean delCommissionWithdraw(Long id);
	
	/**
	 * 批量删除CommissionWithdraw
	 * @param ids
	 * @return
	 */
	boolean batchDelCommissionWithdraws(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到CommissionWithdraw
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCommissionWithdrawBy(IQueryObject queryObj);
	
	/**
	  * 更新一个CommissionWithdraw
	  * @param id 需要更新的CommissionWithdraw的id
	  * @param dir 需要更新的CommissionWithdraw
	  */
	boolean updateCommissionWithdraw(Long id,CommissionWithdraw entity);
}
