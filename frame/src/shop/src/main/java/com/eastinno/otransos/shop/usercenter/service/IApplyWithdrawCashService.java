package com.eastinno.otransos.shop.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
/**
 * ApplyWithdrawCashService
 * @author ksmwly@gmail.com
 */
public interface IApplyWithdrawCashService {
	/**
	 * 保存一个ApplyWithdrawCash，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addApplyWithdrawCash(ApplyWithdrawCash domain);
	
	/**
	 * 根据一个ID得到ApplyWithdrawCash
	 * 
	 * @param id
	 * @return
	 */
	ApplyWithdrawCash getApplyWithdrawCash(Long id);
	
	/**
	 * 删除一个ApplyWithdrawCash
	 * @param id
	 * @return
	 */
	boolean delApplyWithdrawCash(Long id);
	
	/**
	 * 批量删除ApplyWithdrawCash
	 * @param ids
	 * @return
	 */
	boolean batchDelApplyWithdrawCashs(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ApplyWithdrawCash
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getApplyWithdrawCashBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ApplyWithdrawCash
	  * @param id 需要更新的ApplyWithdrawCash的id
	  * @param dir 需要更新的ApplyWithdrawCash
	  */
	boolean updateApplyWithdrawCash(Long id,ApplyWithdrawCash entity);
}
