package com.eastinno.otransos.shop.distribu.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * ShopDistributorService
 * @author ksmwly@gmail.com
 */
public interface IShopDistributorService {
	/**
	 * 保存一个ShopDistributor，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopDistributor(ShopDistributor domain);
	
	/**
	 * 根据一个ID得到ShopDistributor
	 * 
	 * @param id
	 * @return
	 */
	ShopDistributor getShopDistributor(Long id);
	
	/**
	 * 删除一个ShopDistributor
	 * @param id
	 * @return
	 */
	boolean delShopDistributor(Long id);
	
	/**
	 * 批量删除ShopDistributor
	 * @param ids
	 * @return
	 */
	boolean batchDelShopDistributors(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopDistributor
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopDistributorBy(IQueryObject queryObj);
	/**
	 * 审批实体店
	 * @param obj
	 * @return
	 */
	boolean updateChangeEntityShop(ShopDistributor obj);
	/**
	 * 审批微店
	 * @param obj
	 */
	void updateChangeWxShop(ShopDistributor obj);
	
	/**
	 * 获取map，准备转Json
	 * 
	 */
	Map create();
	/**
	 * 更改推荐关系
	 * @param member
	 * @param pmember
	 */
	void changeRalation(ShopMember member,ShopMember pmember);
	/**
	  * 更新一个ShopDistributor
	  * @param id 需要更新的ShopDistributor的id
	  * @param dir 需要更新的ShopDistributor
	  */
	boolean updateShopDistributor(Long id,ShopDistributor entity);
	/**
	 * 申请成为分销商
	 * @param a
	 * @param f
	 * @param member
	 */
	void applyShopDistributor(Account a,ShopDistributor butor,ShopMember member);
	
	/**
	 * 查询分销商
	 * @param a
	 * @param f
	 * @param member
	 */
	ShopDistributor getShopDistributorByMember(ShopMember member);
	
	void mUpdteDistributor(ShopDistributor distributor,ShopDistributor entityshop);
	void piupdate(String jpql);
}
