package com.eastinno.otransos.seafood.content.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.content.domain.ShopAdvert;
import com.eastinno.otransos.seafood.content.service.IShopAdvertService;
import com.eastinno.otransos.seafood.content.dao.IShopAdvertDAO;


/**
 * ShopAdvertServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopAdvertServiceImpl implements IShopAdvertService{
	@Resource
	private IShopAdvertDAO shopAdvertDao;
	
	public void setShopAdvertDao(IShopAdvertDAO shopAdvertDao){
		this.shopAdvertDao=shopAdvertDao;
	}
	
	public Long addShopAdvert(ShopAdvert shopAdvert) {	
		this.shopAdvertDao.save(shopAdvert);
		if (shopAdvert != null && shopAdvert.getId() != null) {
			return shopAdvert.getId();
		}
		return null;
	}
	
	public ShopAdvert getShopAdvert(Long id) {
		ShopAdvert shopAdvert = this.shopAdvertDao.get(id);
		return shopAdvert;
		}
	
	public boolean delShopAdvert(Long id) {	
			ShopAdvert shopAdvert = this.getShopAdvert(id);
			if (shopAdvert != null) {
				this.shopAdvertDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopAdverts(List<Serializable> shopAdvertIds) {
		
		for (Serializable id : shopAdvertIds) {
			delShopAdvert((Long) id);
		}
		return true;
	}
	
	public IPageList getShopAdvertBy(IQueryObject queryObj) {	
		return this.shopAdvertDao.findBy(queryObj);		
	}
	
	public boolean updateShopAdvert(Long id, ShopAdvert shopAdvert) {
		if (id != null) {
			shopAdvert.setId(id);
		} else {
			return false;
		}
		this.shopAdvertDao.update(shopAdvert);
		return true;
	}	
	
}
