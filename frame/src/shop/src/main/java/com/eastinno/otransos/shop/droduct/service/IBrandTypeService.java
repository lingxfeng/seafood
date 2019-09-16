package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.BrandType;
/**
 * BrandTypeService
 * @author ksmwly@gmail.com
 */
public interface IBrandTypeService {
	/**
	 * 保存一个BrandType，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addBrandType(BrandType domain);
	
	/**
	 * 根据一个ID得到BrandType
	 * 
	 * @param id
	 * @return
	 */
	BrandType getBrandType(Long id);
	
	/**
	 * 删除一个BrandType
	 * @param id
	 * @return
	 */
	boolean delBrandType(Long id);
	
	/**
	 * 批量删除BrandType
	 * @param ids
	 * @return
	 */
	boolean batchDelBrandTypes(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到BrandType
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getBrandTypeBy(IQueryObject queryObj);
	
	/**
	  * 更新一个BrandType
	  * @param id 需要更新的BrandType的id
	  * @param dir 需要更新的BrandType
	  */
	boolean updateBrandType(Long id,BrandType entity);
}
