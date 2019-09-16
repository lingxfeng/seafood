package com.eastinno.otransos.shop.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.dao.IAttributeKeyDAO;
import com.eastinno.otransos.shop.droduct.dao.IAttributeValueDAO;
import com.eastinno.otransos.shop.droduct.domain.AttributeKey;
import com.eastinno.otransos.shop.droduct.domain.AttributeValue;
import com.eastinno.otransos.shop.droduct.service.IAttributeKeyService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * AttributeKeyServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class AttributeKeyServiceImpl implements IAttributeKeyService{
	@Resource
	private IAttributeKeyDAO attributeKeyDao;
	@Resource
	private IAttributeValueDAO attributeValueDao;
	
	public void setAttributeKeyDao(IAttributeKeyDAO attributeKeyDao){
		this.attributeKeyDao=attributeKeyDao;
	}
	
	public IAttributeValueDAO getAttributeValueDao() {
		return attributeValueDao;
	}

	public void setAttributeValueDao(IAttributeValueDAO attributeValueDao) {
		this.attributeValueDao = attributeValueDao;
	}

	public IAttributeKeyDAO getAttributeKeyDao() {
		return attributeKeyDao;
	}

	public Long addAttributeKey(AttributeKey attributeKey) {	
		this.attributeKeyDao.save(attributeKey);
		if (attributeKey != null && attributeKey.getId() != null) {
			return attributeKey.getId();
		}
		return null;
	}
	
	public AttributeKey getAttributeKey(Long id) {
		AttributeKey attributeKey = this.attributeKeyDao.get(id);
		return attributeKey;
		}
	
	public boolean delAttributeKey(Long id) {	
			AttributeKey attributeKey = this.getAttributeKey(id);
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.attributeKey",attributeKey,"=");
			List<AttributeValue> list = this.attributeValueDao.findBy(qo).getResult();
			if(list!=null){
				for(AttributeValue av:list){
					this.attributeValueDao.remove(av.getId());
				}
			}
			if (attributeKey != null) {
				this.attributeKeyDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelAttributeKeys(List<Serializable> attributeKeyIds) {
		
		for (Serializable id : attributeKeyIds) {
			delAttributeKey((Long) id);
		}
		return true;
	}
	
	public IPageList getAttributeKeyBy(IQueryObject queryObj) {	
		return this.attributeKeyDao.findBy(queryObj);		
	}
	
	public boolean updateAttributeKey(Long id, AttributeKey attributeKey) {
		if (id != null) {
			attributeKey.setId(id);
		} else {
			return false;
		}
		this.attributeKeyDao.update(attributeKey);
		return true;
	}	
	
}
