package com.eastinno.otransos.shop.droduct.service.impl;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.dao.IBrandDAO;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.service.IBrandService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * BrandServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class BrandServiceImpl implements IBrandService{
	@Resource
	private IBrandDAO brandDao;
	
	public void setBrandDao(IBrandDAO brandDao){
		this.brandDao=brandDao;
	}
	
	public Long addBrand(Brand brand) {	
		this.brandDao.save(brand);
		if (brand != null && brand.getId() != null) {
			return brand.getId();
		}
		return null;
	}
	
	public Brand getBrand(Long id) {
		Brand brand = this.brandDao.get(id);
		return brand;
		}
	
	public boolean delBrand(Long id) {	
			Brand brand = this.getBrand(id);
			if (brand != null) {
				this.brandDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelBrands(List<Serializable> brandIds) {
		
		for (Serializable id : brandIds) {
			delBrand((Long) id);
		}
		return true;
	}
	
	public IPageList getBrandBy(IQueryObject queryObj) {	
		return this.brandDao.findBy(queryObj);		
	}
	
	public boolean updateBrand(Long id, Brand brand) {
		if (id != null) {
			brand.setId(id);
		} else {
			return false;
		}
		this.brandDao.update(brand);
		return true;
	}

	@Override
	public Set<String> getBrandTypeNames() {
		QueryObject qo = new QueryObject();
		List<Brand> list = this.brandDao.findBy(qo).getResult();
		Set<String> set = new HashSet<String>();
		if(list!=null){
			for(Brand b:list){
				//set.add(b.getTypeName());
			}
		}
		return set;
	}

	@Override
	public Brand getBrandByCode(String code) {
		return this.brandDao.getBy("code", code);
	}	
	
}
