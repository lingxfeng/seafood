package com.eastinno.otransos.shop.spokesman.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.spokesman.domain.Restitution;
import com.eastinno.otransos.shop.spokesman.domain.Spokesman;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanProduct;
import com.eastinno.otransos.shop.spokesman.service.IRestitutionService;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanProductService;
import com.eastinno.otransos.shop.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.shop.spokesman.dao.IRestitutionDAO;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;


/**
 * RestitutionServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class RestitutionServiceImpl implements IRestitutionService{
	@Resource
	private IRestitutionDAO restitutionDao;
	@Autowired
	private ISpokesmanProductService spokesmanPoductService;
	@Autowired
	private ISpokesmanService spokesmanService;
	@Autowired
	private IShopOrderInfoService shopOrderInfoService;
	
	public void setRestitutionDao(IRestitutionDAO restitutionDao){
		this.restitutionDao=restitutionDao;
	}
	
	public Long addRestitution(Restitution restitution) {	
		this.restitutionDao.save(restitution);
		if (restitution != null && restitution.getId() != null) {
			return restitution.getId();
		}
		return null;
	}
	
	public Restitution getRestitution(Long id) {
		Restitution restitution = this.restitutionDao.get(id);
		return restitution;
		}
	
	public boolean delRestitution(Long id) {	
			Restitution restitution = this.getRestitution(id);
			if (restitution != null) {
				this.restitutionDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelRestitutions(List<Serializable> restitutionIds) {
		
		for (Serializable id : restitutionIds) {
			delRestitution((Long) id);
		}
		return true;
	}
	
	public IPageList getRestitutionBy(IQueryObject queryObj) {	
		return this.restitutionDao.findBy(queryObj);		
	}
	
	public boolean updateRestitution(Long id, Restitution restitution) {
		if (id != null) {
			restitution.setId(id);
		} else {
			return false;
		}
		this.restitutionDao.update(restitution);
		return true;
	}
	/**
	 * 返现
	 */
	public void calcuteRestitution(ShopOrderInfo order){
		
		Restitution resti = new Restitution();
		ShopMember member = order.getUser();
		int count = order.getRestitutionCount();//当前返现次数
		Spokesman sman = member.getMySpokesman();
		int total = 0;//最多返现次数
		Float restitution = 0F;//月返还金额
		Float currentRestitution = 0F;//本次返还金额
		//更改订单中的相关信息
		List<ShopOrderdetail> list = order.getOrderdetails();
		if(list != null && list.size()!= 0){
			ShopOrderdetail orderdetail = list.get(0);
			ShopProduct sp = orderdetail.getPro();
			int num = orderdetail.getNum();
			 total = order.getTotalMonths();
			 restitution = order.getRestitution();
			 currentRestitution = restitution * num;
			 //新增记录
			 resti.setOrder(order);
			 resti.setRestitution(currentRestitution);
			 resti.setSpokesman(sman);
			 addRestitution(resti);
			 //更新代言人数据
			 sman.setAvailableRestitution(sman.getAvailableRestitution() + currentRestitution);
			 sman.setTotalRestitution(sman.getTotalRestitution() + currentRestitution);
			 this.spokesmanService.updateSpokesman(sman.getId(), sman);
			 
		}
		//更新订单信息
		order.setRestitutionCount(count + 1);
		if((count + 1) > total){
			order.setFinishRestitution(Short.parseShort("1"));
		}else{
			order.setFinishRestitution(Short.parseShort("0"));
		}
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		
	}

	public ISpokesmanProductService getSpokesmanPoductService() {
		return spokesmanPoductService;
	}

	public void setSpokesmanPoductService(
			ISpokesmanProductService spokesmanPoductService) {
		this.spokesmanPoductService = spokesmanPoductService;
	}

	public ISpokesmanService getSpokesmanService() {
		return spokesmanService;
	}

	public void setSpokesmanService(ISpokesmanService spokesmanService) {
		this.spokesmanService = spokesmanService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	
	
}
