package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.SpecialProType;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.ISpecialProTypeService;
import com.eastinno.otransos.seafood.droduct.dao.IProductTypeDAO;
import com.eastinno.otransos.seafood.droduct.dao.ISpecialProTypeDAO;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;


/**
 * SpecialProTypeServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SpecialProTypeServiceImpl implements ISpecialProTypeService{
	@Resource
	private ISpecialProTypeDAO specialProTypeDao;
	@Resource
	private IProductTypeDAO productTypeDAO;
	
	public void setProductTypeDAO(IProductTypeDAO productTypeDAO) {
		this.productTypeDAO = productTypeDAO;
	}

	public void setSpecialProTypeDao(ISpecialProTypeDAO specialProTypeDao){
		this.specialProTypeDao=specialProTypeDao;
	}
	
	public Long addSpecialProType(SpecialProType specialProType) {	
		this.specialProTypeDao.save(specialProType);
		if (specialProType != null && specialProType.getId() != null) {
			return specialProType.getId();
		}
		return null;
	}
	
	public SpecialProType getSpecialProType(Long id) {
		SpecialProType specialProType = this.specialProTypeDao.get(id);
		return specialProType;
		}
	
	public boolean delSpecialProType(Long id) {	
			SpecialProType specialProType = this.getSpecialProType(id);
			if (specialProType != null) {
				this.specialProTypeDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSpecialProTypes(List<Serializable> specialProTypeIds) {
		
		for (Serializable id : specialProTypeIds) {
			delSpecialProType((Long) id);
		}
		return true;
	}
	
	public IPageList getSpecialProTypeBy(IQueryObject queryObj) {	
		return this.specialProTypeDao.findBy(queryObj);		
	}
	
	public boolean updateSpecialProType(Long id, SpecialProType specialProType) {
		if (id != null) {
			specialProType.setId(id);
		} else {
			return false;
		}
		this.specialProTypeDao.update(specialProType);
		return true;
	}

	@Override
	public SpecialProType getSpecialProTypeByName(String name, String value) {
		return this.specialProTypeDao.getBy(name, value);
	}

	@Override
	public boolean addFirstLogin(Long ptId, String password, ShopMember member) {
		ProductType pType=this.productTypeDAO.get(ptId);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member", member, "=");
		qo.addQuery("obj.pType", pType, "=");
		List<?> list=this.specialProTypeDao.findBy(qo).getResult();
		if(list!=null){
			return false;
		}
		if(pType.getIsSpecialProType()){
			if(MD5.encode(password).equals(pType.getPassword())){
				SpecialProType spt = new SpecialProType();
				spt.setMember(member);
				spt.setpType(pType);
				this.specialProTypeDao.save(spt);
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean judegeFirstLogin(Long ptId, ShopMember member) {
		ProductType pType=this.productTypeDAO.get(ptId);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.member", member, "=");
		qo.addQuery("obj.pType", pType, "=");
		List<?> list=this.specialProTypeDao.findBy(qo).getResult();
		if(list==null){
			return true;
		}
		return false;
	}	
	
}
