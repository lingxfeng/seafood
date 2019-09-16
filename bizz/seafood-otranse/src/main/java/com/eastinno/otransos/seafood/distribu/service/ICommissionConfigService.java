package com.eastinno.otransos.seafood.distribu.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.distribu.domain.CommissionConfig;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
/**
 * CommissionConfigService
 * @author ksmwly@gmail.com
 */
public interface ICommissionConfigService {
	/**
	 * 保存一个CommissionConfig，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addCommissionConfig(CommissionConfig domain);
	
	/**
	 * 根据一个ID得到CommissionConfig
	 * 
	 * @param id
	 * @return
	 */
	CommissionConfig getCommissionConfig(Long id);
	
	/**
	 * 删除一个CommissionConfig
	 * @param id
	 * @return
	 */
	boolean delCommissionConfig(Long id);
	
	/**
	 * 批量删除CommissionConfig
	 * @param ids
	 * @return
	 */
	boolean batchDelCommissionConfigs(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到CommissionConfig
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getCommissionConfigBy(IQueryObject queryObj);
	
	/**
	  * 更新一个CommissionConfig
	  * @param id 需要更新的CommissionConfig的id
	  * @param dir 需要更新的CommissionConfig
	  */
	boolean updateCommissionConfig(Long id,CommissionConfig entity);
	/**
	 * 通过商品ID获取对应的佣金配置
	 * @param id
	 *
	 */
	Double getCommissionConfigByProductId(ShopProduct product,int type);
	List<ShopProduct> querymethod(String sql);
}
