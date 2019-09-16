package com.eastinno.otransos.shop.content.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.content.domain.ShopReply;
import com.eastinno.otransos.shop.content.service.IShopReplyService;
import com.eastinno.otransos.shop.content.dao.IShopReplyDAO;


/**
 * ShopReplyServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopReplyServiceImpl implements IShopReplyService{
	@Resource
	private IShopReplyDAO shopReplyDao;
	
	public void setShopReplyDao(IShopReplyDAO shopReplyDao){
		this.shopReplyDao=shopReplyDao;
	}
	
	public Long addShopReply(ShopReply shopReply) {	
		this.shopReplyDao.save(shopReply);
		if (shopReply != null && shopReply.getId() != null) {
			return shopReply.getId();
		}
		return null;
	}
	
	public ShopReply getShopReply(Long id) {
		ShopReply shopReply = this.shopReplyDao.get(id);
		return shopReply;
		}
	
	public boolean delShopReply(Long id) {	
			ShopReply shopReply = this.getShopReply(id);
			if (shopReply != null) {
				this.shopReplyDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopReplys(List<Serializable> shopReplyIds) {
		
		for (Serializable id : shopReplyIds) {
			delShopReply((Long) id);
		}
		return true;
	}
	
	public IPageList getShopReplyBy(IQueryObject queryObj) {	
		return this.shopReplyDao.findBy(queryObj);		
	}
	
	public boolean updateShopReply(Long id, ShopReply shopReply) {
		if (id != null) {
			shopReply.setId(id);
		} else {
			return false;
		}
		this.shopReplyDao.update(shopReply);
		return true;
	}	
	
}
