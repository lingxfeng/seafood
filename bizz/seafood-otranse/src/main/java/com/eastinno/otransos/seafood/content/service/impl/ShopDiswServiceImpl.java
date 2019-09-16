package com.eastinno.otransos.seafood.content.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.content.domain.ShopDisw;
import com.eastinno.otransos.seafood.content.service.IShopDiswService;
import com.eastinno.otransos.seafood.content.dao.IShopDiswDAO;


/**
 * ShopDiswServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopDiswServiceImpl implements IShopDiswService{
	@Resource
	private IShopDiswDAO shopDiswDao;
	
	public void setShopDiswDao(IShopDiswDAO shopDiswDao){
		this.shopDiswDao=shopDiswDao;
	}
	
	public Long addShopDisw(ShopDisw shopDisw) {	
		this.shopDiswDao.save(shopDisw);
		if (shopDisw != null && shopDisw.getId() != null) {
			return shopDisw.getId();
		}
		return null;
	}
	
	public ShopDisw getShopDisw(Long id) {
		ShopDisw shopDisw = this.shopDiswDao.get(id);
		return shopDisw;
		}
	
	public boolean delShopDisw(Long id) {	
			ShopDisw shopDisw = this.getShopDisw(id);
			if (shopDisw != null) {
				this.shopDiswDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopDisws(List<Serializable> shopDiswIds) {
		
		for (Serializable id : shopDiswIds) {
			delShopDisw((Long) id);
		}
		return true;
	}
	
	public IPageList getShopDiswBy(IQueryObject queryObj) {	
		return this.shopDiswDao.findBy(queryObj);		
	}
	
	public boolean updateShopDisw(Long id, ShopDisw shopDisw) {
		if (id != null) {
			shopDisw.setId(id);
		} else {
			return false;
		}
		this.shopDiswDao.update(shopDisw);
		return true;
	}	
	
}
