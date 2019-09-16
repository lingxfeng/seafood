package com.eastinno.otransos.seafood.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.promotions.domain.IntegralBuyRecord;
/**
 * IntegralBuyRecordService
 * @author ksmwly@gmail.com
 */
public interface IIntegralBuyRecordService {
	/**
	 * 保存一个IntegralBuyRecord，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addIntegralBuyRecord(IntegralBuyRecord domain);
	
	/**
	 * 根据一个ID得到IntegralBuyRecord
	 * 
	 * @param id
	 * @return
	 */
	IntegralBuyRecord getIntegralBuyRecord(Long id);
	
	/**
	 * 删除一个IntegralBuyRecord
	 * @param id
	 * @return
	 */
	boolean delIntegralBuyRecord(Long id);
	
	/**
	 * 批量删除IntegralBuyRecord
	 * @param ids
	 * @return
	 */
	boolean batchDelIntegralBuyRecords(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到IntegralBuyRecord
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getIntegralBuyRecordBy(IQueryObject queryObj);
	
	/**
	  * 更新一个IntegralBuyRecord
	  * @param id 需要更新的IntegralBuyRecord的id
	  * @param dir 需要更新的IntegralBuyRecord
	  */
	boolean updateIntegralBuyRecord(Long id,IntegralBuyRecord entity);
}
