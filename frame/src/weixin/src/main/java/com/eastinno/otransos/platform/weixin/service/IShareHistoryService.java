package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.ShareHistory;
import com.eastinno.otransos.web.tools.IPageList;


public interface IShareHistoryService {
	/**
	 * 保存一个ShareHistory，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShareHistory(ShareHistory domain);
	
	/**
	 * 根据一个ID得到ShareHistory
	 * 
	 * @param id
	 * @return
	 */
	ShareHistory getShareHistory(Long id);
	
	/**
	 * 删除一个ShareHistory
	 * @param id
	 * @return
	 */
	boolean delShareHistory(Long id);
	
	/**
	 * 批量删除ShareHistory
	 * @param ids
	 * @return
	 */
	boolean batchDelShareHistorys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShareHistory
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShareHistoryBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShareHistory
	  * @param id 需要更新的ShareHistory的idO
	  * @param dir 需要更新的ShareHistory
	  */
	boolean updateShareHistory(Long id,ShareHistory entity);
}
