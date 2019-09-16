package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.shop.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.shop.promotions.dao.IIntegralRechargeRecordDAO;


/**
 * IntegralRechargeRecordServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class IntegralRechargeRecordServiceImpl implements IIntegralRechargeRecordService{
	@Resource
	private IIntegralRechargeRecordDAO integralRechargeRecordDao;
	
	public void setIntegralRechargeRecordDao(IIntegralRechargeRecordDAO integralRechargeRecordDao){
		this.integralRechargeRecordDao=integralRechargeRecordDao;
	}
	
	public Long addIntegralRechargeRecord(IntegralRechargeRecord integralRechargeRecord) {	
		this.integralRechargeRecordDao.save(integralRechargeRecord);
		if (integralRechargeRecord != null && integralRechargeRecord.getId() != null) {
			return integralRechargeRecord.getId();
		}
		return null;
	}
	
	public IntegralRechargeRecord getIntegralRechargeRecord(Long id) {
		IntegralRechargeRecord integralRechargeRecord = this.integralRechargeRecordDao.get(id);
		return integralRechargeRecord;
		}
	
	public boolean delIntegralRechargeRecord(Long id) {	
			IntegralRechargeRecord integralRechargeRecord = this.getIntegralRechargeRecord(id);
			if (integralRechargeRecord != null) {
				this.integralRechargeRecordDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelIntegralRechargeRecords(List<Serializable> integralRechargeRecordIds) {
		
		for (Serializable id : integralRechargeRecordIds) {
			delIntegralRechargeRecord((Long) id);
		}
		return true;
	}
	
	public IPageList getIntegralRechargeRecordBy(IQueryObject queryObj) {	
		return this.integralRechargeRecordDao.findBy(queryObj);		
	}
	
	public boolean updateIntegralRechargeRecord(Long id, IntegralRechargeRecord integralRechargeRecord) {
		if (id != null) {
			integralRechargeRecord.setId(id);
		} else {
			return false;
		}
		this.integralRechargeRecordDao.update(integralRechargeRecord);
		return true;
	}

	public IIntegralRechargeRecordDAO getIntegralRechargeRecordDao() {
		return integralRechargeRecordDao;
	}
	
	
}
