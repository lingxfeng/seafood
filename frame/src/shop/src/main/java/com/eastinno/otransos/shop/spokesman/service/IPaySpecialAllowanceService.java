package com.eastinno.otransos.shop.spokesman.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.spokesman.domain.PaySpecialAllowance;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
/**
 * PaySpecialAllowanceService
 * @author ksmwly@gmail.com
 */
public interface IPaySpecialAllowanceService {
	/**
	 * 保存一个PaySpecialAllowance，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addPaySpecialAllowance(PaySpecialAllowance domain);
	
	/**
	 * 根据一个ID得到PaySpecialAllowance
	 * 
	 * @param id
	 * @return
	 */
	PaySpecialAllowance getPaySpecialAllowance(Long id);
	
	/**
	 * 删除一个PaySpecialAllowance
	 * @param id
	 * @return
	 */
	boolean delPaySpecialAllowance(Long id);
	
	/**
	 * 批量删除PaySpecialAllowance
	 * @param ids
	 * @return
	 */
	boolean batchDelPaySpecialAllowances(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到PaySpecialAllowance
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getPaySpecialAllowanceBy(IQueryObject queryObj);
	
	/**
	  * 更新一个PaySpecialAllowance
	  * @param id 需要更新的PaySpecialAllowance的id
	  * @param dir 需要更新的PaySpecialAllowance
	  */
	boolean updatePaySpecialAllowance(Long id,PaySpecialAllowance entity);
	/**
	  * 计算总的津贴份数
	  * @param 
	  * @param 
	  */
	int calculateSpecialAllowance();
	/**
	  * 计算及每个代言人的份数
	  * @param 
	  * @param 
	  */
	int calculateSpecialAllowancepart(Spokesman sman);
}
