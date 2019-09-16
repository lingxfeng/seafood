package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.web.tools.IPageList;


public interface IAccountService {
	/**
	 * 保存一个Account，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addAccount(Account domain);
	
	/**
	 * 根据一个ID得到Account
	 * 
	 * @param id
	 * @return
	 */
	Account getAccount(Long id);
	
	/**
	 * 删除一个Account
	 * @param id
	 * @return
	 */
	boolean delAccount(Long id);
	
	/**
	 * 批量删除Account
	 * @param ids
	 * @return
	 */
	boolean batchDelAccounts(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Account
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getAccountBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Account
	  * @param id 需要更新的Account的id
	  * @param dir 需要更新的Account
	  */
	boolean updateAccount(Long id,Account entity);
}
