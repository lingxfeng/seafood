package com.eastinno.otransos.seafood.spokesman.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.spokesman.domain.SpecialAllowance;
/**
 * SpecialAllowanceService
 * @author ksmwly@gmail.com
 */
public interface ISpecialAllowanceService {
	/**
	 * 保存一个SpecialAllowance，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSpecialAllowance(SpecialAllowance domain);
	
	/**
	 * 根据一个ID得到SpecialAllowance
	 * 
	 * @param id
	 * @return
	 */
	SpecialAllowance getSpecialAllowance(Long id);
	
	/**
	 * 删除一个SpecialAllowance
	 * @param id
	 * @return
	 */
	boolean delSpecialAllowance(Long id);
	
	/**
	 * 批量删除SpecialAllowance
	 * @param ids
	 * @return
	 */
	boolean batchDelSpecialAllowances(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SpecialAllowance
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSpecialAllowanceBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SpecialAllowance
	  * @param id 需要更新的SpecialAllowance的id
	  * @param dir 需要更新的SpecialAllowance
	  */
	boolean updateSpecialAllowance(Long id,SpecialAllowance entity);
}
