package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.PrizeHistory;
import com.eastinno.otransos.web.tools.IPageList;


public interface IPrizeHistoryService {
	/**
	 * 保存一个PrizeHistory，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addPrizeHistory(PrizeHistory domain);
	
	/**
	 * 根据一个ID得到PrizeHistory
	 * 
	 * @param id
	 * @return
	 */
	PrizeHistory getPrizeHistory(Long id);
	
	/**
	 * 删除一个PrizeHistory
	 * @param id
	 * @return
	 */
	boolean delPrizeHistory(Long id);
	
	/**
	 * 批量删除PrizeHistory
	 * @param ids
	 * @return
	 */
	boolean batchDelPrizeHistorys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到PrizeHistory
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getPrizeHistoryBy(IQueryObject queryObj);
	
	/**
	  * 更新一个PrizeHistory
	  * @param id 需要更新的PrizeHistory的idO
	  * @param dir 需要更新的PrizeHistory
	  */
	boolean updatePrizeHistory(Long id,PrizeHistory entity);
}
