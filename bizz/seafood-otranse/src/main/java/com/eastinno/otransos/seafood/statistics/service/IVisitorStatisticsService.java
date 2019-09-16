package com.eastinno.otransos.seafood.statistics.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.statistics.domain.VisitorStatistics;
/**
 * VisitorStatisticsService
 * @author ksmwly@gmail.com
 */
public interface IVisitorStatisticsService {
	/**
	 * 保存一个VisitorStatistics，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addVisitorStatistics(VisitorStatistics domain);
	
	/**
	 * 根据一个ID得到VisitorStatistics
	 * 
	 * @param id
	 * @return
	 */
	VisitorStatistics getVisitorStatistics(Long id);
	
	/**
	 * 删除一个VisitorStatistics
	 * @param id
	 * @return
	 */
	boolean delVisitorStatistics(Long id);
	
	/**
	 * 批量删除VisitorStatistics
	 * @param ids
	 * @return
	 */
	boolean batchDelVisitorStatisticss(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到VisitorStatistics
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getVisitorStatisticsBy(IQueryObject queryObj);
	
	/**
	  * 更新一个VisitorStatistics
	  * @param id 需要更新的VisitorStatistics的id
	  * @param dir 需要更新的VisitorStatistics
	  */
	boolean updateVisitorStatistics(Long id,VisitorStatistics entity);
	List<String> getCount();
	List<String> getCountDay();
	List<String> getCountMonth();
	List<String> getCountYear();
	List<String> getAllDayData();

	List<String> getAllMonthData();
	
	List<String> getAllYearData();

}
