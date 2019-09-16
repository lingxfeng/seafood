package com.eastinno.otransos.seafood.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMemberRating;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberRatingService;
import com.eastinno.otransos.seafood.usercenter.dao.IShopMemberRatingDAO;


/**
 * ShopMemberRatingServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopMemberRatingServiceImpl implements IShopMemberRatingService{
	@Resource
	private IShopMemberRatingDAO shopMemberRatingDao;
	
	public ShopMemberRating getShopMemberRatingByShopMember(ShopMember member){
		ShopMemberRating result = null;
		QueryObject qo = new QueryObject();
		qo.addQuery("integral", member.getTotalIntegral(), "<=");
		qo.setOrderBy("integral");
		qo.setOrderType("desc");
		IPageList list = (IPageList) this.shopMemberRatingDao.findBy(qo);
		if(list.getRowCount() > 0){
			result = (ShopMemberRating)list.getResult().get(0);
		}
		return result;
	}
	
	public void setShopMemberRatingDao(IShopMemberRatingDAO shopMemberRatingDao){
		this.shopMemberRatingDao=shopMemberRatingDao;
	}
	
	public Long addShopMemberRating(ShopMemberRating shopMemberRating) {	
		this.shopMemberRatingDao.save(shopMemberRating);
		if (shopMemberRating != null && shopMemberRating.getId() != null) {
			return shopMemberRating.getId();
		}
		return null;
	}
	
	public ShopMemberRating getShopMemberRating(Long id) {
		ShopMemberRating shopMemberRating = this.shopMemberRatingDao.get(id);
		return shopMemberRating;
		}
	
	public boolean delShopMemberRating(Long id) {	
			ShopMemberRating shopMemberRating = this.getShopMemberRating(id);
			if (shopMemberRating != null) {
				this.shopMemberRatingDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopMemberRatings(List<Serializable> shopMemberRatingIds) {
		
		for (Serializable id : shopMemberRatingIds) {
			delShopMemberRating((Long) id);
		}
		return true;
	}
	
	public IPageList getShopMemberRatingBy(IQueryObject queryObj) {	
		return this.shopMemberRatingDao.findBy(queryObj);		
	}
	
	public boolean updateShopMemberRating(Long id, ShopMemberRating shopMemberRating) {
		if (id != null) {
			shopMemberRating.setId(id);
		} else {
			return false;
		}
		this.shopMemberRatingDao.update(shopMemberRating);
		return true;
	}

	@Override
	public ShopMemberRating queryRatingByShopMember(ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("integral", member.getTotalIntegral(), "<=");
		qo.setOrderBy("integral");
		qo.setOrderType("desc");
		List<ShopMemberRating> ratingList=(List)this.shopMemberRatingDao.findBy(qo).getResult();
		ShopMemberRating rating;
		if(ratingList!=null){
			rating=(ShopMemberRating)ratingList.get(0);
		}else{
			rating = new ShopMemberRating();
			rating.setName("无等级");
			return rating;
		}
		return rating;
	}	
	
}
