package com.eastinno.otransos.shop.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.droduct.domain.SpecialProType;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * SpecialProTypeService
 * @author ksmwly@gmail.com
 */
public interface ISpecialProTypeService {
	/**
	 * 保存一个SpecialProType，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSpecialProType(SpecialProType domain);
	
	/**
	 * 根据一个ID得到SpecialProType
	 * 
	 * @param id
	 * @return
	 */
	SpecialProType getSpecialProType(Long id);
	
	/**
	 * 删除一个SpecialProType
	 * @param id
	 * @return
	 */
	boolean delSpecialProType(Long id);
	
	/**
	 * 批量删除SpecialProType
	 * @param ids
	 * @return
	 */
	boolean batchDelSpecialProTypes(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SpecialProType
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSpecialProTypeBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SpecialProType
	  * @param id 需要更新的SpecialProType的id
	  * @param dir 需要更新的SpecialProType
	  */
	boolean updateSpecialProType(Long id,SpecialProType entity);
	
	/**
	  * 更新一个SpecialProType
	  * @param id 需要更新的SpecialProType的id
	  * @param dir 需要更新的SpecialProType
	  */
	SpecialProType getSpecialProTypeByName(String name,String value);
	
	/**
	  * 特殊商品判断是否首次登陆,如果是首次登陆，增加一条记录
	  * @param ptId 商品类型id
	  * @param dir 需要更新的SpecialProType
	  */
	boolean addFirstLogin(Long ptId,String password,ShopMember member);
	
	/**
	  * 判断是否首次登陆
	  * @param ptId 商品类型id
	  * @param dir 需要更新的SpecialProType
	  */
	boolean judegeFirstLogin(Long ptId,ShopMember member);
}
