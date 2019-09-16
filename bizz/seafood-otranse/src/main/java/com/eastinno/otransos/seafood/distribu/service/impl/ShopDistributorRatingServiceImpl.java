package com.eastinno.otransos.seafood.distribu.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.application.util.QRCodeUtil;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributorRating;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorRatingService;
import com.eastinno.otransos.seafood.distribu.dao.IShopDistributorRatingDAO;


/**
 * ShopDistributorRatingServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopDistributorRatingServiceImpl implements IShopDistributorRatingService{
	@Resource
	private IShopDistributorRatingDAO shopDistributorRatingDao;
	
	public void setShopDistributorRatingDao(IShopDistributorRatingDAO shopDistributorRatingDao){
		this.shopDistributorRatingDao=shopDistributorRatingDao;
	}
	
	public Long addShopDistributorRating(ShopDistributorRating shopDistributorRating) {	
		this.shopDistributorRatingDao.save(shopDistributorRating);
		if (shopDistributorRating != null && shopDistributorRating.getId() != null) {
			return shopDistributorRating.getId();
		}
		return null;
	}
	
	public ShopDistributorRating getShopDistributorRating(Long id) {
		ShopDistributorRating shopDistributorRating = this.shopDistributorRatingDao.get(id);
		return shopDistributorRating;
		}
	
	public boolean delShopDistributorRating(Long id) {	
			ShopDistributorRating shopDistributorRating = this.getShopDistributorRating(id);
			if (shopDistributorRating != null) {
				this.shopDistributorRatingDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopDistributorRatings(List<Serializable> shopDistributorRatingIds) {
		
		for (Serializable id : shopDistributorRatingIds) {
			delShopDistributorRating((Long) id);
		}
		return true;
	}
	
	public IPageList getShopDistributorRatingBy(IQueryObject queryObj) {	
		return this.shopDistributorRatingDao.findBy(queryObj);		
	}
	
	public boolean updateShopDistributorRating(Long id, ShopDistributorRating shopDistributorRating) {
		if (id != null) {
			shopDistributorRating.setId(id);
		} else {
			return false;
		}
		this.shopDistributorRatingDao.update(shopDistributorRating);
		return true;
	}	
	
}
