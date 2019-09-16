package com.eastinno.otransos.shop.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.droduct.domain.AttributeValue;
import com.eastinno.otransos.shop.droduct.service.IAttributeValueService;
import com.eastinno.otransos.shop.droduct.dao.IAttributeValueDAO;


/**
 * AttributeValueServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class AttributeValueServiceImpl implements IAttributeValueService{
	@Resource
	private IAttributeValueDAO attributeValueDao;
	
	public void setAttributeValueDao(IAttributeValueDAO attributeValueDao){
		this.attributeValueDao=attributeValueDao;
	}
	
	public Long addAttributeValue(AttributeValue attributeValue) {	
		this.attributeValueDao.save(attributeValue);
		if (attributeValue != null && attributeValue.getId() != null) {
			return attributeValue.getId();
		}
		return null;
	}
	
	public AttributeValue getAttributeValue(Long id) {
		AttributeValue attributeValue = this.attributeValueDao.get(id);
		return attributeValue;
		}
	
	public boolean delAttributeValue(Long id) {	
			AttributeValue attributeValue = this.getAttributeValue(id);
			if (attributeValue != null) {
				this.attributeValueDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelAttributeValues(List<Serializable> attributeValueIds) {
		
		for (Serializable id : attributeValueIds) {
			delAttributeValue((Long) id);
		}
		return true;
	}
	
	public IPageList getAttributeValueBy(IQueryObject queryObj) {	
		return this.attributeValueDao.findBy(queryObj);		
	}
	
	public boolean updateAttributeValue(Long id, AttributeValue attributeValue) {
		if (id != null) {
			attributeValue.setId(id);
		} else {
			return false;
		}
		this.attributeValueDao.update(attributeValue);
		return true;
	}

	@Override
	public List<Long> getProductIdsByAttrVals(String attriValIds) {
		if(attriValIds.endsWith(",")){
			attriValIds.substring(0, attriValIds.length()-1);
		}
		return null;
	}	
	
}
