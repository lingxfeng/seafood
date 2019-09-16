package com.eastinno.otransos.platform.weixin.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.PrizePro;
import com.eastinno.otransos.web.tools.IPageList;


public interface IPrizeProService {
	/**
	 * 保存一个PrizePro，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addPrizePro(PrizePro domain);
	
	/**
	 * 根据一个ID得到PrizePro
	 * 
	 * @param id
	 * @return
	 */
	PrizePro getPrizePro(Long id);
	
	/**
	 * 删除一个PrizePro
	 * @param id
	 * @return
	 */
	boolean delPrizePro(Long id);
	
	/**
	 * 批量删除PrizePro
	 * @param ids
	 * @return
	 */
	boolean batchDelPrizePros(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到PrizePro
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getPrizeProBy(IQueryObject queryObj);
	
	/**
	  * 更新一个PrizePro
	  * @param id 需要更新的PrizePro的idO
	  * @param dir 需要更新的PrizePro
	  */
	boolean updatePrizePro(Long id,PrizePro entity);
}
