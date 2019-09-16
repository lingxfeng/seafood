package com.eastinno.otransos.seafood.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.usercenter.dao.IShopSinceSomeDAO;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.seafood.usercenter.service.IShopSinceSomeService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * ShopSinceSomeServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class ShopSinceSomeServiceImpl implements IShopSinceSomeService{
	@Resource
	private IShopSinceSomeDAO shopSinceSomeDao;
	
	public void setShopSinceSomeDao(IShopSinceSomeDAO shopSinceSomeDao){
		this.shopSinceSomeDao=shopSinceSomeDao;
	}
	
	public Long addShopSinceSome(ShopSinceSome shopSinceSome) {	
		this.shopSinceSomeDao.save(shopSinceSome);
		if (shopSinceSome != null && shopSinceSome.getId() != null) {
			return shopSinceSome.getId();
		}
		return null;
	}
	
	public ShopSinceSome getShopSinceSome(Long id) {
		ShopSinceSome shopSinceSome = this.shopSinceSomeDao.get(id);
		return shopSinceSome;
		}
	
	public boolean delShopSinceSome(Long id) {	
			ShopSinceSome shopSinceSome = this.getShopSinceSome(id);
			if (shopSinceSome != null) {
				this.shopSinceSomeDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelShopSinceSomes(List<Serializable> shopSinceSomeIds) {
		
		for (Serializable id : shopSinceSomeIds) {
			delShopSinceSome((Long) id);
		}
		return true;
	}
	
	public IPageList getShopSinceSomeBy(IQueryObject queryObj) {	
		return this.shopSinceSomeDao.findBy(queryObj);		
	}
	
	public boolean updateShopSinceSome(Long id, ShopSinceSome shopSinceSome) {
		if (id != null) {
			shopSinceSome.setId(id);
		} else {
			return false;
		}
		this.shopSinceSomeDao.update(shopSinceSome);
		return true;
	}

	@Override
	public boolean setDefault(Long id, ShopMember member) {
		ShopSinceSome shopSinceSome = this.shopSinceSomeDao.get(id);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user", member, "=");
		qo.addQuery("obj.isDefault", true, "=");
		List<ShopSinceSome> list = this.shopSinceSomeDao.findBy(qo).getResult();
		if(list!=null){
			for (ShopSinceSome shopSinceSome2 : list) {
				shopSinceSome2.setIsDefault(false);
				this.shopSinceSomeDao.update(shopSinceSome2);
			}
		}
		shopSinceSome.setIsDefault(true);
		this.shopSinceSomeDao.update(shopSinceSome);
		return true;
	}

	@Override
	public List<ShopSinceSome> getShopSinceSomeListByMember(ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.addQuery("obj.status",Short.parseShort("1"),"=");
		qo.setLimit(-1);
		return this.shopSinceSomeDao.findBy(qo).getResult();
	}	
	
}
