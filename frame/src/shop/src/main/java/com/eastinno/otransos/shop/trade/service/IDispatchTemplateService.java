package com.eastinno.otransos.shop.trade.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.trade.domain.DispatchTemplate;
/**
 * DispatchTemplateService
 * @author ksmwly@gmail.com
 */
public interface IDispatchTemplateService {
	/**
	 * 保存一个DispatchTemplate，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addDispatchTemplate(DispatchTemplate domain);
	
	/**
	 * 根据一个ID得到DispatchTemplate
	 * 
	 * @param id
	 * @return
	 */
	DispatchTemplate getDispatchTemplate(Long id);
	
	/**
	 * 删除一个DispatchTemplate
	 * @param id
	 * @return
	 */
	boolean delDispatchTemplate(Long id);
	
	/**
	 * 批量删除DispatchTemplate
	 * @param ids
	 * @return
	 */
	boolean batchDelDispatchTemplates(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到DispatchTemplate
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getDispatchTemplateBy(IQueryObject queryObj);
	
	/**
	  * 更新一个DispatchTemplate
	  * @param id 需要更新的DispatchTemplate的id
	  * @param dir 需要更新的DispatchTemplate
	  */
	boolean updateDispatchTemplate(Long id,DispatchTemplate entity);
}
