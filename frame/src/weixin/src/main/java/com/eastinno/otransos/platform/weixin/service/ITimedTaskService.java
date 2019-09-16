package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.TimedTask;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * TimedTaskService
 * @author ksmwly@gmail.com
 */
public interface ITimedTaskService {
	/**
	 * 保存一个TimedTask，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addTimedTask(TimedTask domain);
	
	/**
	 * 根据一个ID得到TimedTask
	 * 
	 * @param id
	 * @return
	 */
	TimedTask getTimedTask(Long id);
	
	/**
	 * 删除一个TimedTask
	 * @param id
	 * @return
	 */
	boolean delTimedTask(Long id);
	
	/**
	 * 批量删除TimedTask
	 * @param ids
	 * @return
	 */
	boolean batchDelTimedTasks(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到TimedTask
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getTimedTaskBy(IQueryObject queryObj);
	
	/**
	  * 更新一个TimedTask
	  * @param id 需要更新的TimedTask的id
	  * @param dir 需要更新的TimedTask
	  */
	boolean updateTimedTask(Long id,TimedTask entity);
}
