package com.eastinno.otransos.seafood.core.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
/**
 * ShopSystemConfigService
 * @author ksmwly@gmail.com
 */
public interface IShopSystemConfigService {
	/**
	 * 保存一个ShopSystemConfig，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addShopSystemConfig(ShopSystemConfig domain);
	
	/**
	 * 根据一个ID得到ShopSystemConfig
	 * 
	 * @param id
	 * @return
	 */
	ShopSystemConfig getShopSystemConfig(Long id);
	
	/**
	 * 删除一个ShopSystemConfig
	 * @param id
	 * @return
	 */
	boolean delShopSystemConfig(Long id);
	
	/**
	 * 批量删除ShopSystemConfig
	 * @param ids
	 * @return
	 */
	boolean batchDelShopSystemConfigs(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到ShopSystemConfig
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getShopSystemConfigBy(IQueryObject queryObj);
	
	/**
	  * 更新一个ShopSystemConfig
	  * @param id 需要更新的ShopSystemConfig的id
	  * @param dir 需要更新的ShopSystemConfig
	  */
	boolean updateShopSystemConfig(Long id,ShopSystemConfig entity);
	/**
	 * 获取当前配置
	 * @return
	 */
	ShopSystemConfig getSystemConfig();
}
