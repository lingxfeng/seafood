package com.eastinno.otransos.shop.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.droduct.domain.BrandType;
import com.eastinno.otransos.shop.droduct.service.IBrandTypeService;
import com.eastinno.otransos.shop.droduct.dao.IBrandTypeDAO;


/**
 * BrandTypeServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class BrandTypeServiceImpl implements IBrandTypeService{
	@Resource
	private IBrandTypeDAO brandTypeDao;
	
	public void setBrandTypeDao(IBrandTypeDAO brandTypeDao){
		this.brandTypeDao=brandTypeDao;
	}
	
	public Long addBrandType(BrandType brandType) {	
		this.brandTypeDao.save(brandType);
		if (brandType != null && brandType.getId() != null) {
			return brandType.getId();
		}
		return null;
	}
	
	public BrandType getBrandType(Long id) {
		BrandType brandType = this.brandTypeDao.get(id);
		return brandType;
		}
	
	public boolean delBrandType(Long id) {	
			BrandType brandType = this.getBrandType(id);
			if (brandType != null) {
				this.brandTypeDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelBrandTypes(List<Serializable> brandTypeIds) {
		
		for (Serializable id : brandTypeIds) {
			delBrandType((Long) id);
		}
		return true;
	}
	
	public IPageList getBrandTypeBy(IQueryObject queryObj) {	
		return this.brandTypeDao.findBy(queryObj);		
	}
	
	public boolean updateBrandType(Long id, BrandType brandType) {
		if (id != null) {
			brandType.setId(id);
		} else {
			return false;
		}
		this.brandTypeDao.update(brandType);
		return true;
	}	
	
}
