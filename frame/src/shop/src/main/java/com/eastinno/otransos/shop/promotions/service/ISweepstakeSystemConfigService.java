package com.eastinno.otransos.shop.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeSystemConfig;
/**
 * SweepstakeSystemConfigService
 * @author ksmwly@gmail.com
 */
public interface ISweepstakeSystemConfigService {
	/**
	 * 保存一个SweepstakeSystemConfig，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSweepstakeSystemConfig(SweepstakeSystemConfig domain);
	
	/**
	 * 根据一个ID得到SweepstakeSystemConfig
	 * 
	 * @param id
	 * @return
	 */
	SweepstakeSystemConfig getSweepstakeSystemConfig(Long id);
	
	/**
	 * 删除一个SweepstakeSystemConfig
	 * @param id
	 * @return
	 */
	boolean delSweepstakeSystemConfig(Long id);
	
	/**
	 * 批量删除SweepstakeSystemConfig
	 * @param ids
	 * @return
	 */
	boolean batchDelSweepstakeSystemConfigs(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SweepstakeSystemConfig
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSweepstakeSystemConfigBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SweepstakeSystemConfig
	  * @param id 需要更新的SweepstakeSystemConfig的id
	  * @param dir 需要更新的SweepstakeSystemConfig
	  */
	boolean updateSweepstakeSystemConfig(Long id,SweepstakeSystemConfig entity);
}
