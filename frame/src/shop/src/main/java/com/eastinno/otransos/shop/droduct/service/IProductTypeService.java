package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.AttributeKey;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * ProductTypeService
 * @author ksmwly@gmail.com
 */
public interface IProductTypeService {
	/**
	 * 保存一个ProductType，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addProductType(ProductType domain);
	
	/**
	 * 根据一个ID得到ProductType
	 * 
	 * @param id
	 * @return
	 */
	ProductType getProductType(Long id);
	
	/**
	 * 删除一个ProductType
	 * @param id
	 * @return
	 */
	boolean delProductType(Long id);
	
	/**
	 * 批量删除ProductType
	 * @param ids
	 * @return
	 */
	boolean batchDelProductTypes(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ProductType
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getProductTypeBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ProductType
	  * @param id 需要更新的ProductType的id
	  * @param dir 需要更新的ProductType
	  */
	boolean updateProductType(Long id,ProductType entity);
	/**
	 * 根据分类名称查询商品分类
	 * @return
	 */
	ProductType getProductByName(String name);
	/**
	 * 获取父类拥有的品牌
	 * @param id
	 * @return
	 */
	List<Brand> getParentBrands(ProductType pType);
	/**
	 * 获取父类拥有的属性
	 * @param id
	 * @return
	 */
	List<AttributeKey> getParentAttrs(ProductType pType,Short type);
	
	/**
	 * 根据code获取ProductType
	 * @param id
	 * @return
	 */
	ProductType getProductTypeByCode(String code);
}
