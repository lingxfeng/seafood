package com.eastinno.otransos.seafood.statistics.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.statistics.domain.VisitorReffer;
/**
 * VisitorRefferService
 * @author ksmwly@gmail.com
 */
public interface IVisitorRefferService {
	/**
	 * 保存一个VisitorReffer，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addVisitorReffer(VisitorReffer domain);
	
	/**
	 * 根据一个ID得到VisitorReffer
	 * 
	 * @param id
	 * @return
	 */
	VisitorReffer getVisitorReffer(Long id);
	
	/**
	 * 删除一个VisitorReffer
	 * @param id
	 * @return
	 */
	boolean delVisitorReffer(Long id);
	
	/**
	 * 批量删除VisitorReffer
	 * @param ids
	 * @return
	 */
	boolean batchDelVisitorReffers(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到VisitorReffer
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getVisitorRefferBy(IQueryObject queryObj);
	
	/**
	  * 更新一个VisitorReffer
	  * @param id 需要更新的VisitorReffer的id
	  * @param dir 需要更新的VisitorReffer
	  */
	boolean updateVisitorReffer(Long id,VisitorReffer entity);
}
