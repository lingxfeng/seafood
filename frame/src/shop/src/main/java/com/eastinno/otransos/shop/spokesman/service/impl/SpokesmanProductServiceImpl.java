package com.eastinno.otransos.shop.spokesman.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanProduct;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanProductService;
import com.eastinno.otransos.shop.spokesman.dao.ISpokesmanProductDAO;


/**
 * SpokesmanProductServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SpokesmanProductServiceImpl implements ISpokesmanProductService{
	@Resource
	private ISpokesmanProductDAO spokesmanProductDao;
	
	public void setSpokesmanProductDao(ISpokesmanProductDAO spokesmanProductDao){
		this.spokesmanProductDao=spokesmanProductDao;
	}
	
	public Long addSpokesmanProduct(SpokesmanProduct spokesmanProduct) {	
		this.spokesmanProductDao.save(spokesmanProduct);
		if (spokesmanProduct != null && spokesmanProduct.getId() != null) {
			return spokesmanProduct.getId();
		}
		return null;
	}
	
	public SpokesmanProduct getSpokesmanProduct(Long id) {
		SpokesmanProduct spokesmanProduct = this.spokesmanProductDao.get(id);
		return spokesmanProduct;
		}
	
	public boolean delSpokesmanProduct(Long id) {	
			SpokesmanProduct spokesmanProduct = this.getSpokesmanProduct(id);
			if (spokesmanProduct != null) {
				this.spokesmanProductDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSpokesmanProducts(List<Serializable> spokesmanProductIds) {
		
		for (Serializable id : spokesmanProductIds) {
			delSpokesmanProduct((Long) id);
		}
		return true;
	}
	
	public IPageList getSpokesmanProductBy(IQueryObject queryObj) {	
		return this.spokesmanProductDao.findBy(queryObj);		
	}
	
	public boolean updateSpokesmanProduct(Long id, SpokesmanProduct spokesmanProduct) {
		if (id != null) {
			spokesmanProduct.setId(id);
		} else {
			return false;
		}
		this.spokesmanProductDao.update(spokesmanProduct);
		return true;
	}	
	
}
