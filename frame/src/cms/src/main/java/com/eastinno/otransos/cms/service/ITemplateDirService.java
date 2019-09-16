package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.TemplateDir;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * TemplateDirService
 * @author ksmwly@gmail.com
 */
public interface ITemplateDirService {
	/**
	 * 保存一个TemplateDir，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addTemplateDir(TemplateDir domain);
	
	/**
	 * 根据一个ID得到TemplateDir
	 * 
	 * @param id
	 * @return
	 */
	TemplateDir getTemplateDir(Long id);
	
	/**
	 * 删除一个TemplateDir
	 * @param id
	 * @return
	 */
	boolean delTemplateDir(Long id);
	
	/**
	 * 批量删除TemplateDir
	 * @param ids
	 * @return
	 */
	boolean batchDelTemplateDirs(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到TemplateDir
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getTemplateDirBy(IQueryObject queryObj);
	
	/**
	  * 更新一个TemplateDir
	  * @param id 需要更新的TemplateDir的id
	  * @param dir 需要更新的TemplateDir
	  */
	boolean updateTemplateDir(Long id,TemplateDir entity);
}
