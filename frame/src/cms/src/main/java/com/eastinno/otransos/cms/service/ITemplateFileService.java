package com.eastinno.otransos.cms.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.cms.domain.TemplateFile;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * TemplateFileService
 * @author ksmwly@gmail.com
 */
public interface ITemplateFileService {
	/**
	 * 保存一个TemplateFile，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addTemplateFile(TemplateFile domain);
	
	/**
	 * 根据一个ID得到TemplateFile
	 * 
	 * @param id
	 * @return
	 */
	TemplateFile getTemplateFile(Long id);
	
	/**
	 * 删除一个TemplateFile
	 * @param id
	 * @return
	 */
	boolean delTemplateFile(Long id);
	
	/**
	 * 批量删除TemplateFile
	 * @param ids
	 * @return
	 */
	boolean batchDelTemplateFiles(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到TemplateFile
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getTemplateFileBy(IQueryObject queryObj);
	
	/**
	  * 更新一个TemplateFile
	  * @param id 需要更新的TemplateFile的id
	  * @param dir 需要更新的TemplateFile
	  */
	boolean updateTemplateFile(Long id,TemplateFile entity);
}
