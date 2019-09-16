package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * BrandService
 * @author ksmwly@gmail.com
 */
public interface IBrandService {
	/**
	 * 保存一个Brand，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addBrand(Brand domain);
	
	/**
	 * 根据一个ID得到Brand
	 * 
	 * @param id
	 * @return
	 */
	Brand getBrand(Long id);
	
	/**
	 * 删除一个Brand
	 * @param id
	 * @return
	 */
	boolean delBrand(Long id);
	
	/**
	 * 批量删除Brand
	 * @param ids
	 * @return
	 */
	boolean batchDelBrands(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Brand
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getBrandBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Brand
	  * @param id 需要更新的Brand的id
	  * @param dir 需要更新的Brand
	  */
	boolean updateBrand(Long id,Brand entity);
	/**
	 * 获取所有品牌类型
	 * @return
	 */
	Set<String> getBrandTypeNames();
	Brand getBrandByCode(String code);
}
