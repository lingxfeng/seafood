package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.web.tools.IPageList;


public interface ITemplateService {
	/**
	 * 保存一个Template，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addTemplate(Template domain);
	
	/**
	 * 根据一个ID得到Template
	 * 
	 * @param id
	 * @return
	 */
	Template getTemplate(Long id);
	
	/**
	 * 删除一个Template
	 * @param id
	 * @return
	 */
	boolean delTemplate(Long id);
	
	/**
	 * 批量删除Template
	 * @param ids
	 * @return
	 */
	boolean batchDelTemplates(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Template
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getTemplateBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Template
	  * @param id 需要更新的Template的idO
	  * @param dir 需要更新的Template
	  */
	boolean updateTemplate(Long id,Template entity);
}
