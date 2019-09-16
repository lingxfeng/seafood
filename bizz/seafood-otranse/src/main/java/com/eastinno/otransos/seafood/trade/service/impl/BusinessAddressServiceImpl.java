package com.eastinno.otransos.seafood.trade.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.trade.domain.BusinessAddress;
import com.eastinno.otransos.seafood.trade.service.IBusinessAddressService;
import com.eastinno.otransos.seafood.trade.dao.IBusinessAddressDAO;


/**
 * BusinessAddressServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class BusinessAddressServiceImpl implements IBusinessAddressService{
	@Resource
	private IBusinessAddressDAO businessAddressDao;
	
	public void setBusinessAddressDao(IBusinessAddressDAO businessAddressDao){
		this.businessAddressDao=businessAddressDao;
	}
	
	public Long addBusinessAddress(BusinessAddress businessAddress) {	
		this.businessAddressDao.save(businessAddress);
		if (businessAddress != null && businessAddress.getId() != null) {
			return businessAddress.getId();
		}
		return null;
	}
	
	public BusinessAddress getBusinessAddress(Long id) {
		BusinessAddress businessAddress = this.businessAddressDao.get(id);
		return businessAddress;
		}
	
	public boolean delBusinessAddress(Long id) {	
			BusinessAddress businessAddress = this.getBusinessAddress(id);
			if (businessAddress != null) {
				this.businessAddressDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelBusinessAddresss(List<Serializable> businessAddressIds) {
		
		for (Serializable id : businessAddressIds) {
			delBusinessAddress((Long) id);
		}
		return true;
	}
	
	public IPageList getBusinessAddressBy(IQueryObject queryObj) {	
		return this.businessAddressDao.findBy(queryObj);		
	}
	
	public boolean updateBusinessAddress(Long id, BusinessAddress businessAddress) {
		if (id != null) {
			businessAddress.setId(id);
		} else {
			return false;
		}
		this.businessAddressDao.update(businessAddress);
		return true;
	}	
	
}
