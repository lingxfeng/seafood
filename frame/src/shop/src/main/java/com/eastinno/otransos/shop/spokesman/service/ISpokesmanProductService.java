package com.eastinno.otransos.shop.spokesman.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanProduct;
/**
 * SpokesmanProductService
 * @author ksmwly@gmail.com
 */
public interface ISpokesmanProductService {
	/**
	 * 保存一个SpokesmanProduct，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSpokesmanProduct(SpokesmanProduct domain);
	
	/**
	 * 根据一个ID得到SpokesmanProduct
	 * 
	 * @param id
	 * @return
	 */
	SpokesmanProduct getSpokesmanProduct(Long id);
	
	/**
	 * 删除一个SpokesmanProduct
	 * @param id
	 * @return
	 */
	boolean delSpokesmanProduct(Long id);
	
	/**
	 * 批量删除SpokesmanProduct
	 * @param ids
	 * @return
	 */
	boolean batchDelSpokesmanProducts(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SpokesmanProduct
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSpokesmanProductBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SpokesmanProduct
	  * @param id 需要更新的SpokesmanProduct的id
	  * @param dir 需要更新的SpokesmanProduct
	  */
	boolean updateSpokesmanProduct(Long id,SpokesmanProduct entity);
}
