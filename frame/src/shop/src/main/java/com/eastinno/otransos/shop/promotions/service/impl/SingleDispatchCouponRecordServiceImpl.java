package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.SingleDispatchCouponRecord;
import com.eastinno.otransos.shop.promotions.service.ISingleDispatchCouponRecordService;
import com.eastinno.otransos.shop.promotions.dao.ISingleDispatchCouponRecordDAO;


/**
 * SingleDispatchCouponRecordServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SingleDispatchCouponRecordServiceImpl implements ISingleDispatchCouponRecordService{
	@Resource
	private ISingleDispatchCouponRecordDAO singleDispatchCouponRecordDao;
	
	public void setSingleDispatchCouponRecordDao(ISingleDispatchCouponRecordDAO singleDispatchCouponRecordDao){
		this.singleDispatchCouponRecordDao=singleDispatchCouponRecordDao;
	}
	
	public Long addSingleDispatchCouponRecord(SingleDispatchCouponRecord singleDispatchCouponRecord) {	
		this.singleDispatchCouponRecordDao.save(singleDispatchCouponRecord);
		if (singleDispatchCouponRecord != null && singleDispatchCouponRecord.getId() != null) {
			return singleDispatchCouponRecord.getId();
		}
		return null;
	}
	
	public SingleDispatchCouponRecord getSingleDispatchCouponRecord(Long id) {
		SingleDispatchCouponRecord singleDispatchCouponRecord = this.singleDispatchCouponRecordDao.get(id);
		return singleDispatchCouponRecord;
		}
	
	public boolean delSingleDispatchCouponRecord(Long id) {	
			SingleDispatchCouponRecord singleDispatchCouponRecord = this.getSingleDispatchCouponRecord(id);
			if (singleDispatchCouponRecord != null) {
				this.singleDispatchCouponRecordDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSingleDispatchCouponRecords(List<Serializable> singleDispatchCouponRecordIds) {
		
		for (Serializable id : singleDispatchCouponRecordIds) {
			delSingleDispatchCouponRecord((Long) id);
		}
		return true;
	}
	
	public IPageList getSingleDispatchCouponRecordBy(IQueryObject queryObj) {	
		return this.singleDispatchCouponRecordDao.findBy(queryObj);		
	}
	
	public boolean updateSingleDispatchCouponRecord(Long id, SingleDispatchCouponRecord singleDispatchCouponRecord) {
		if (id != null) {
			singleDispatchCouponRecord.setId(id);
		} else {
			return false;
		}
		this.singleDispatchCouponRecordDao.update(singleDispatchCouponRecord);
		return true;
	}	
	
}
