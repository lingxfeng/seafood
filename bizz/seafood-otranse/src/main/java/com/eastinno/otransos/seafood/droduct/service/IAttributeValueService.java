package com.eastinno.otransos.seafood.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.domain.AttributeValue;
/**
 * AttributeValueService
 * @author ksmwly@gmail.com
 */
public interface IAttributeValueService {
	/**
	 * 保存一个AttributeValue，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addAttributeValue(AttributeValue domain);
	
	/**
	 * 根据一个ID得到AttributeValue
	 * 
	 * @param id
	 * @return
	 */
	AttributeValue getAttributeValue(Long id);
	
	/**
	 * 删除一个AttributeValue
	 * @param id
	 * @return
	 */
	boolean delAttributeValue(Long id);
	
	/**
	 * 批量删除AttributeValue
	 * @param ids
	 * @return
	 */
	boolean batchDelAttributeValues(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到AttributeValue
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getAttributeValueBy(IQueryObject queryObj);
	
	/**
	  * 更新一个AttributeValue
	  * @param id 需要更新的AttributeValue的id
	  * @param dir 需要更新的AttributeValue
	  */
	boolean updateAttributeValue(Long id,AttributeValue entity);
	/**
	 * 获取拥有多种属性的商品ids
	 * @param attriVal
	 * @return
	 */
	List<Long> getProductIdsByAttrVals(String attriVal);
}
