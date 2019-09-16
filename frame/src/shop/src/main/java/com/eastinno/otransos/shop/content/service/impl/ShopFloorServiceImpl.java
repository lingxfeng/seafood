package com.eastinno.otransos.shop.content.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.content.domain.ShopFloor;
import com.eastinno.otransos.shop.content.service.IShopFloorService;
import com.eastinno.otransos.shop.content.dao.IShopFloorDAO;


/**
 * ShopFloorServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopFloorServiceImpl implements IShopFloorService{
	@Resource
	private IShopFloorDAO shopFloorDao;
	
	public void setShopFloorDao(IShopFloorDAO shopFloorDao){
		this.shopFloorDao=shopFloorDao;
	}
	
	public Long addShopFloor(ShopFloor shopFloor) {	
		this.shopFloorDao.save(shopFloor);
		if (shopFloor != null && shopFloor.getId() != null) {
			return shopFloor.getId();
		}
		return null;
	}
	
	public ShopFloor getShopFloor(Long id) {
		ShopFloor shopFloor = this.shopFloorDao.get(id);
		return shopFloor;
		}
	
	public boolean delShopFloor(Long id) {	
			ShopFloor shopFloor = this.getShopFloor(id);
			if (shopFloor != null) {
				this.shopFloorDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopFloors(List<Serializable> shopFloorIds) {
		
		for (Serializable id : shopFloorIds) {
			delShopFloor((Long) id);
		}
		return true;
	}
	
	public IPageList getShopFloorBy(IQueryObject queryObj) {	
		return this.shopFloorDao.findBy(queryObj);		
	}
	
	public boolean updateShopFloor(Long id, ShopFloor shopFloor) {
		if (id != null) {
			shopFloor.setId(id);
		} else {
			return false;
		}
		this.shopFloorDao.update(shopFloor);
		return true;
	}	
	
}
