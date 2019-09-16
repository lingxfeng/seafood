package com.eastinno.otransos.seafood.distribu.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.distribu.domain.CommissionDetail;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * CommissionDetailService
 * @author ksmwly@gmail.com
 */
public interface ICommissionDetailService {
	/**
	 * 保存一个CommissionDetail，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCommissionDetail(CommissionDetail domain);
	
	/**
	 * 根据一个ID得到CommissionDetail
	 * 
	 * @param id
	 * @return
	 */
	CommissionDetail getCommissionDetail(Long id);
	
	/**
	 * 删除一个CommissionDetail
	 * @param id
	 * @return
	 */
	boolean delCommissionDetail(Long id);
	
	/**
	 * 批量删除CommissionDetail
	 * @param ids
	 * @return
	 */
	boolean batchDelCommissionDetails(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到CommissionDetail
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCommissionDetailBy(IQueryObject queryObj);
	
	/**
	  * 更新一个CommissionDetail
	  * @param id 需要更新的CommissionDetail的id
	  * @param dir 需要更新的CommissionDetail
	  */
	boolean updateCommissionDetail(Long id,CommissionDetail entity);
	
	/**
	  * 返回佣金
	  * @param member 退款用户
	  * @param shopDistributor 店铺
	  */
	boolean returnCommissionDetail(ShopMember member,CommissionDetail commissionDetail);
	
	/**
	  * 得到
	  * @param name 名称
	  * @param shopOrderInfo 订单
	  */
	CommissionDetail getCommissionDetail(String name,ShopOrderInfo shopOrderInfo);
}
