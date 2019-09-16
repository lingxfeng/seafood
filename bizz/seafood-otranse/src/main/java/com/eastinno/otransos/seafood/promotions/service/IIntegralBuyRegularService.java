package com.eastinno.otransos.seafood.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.promotions.domain.IntegralBuyRegular;
/**
 * IntegralBuyRegularService
 * @author ksmwly@gmail.com
 */
public interface IIntegralBuyRegularService {
	/**
	 * 保存一个IntegralBuyRegular，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addIntegralBuyRegular(IntegralBuyRegular domain);
	
	/**
	 * 根据一个ID得到IntegralBuyRegular
	 * 
	 * @param id
	 * @return
	 */
	IntegralBuyRegular getIntegralBuyRegular(Long id);
	
	/**
	 * 删除一个IntegralBuyRegular
	 * @param id
	 * @return
	 */
	boolean delIntegralBuyRegular(Long id);
	
	/**
	 * 批量删除IntegralBuyRegular
	 * @param ids
	 * @return
	 */
	boolean batchDelIntegralBuyRegulars(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到IntegralBuyRegular
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getIntegralBuyRegularBy(IQueryObject queryObj);
	
	/**
	  * 更新一个IntegralBuyRegular
	  * @param id 需要更新的IntegralBuyRegular的id
	  * @param dir 需要更新的IntegralBuyRegular
	  */
	boolean updateIntegralBuyRegular(Long id,IntegralBuyRegular entity);
	
	/**
	 * 管理员后台添加一个积分商品
	 * @param entity
	 * @return
	 */
	IntegralBuyRegular addIntegralBuyByAdmin(IntegralBuyRegular entity);
	
	/**
	 * 管理员后台更新一个积分商品活动
	 * @param entiry
	 * @return
	 */
	boolean updateIntegralBuyByAdmin(IntegralBuyRegular entity);
	
	/**
	 * 获取积分商品列表页
	 * @return
	 */
	public IPageList getAllIntegralRegularForHomeList();
}
