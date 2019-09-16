package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.web.tools.IPageList;


public interface INewsItemService {
	/**
	 * 保存一个NewsItem，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addNewsItem(NewsItem domain);
	
	/**
	 * 根据一个ID得到NewsItem
	 * 
	 * @param id
	 * @return
	 */
	NewsItem getNewsItem(Long id);
	
	/**
	 * 删除一个NewsItem
	 * @param id
	 * @return
	 */
	boolean delNewsItem(Long id);
	
	/**
	 * 批量删除NewsItem
	 * @param ids
	 * @return
	 */
	boolean batchDelNewsItems(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到NewsItem
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getNewsItemBy(IQueryObject queryObj);
	
	/**
	  * 更新一个NewsItem
	  * @param id 需要更新的NewsItem的idO
	  * @param dir 需要更新的NewsItem
	  */
	boolean updateNewsItem(Long id,NewsItem entity);
}
