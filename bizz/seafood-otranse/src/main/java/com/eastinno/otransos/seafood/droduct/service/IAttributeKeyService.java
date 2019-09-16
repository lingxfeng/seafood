package com.eastinno.otransos.seafood.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
/**
 * AttributeKeyService
 * @author ksmwly@gmail.com
 */
public interface IAttributeKeyService {
	/**
	 * 保存一个AttributeKey，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addAttributeKey(AttributeKey domain);
	
	/**
	 * 根据一个ID得到AttributeKey
	 * 
	 * @param id
	 * @return
	 */
	AttributeKey getAttributeKey(Long id);
	
	/**
	 * 删除一个AttributeKey
	 * @param id
	 * @return
	 */
	boolean delAttributeKey(Long id);
	
	/**
	 * 批量删除AttributeKey
	 * @param ids
	 * @return
	 */
	boolean batchDelAttributeKeys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到AttributeKey
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getAttributeKeyBy(IQueryObject queryObj);
	
	/**
	  * 更新一个AttributeKey
	  * @param id 需要更新的AttributeKey的id
	  * @param dir 需要更新的AttributeKey
	  */
	boolean updateAttributeKey(Long id,AttributeKey entity);
}
