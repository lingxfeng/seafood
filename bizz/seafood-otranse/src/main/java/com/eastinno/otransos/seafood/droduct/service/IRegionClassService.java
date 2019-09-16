package com.eastinno.otransos.seafood.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
/**
 * RegionClassService
 * @author ksmwly@gmail.com
 */
public interface IRegionClassService {
	/**
	 * 保存一个RegionClass，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRegionClass(RegionClass domain);
	
	/**
	 * 根据一个ID得到RegionClass
	 * 
	 * @param id
	 * @return
	 */
	RegionClass getRegionClass(Long id);
	
	/**
	 * 删除一个RegionClass
	 * @param id
	 * @return
	 */
	boolean delRegionClass(Long id);
	
	/**
	 * 批量删除RegionClass
	 * @param ids
	 * @return
	 */
	boolean batchDelRegionClasss(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到RegionClass
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRegionClassBy(IQueryObject queryObj);
	
	/**
	  * 更新一个RegionClass
	  * @param id 需要更新的RegionClass的id
	  * @param dir 需要更新的RegionClass
	  */
	boolean updateRegionClass(Long id,RegionClass entity);
}
