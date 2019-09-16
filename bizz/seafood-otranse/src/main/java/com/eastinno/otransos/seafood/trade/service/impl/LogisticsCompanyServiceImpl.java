package com.eastinno.otransos.seafood.trade.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.trade.domain.LogisticsCompany;
import com.eastinno.otransos.seafood.trade.service.ILogisticsCompanyService;
import com.eastinno.otransos.seafood.trade.dao.ILogisticsCompanyDAO;


/**
 * LogisticsCompanyServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class LogisticsCompanyServiceImpl implements ILogisticsCompanyService{
	@Resource
	private ILogisticsCompanyDAO logisticsCompanyDao;
	
	public void setLogisticsCompanyDao(ILogisticsCompanyDAO logisticsCompanyDao){
		this.logisticsCompanyDao=logisticsCompanyDao;
	}
	
	public Long addLogisticsCompany(LogisticsCompany logisticsCompany) {	
		this.logisticsCompanyDao.save(logisticsCompany);
		if (logisticsCompany != null && logisticsCompany.getId() != null) {
			return logisticsCompany.getId();
		}
		return null;
	}
	
	public LogisticsCompany getLogisticsCompany(Long id) {
		LogisticsCompany logisticsCompany = this.logisticsCompanyDao.get(id);
		return logisticsCompany;
		}
	
	public boolean delLogisticsCompany(Long id) {	
			LogisticsCompany logisticsCompany = this.getLogisticsCompany(id);
			if (logisticsCompany != null) {
				this.logisticsCompanyDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelLogisticsCompanys(List<Serializable> logisticsCompanyIds) {
		
		for (Serializable id : logisticsCompanyIds) {
			delLogisticsCompany((Long) id);
		}
		return true;
	}
	
	public IPageList getLogisticsCompanyBy(IQueryObject queryObj) {	
		return this.logisticsCompanyDao.findBy(queryObj);		
	}
	
	public boolean updateLogisticsCompany(Long id, LogisticsCompany logisticsCompany) {
		if (id != null) {
			logisticsCompany.setId(id);
		} else {
			return false;
		}
		this.logisticsCompanyDao.update(logisticsCompany);
		return true;
	}	
	
}
