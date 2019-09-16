package com.eastinno.otransos.seafood.statistics.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.statistics.domain.VisitorHistory;
/**
 * VisitorHistoryService
 * @author ksmwly@gmail.com
 */
public interface IVisitorHistoryService {
	/**
	 * 保存一个VisitorHistory，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addVisitorHistory(VisitorHistory domain);
	
	/**
	 * 根据一个ID得到VisitorHistory
	 * 
	 * @param id
	 * @return
	 */
	VisitorHistory getVisitorHistory(Long id);
	
	/**
	 * 删除一个VisitorHistory
	 * @param id
	 * @return
	 */
	boolean delVisitorHistory(Long id);
	
	/**
	 * 批量删除VisitorHistory
	 * @param ids
	 * @return
	 */
	boolean batchDelVisitorHistorys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到VisitorHistory
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getVisitorHistoryBy(IQueryObject queryObj);
	
	/**
	  * 更新一个VisitorHistory
	  * @param id 需要更新的VisitorHistory的id
	  * @param dir 需要更新的VisitorHistory
	  */
	boolean updateVisitorHistory(Long id,VisitorHistory entity);
}
