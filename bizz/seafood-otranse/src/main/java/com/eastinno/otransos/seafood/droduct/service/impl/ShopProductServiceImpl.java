package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.droduct.dao.IShopProductDAO;
import com.eastinno.otransos.seafood.droduct.dao.IShopSpecDAO;


/**
 * ShopProductServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopProductServiceImpl implements IShopProductService{
	@Resource
	private IShopProductDAO shopProductDao;
	
	@Resource
	private IShopSpecDAO shopSpecDAO;
	
	public void setShopProductDao(IShopProductDAO shopProductDao){
		this.shopProductDao=shopProductDao;
	}
	
	public void setShopSpecDAO(IShopSpecDAO shopSpecDAO) {
		this.shopSpecDAO = shopSpecDAO;
	}



	public Long addShopProduct(ShopProduct shopProduct) {	
		this.shopProductDao.save(shopProduct);
		if (shopProduct != null && shopProduct.getId() != null) {
			return shopProduct.getId();
		}
		return null;
	}
	
	public ShopProduct getShopProduct(Long id) {
		ShopProduct shopProduct = this.shopProductDao.get(id);
		return shopProduct;
		}
	
	public boolean delShopProduct(Long id) {	
			ShopProduct shopProduct = this.getShopProduct(id);
			if (shopProduct != null) {
				this.shopProductDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopProducts(List<Serializable> shopProductIds) {
		
		for (Serializable id : shopProductIds) {
			delShopProduct((Long) id);
		}
		return true;
	}
	
	public IPageList getShopProductBy(IQueryObject queryObj) {	
		return this.shopProductDao.findBy(queryObj);		
	}
	
	public boolean updateShopProduct(Long id, ShopProduct shopProduct) {
		if (id != null) {
			shopProduct.setId(id);
		} else {
			return false;
		}
		this.shopProductDao.update(shopProduct);
		return true;
	}

	@Override
	public boolean updateShopProductAfterPay(ShopProduct shopProduct,
			Long specId, Integer count) {
		if(shopProduct==null){
			return false;
		}
		if(specId>0){
			boolean b=false;
			List<?> list=shopProduct.getShopSpecs();
			for (int i = 0; i < list.size(); i++) {
				ShopSpec shopSpec=(ShopSpec)list.get(i);
				if(specId==shopSpec.getId()){
					b=true;
				}
			}
			if(!b){
				return false;
			}
			ShopSpec shopSpec=this.shopSpecDAO.get(specId);
			Integer ggsykc=shopSpec.getInventory()-count;
			if(ggsykc>=0){
				shopSpec.setInventory(shopSpec.getInventory()-count);
				this.shopSpecDAO.update(shopSpec);
				shopProduct.setSaleNum(shopProduct.getSaleNum()+count);
				shopProduct.setInventory(shopProduct.getInventory()-count);
				this.shopProductDao.update(shopProduct);
			}else {
				return false;
			}
		}else{
			Integer sykc=shopProduct.getInventory()-count;
			if(sykc>=0){
				shopProduct.setSaleNum(shopProduct.getSaleNum()+count);
				shopProduct.setInventory(shopProduct.getInventory()-count);
				this.shopProductDao.update(shopProduct);
			}else{
				return false;
			}
		}
		
		return true;
	}	
	
}
