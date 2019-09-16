package com.eastinno.otransos.seafood.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.domain.ApplyPro;
/**
 * ApplyProService
 * @author ksmwly@gmail.com
 */
public interface IApplyProService {
	/**
	 * 保存一个ApplyPro，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addApplyPro(ApplyPro domain);
	
	/**
	 * 根据一个ID得到ApplyPro
	 * 
	 * @param id
	 * @return
	 */
	ApplyPro getApplyPro(Long id);
	
	/**
	 * 删除一个ApplyPro
	 * @param id
	 * @return
	 */
	boolean delApplyPro(Long id);
	
	/**
	 * 批量删除ApplyPro
	 * @param ids
	 * @return
	 */
	boolean batchDelApplyPros(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ApplyPro
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getApplyProBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ApplyPro
	  * @param id 需要更新的ApplyPro的id
	  * @param dir 需要更新的ApplyPro
	  */
	boolean updateApplyPro(Long id,ApplyPro entity);
}
