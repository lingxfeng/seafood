package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRecord;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRecordService;
import com.eastinno.otransos.shop.promotions.dao.IIntegralBuyRecordDAO;


/**
 * IntegralBuyRecordServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class IntegralBuyRecordServiceImpl implements IIntegralBuyRecordService{
	@Resource
	private IIntegralBuyRecordDAO integralBuyRecordDao;
	
	public void setIntegralBuyRecordDao(IIntegralBuyRecordDAO integralBuyRecordDao){
		this.integralBuyRecordDao=integralBuyRecordDao;
	}
	
	public Long addIntegralBuyRecord(IntegralBuyRecord integralBuyRecord) {	
		this.integralBuyRecordDao.save(integralBuyRecord);
		if (integralBuyRecord != null && integralBuyRecord.getId() != null) {
			return integralBuyRecord.getId();
		}
		return null;
	}
	
	public IntegralBuyRecord getIntegralBuyRecord(Long id) {
		IntegralBuyRecord integralBuyRecord = this.integralBuyRecordDao.get(id);
		return integralBuyRecord;
		}
	
	public boolean delIntegralBuyRecord(Long id) {	
			IntegralBuyRecord integralBuyRecord = this.getIntegralBuyRecord(id);
			if (integralBuyRecord != null) {
				this.integralBuyRecordDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelIntegralBuyRecords(List<Serializable> integralBuyRecordIds) {
		
		for (Serializable id : integralBuyRecordIds) {
			delIntegralBuyRecord((Long) id);
		}
		return true;
	}
	
	public IPageList getIntegralBuyRecordBy(IQueryObject queryObj) {	
		return this.integralBuyRecordDao.findBy(queryObj);		
	}
	
	public boolean updateIntegralBuyRecord(Long id, IntegralBuyRecord integralBuyRecord) {
		if (id != null) {
			integralBuyRecord.setId(id);
		} else {
			return false;
		}
		this.integralBuyRecordDao.update(integralBuyRecord);
		return true;
	}	
}
