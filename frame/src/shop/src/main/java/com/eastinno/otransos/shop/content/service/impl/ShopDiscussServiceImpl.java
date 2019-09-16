package com.eastinno.otransos.shop.content.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.content.domain.ShopDiscuss;
import com.eastinno.otransos.shop.content.service.IShopDiscussService;
import com.eastinno.otransos.shop.content.dao.IShopDiscussDAO;


/**
 * ShopDiscussServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopDiscussServiceImpl implements IShopDiscussService{
	@Resource
	private IShopDiscussDAO shopDiscussDao;
	
	public void setShopDiscussDao(IShopDiscussDAO shopDiscussDao){
		this.shopDiscussDao=shopDiscussDao;
	}
	
	public Long addShopDiscuss(ShopDiscuss shopDiscuss) {	
		this.shopDiscussDao.save(shopDiscuss);
		if (shopDiscuss != null && shopDiscuss.getId() != null) {
			return shopDiscuss.getId();
		}
		return null;
	}
	
	public ShopDiscuss getShopDiscuss(Long id) {
		ShopDiscuss shopDiscuss = this.shopDiscussDao.get(id);
		return shopDiscuss;
		}
	
	public boolean delShopDiscuss(Long id) {	
			ShopDiscuss shopDiscuss = this.getShopDiscuss(id);
			if (shopDiscuss != null) {
				this.shopDiscussDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopDiscusss(List<Serializable> shopDiscussIds) {
		
		for (Serializable id : shopDiscussIds) {
			delShopDiscuss((Long) id);
		}
		return true;
	}
	
	public IPageList getShopDiscussBy(IQueryObject queryObj) {	
		return this.shopDiscussDao.findBy(queryObj);		
	}
	
	public boolean updateShopDiscuss(Long id, ShopDiscuss shopDiscuss) {
		if (id != null) {
			shopDiscuss.setId(id);
		} else {
			return false;
		}
		this.shopDiscussDao.update(shopDiscuss);
		return true;
	}	
	
}
