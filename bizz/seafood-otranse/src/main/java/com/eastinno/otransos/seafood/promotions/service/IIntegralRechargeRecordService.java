package com.eastinno.otransos.seafood.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.promotions.domain.IntegralRechargeRecord;
/**
 * IntegralRechargeRecordService
 * @author ksmwly@gmail.com
 */
public interface IIntegralRechargeRecordService {
	/**
	 * 保存一个IntegralRechargeRecord，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addIntegralRechargeRecord(IntegralRechargeRecord domain);
	
	/**
	 * 根据一个ID得到IntegralRechargeRecord
	 * 
	 * @param id
	 * @return
	 */
	IntegralRechargeRecord getIntegralRechargeRecord(Long id);
	
	/**
	 * 删除一个IntegralRechargeRecord
	 * @param id
	 * @return
	 */
	boolean delIntegralRechargeRecord(Long id);
	
	/**
	 * 批量删除IntegralRechargeRecord
	 * @param ids
	 * @return
	 */
	boolean batchDelIntegralRechargeRecords(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到IntegralRechargeRecord
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getIntegralRechargeRecordBy(IQueryObject queryObj);
	
	/**
	  * 更新一个IntegralRechargeRecord
	  * @param id 需要更新的IntegralRechargeRecord的id
	  * @param dir 需要更新的IntegralRechargeRecord
	  */
	boolean updateIntegralRechargeRecord(Long id,IntegralRechargeRecord entity);
}
