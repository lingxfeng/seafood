package com.eastinno.otransos.shop.droduct.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.dao.IAttributeKeyDAO;
import com.eastinno.otransos.shop.droduct.dao.IProductTypeDAO;
import com.eastinno.otransos.shop.droduct.domain.AttributeKey;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.service.IProductTypeService;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * ProductTypeServiceImpl
 * 
 * @author ksmwly@gmail.com
 */
@Service
public class ProductTypeServiceImpl implements IProductTypeService {
	@Resource
	private IProductTypeDAO productTypeDao;
	@Resource
	private IAttributeKeyDAO attributeKeyDao;

	public void setProductTypeDao(IProductTypeDAO productTypeDao) {
		this.productTypeDao = productTypeDao;
	}

	public void setAttributeKeyDao(IAttributeKeyDAO attributeKeyDao) {
		this.attributeKeyDao = attributeKeyDao;
	}

	public Long addProductType(ProductType productType) {
		if(productType.getParent()==null){
			productType.setDePath("@"+productType.getCode());
		}else{
			productType.setDePath(productType.getParent().getDePath()+"@"+productType.getCode());
		}
		this.productTypeDao.save(productType);
		if (productType != null && productType.getId() != null) {
			return productType.getId();
		}
		return null;
	}

	public ProductType getProductType(Long id) {
		ProductType productType = this.productTypeDao.get(id);
		return productType;
	}

	public boolean delProductType(Long id) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent.id", id, "=");
		List<ProductType> list = this.productTypeDao.findBy(qo).getResult();
		if (list != null && list.size() > 0) {
			for (ProductType p : list) {
				qo = new QueryObject();
				qo.addQuery("obj.parent", p, "=");
				List<ProductType> list2 = this.productTypeDao.findBy(qo)
						.getResult();
				if (list2 != null && list2.size() > 0) {
					for (ProductType p2 : list2) {
						this.productTypeDao.delete(p2);
					}
				}
				this.productTypeDao.delete(p);
			}
		}

		ProductType productType = this.getProductType(id);
		if (productType != null) {
			this.productTypeDao.remove(id);
			return true;
		}
		return false;
	}

	public boolean batchDelProductTypes(List<Serializable> productTypeIds) {

		for (Serializable id : productTypeIds) {
			delProductType((Long) id);
		}
		return true;
	}

	public IPageList getProductTypeBy(IQueryObject queryObj) {
		return this.productTypeDao.findBy(queryObj);
	}

	public boolean updateProductType(Long id, ProductType productType) {
		if (id != null) {
			productType.setId(id);
		} else {
			return false;
		}
		this.productTypeDao.update(productType);
		return true;
	}

	@Override
	public ProductType getProductByName(String name) {
		ProductType obj = this.productTypeDao.getBy("name", name);
		return obj;
	}

	@Override
	public List<Brand> getParentBrands(ProductType pType) {
		List<Brand> brands = new ArrayList<Brand>();
		while (pType != null) {
			if (pType.getIsChilrenBrand()) {
				for (Brand b : pType.getBrands()) {
					brands.add(b);
				}
			}
			pType = pType.getParent();
		}

		return brands;
	}

	@Override
	public List<AttributeKey> getParentAttrs(ProductType pType, Short type) {
		List<AttributeKey> as = new ArrayList<AttributeKey>();
		while (pType != null) {
			if (pType.getIsChilrenAtt()) {
				QueryObject qo = new QueryObject();
				qo.addQuery("obj.type", type, "=");
				qo.addQuery("obj.productType", pType, "=");
				qo.setOrderBy("sequence");
				List<AttributeKey> list = this.attributeKeyDao.findBy(qo)
						.getResult();
				if (list != null) {
					for (AttributeKey a : list) {
						as.add(a);
					}
				}
			}
			pType = pType.getParent();
		}
		return as;
	}
	
	@Override
	public ProductType getProductTypeByCode(String code){
		return this.productTypeDao.getBy("code", code);
	}
}
