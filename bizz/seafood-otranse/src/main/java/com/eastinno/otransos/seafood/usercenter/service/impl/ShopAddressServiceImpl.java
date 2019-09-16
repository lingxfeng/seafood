package com.eastinno.otransos.seafood.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.seafood.usercenter.dao.IShopAddressDAO;


/**
 * ShopAddressServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopAddressServiceImpl implements IShopAddressService{
	@Resource
	private IShopAddressDAO shopAddressDao;
	
	public void setShopAddressDao(IShopAddressDAO shopAddressDao){
		this.shopAddressDao=shopAddressDao;
	}
	
	public Long addShopAddress(ShopAddress shopAddress) {	
		this.shopAddressDao.save(shopAddress);
		if (shopAddress != null && shopAddress.getId() != null) {
			return shopAddress.getId();
		}
		return null;
	}
	
	public ShopAddress getShopAddress(Long id) {
		ShopAddress shopAddress = this.shopAddressDao.get(id);
		return shopAddress;
		}
	
	public boolean delShopAddress(Long id) {	
			ShopAddress shopAddress = this.getShopAddress(id);
			if (shopAddress != null) {
				this.shopAddressDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopAddresss(List<Serializable> shopAddressIds) {
		
		for (Serializable id : shopAddressIds) {
			delShopAddress((Long) id);
		}
		return true;
	}
	
	public IPageList getShopAddressBy(IQueryObject queryObj) {	
		return this.shopAddressDao.findBy(queryObj);		
	}
	
	public boolean updateShopAddress(Long id, ShopAddress shopAddress) {
		if (id != null) {
			shopAddress.setId(id);
		} else {
			return false;
		}
		this.shopAddressDao.update(shopAddress);
		return true;
	}

	@Override
	public ShopAddress getParentShopAddressBy(ShopAddress address) {
		
		return null;
	}	
	
}
