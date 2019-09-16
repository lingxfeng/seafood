package com.eastinno.otransos.seafood.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.promotions.domain.SingleDispatchCouponRecord;
/**
 * SingleDispatchCouponRecordService
 * @author ksmwly@gmail.com
 */
public interface ISingleDispatchCouponRecordService {
	/**
	 * 保存一个SingleDispatchCouponRecord，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSingleDispatchCouponRecord(SingleDispatchCouponRecord domain);
	
	/**
	 * 根据一个ID得到SingleDispatchCouponRecord
	 * 
	 * @param id
	 * @return
	 */
	SingleDispatchCouponRecord getSingleDispatchCouponRecord(Long id);
	
	/**
	 * 删除一个SingleDispatchCouponRecord
	 * @param id
	 * @return
	 */
	boolean delSingleDispatchCouponRecord(Long id);
	
	/**
	 * 批量删除SingleDispatchCouponRecord
	 * @param ids
	 * @return
	 */
	boolean batchDelSingleDispatchCouponRecords(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SingleDispatchCouponRecord
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSingleDispatchCouponRecordBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SingleDispatchCouponRecord
	  * @param id 需要更新的SingleDispatchCouponRecord的id
	  * @param dir 需要更新的SingleDispatchCouponRecord
	  */
	boolean updateSingleDispatchCouponRecord(Long id,SingleDispatchCouponRecord entity);
}
