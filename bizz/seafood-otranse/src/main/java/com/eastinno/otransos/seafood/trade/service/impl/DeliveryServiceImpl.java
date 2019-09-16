package com.eastinno.otransos.seafood.trade.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.trade.domain.Delivery;
import com.eastinno.otransos.seafood.trade.service.IDeliveryService;
import com.eastinno.otransos.seafood.trade.dao.IDeliveryDAO;


/**
 * DeliveryServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class DeliveryServiceImpl implements IDeliveryService{
	@Resource
	private IDeliveryDAO deliveryDao;
	
	public void setDeliveryDao(IDeliveryDAO deliveryDao){
		this.deliveryDao=deliveryDao;
	}
	
	public Long addDelivery(Delivery delivery) {	
		this.deliveryDao.save(delivery);
		if (delivery != null && delivery.getId() != null) {
			return delivery.getId();
		}
		return null;
	}
	
	public Delivery getDelivery(Long id) {
		Delivery delivery = this.deliveryDao.get(id);
		return delivery;
		}
	
	public boolean delDelivery(Long id) {	
			Delivery delivery = this.getDelivery(id);
			if (delivery != null) {
				this.deliveryDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelDeliverys(List<Serializable> deliveryIds) {
		
		for (Serializable id : deliveryIds) {
			delDelivery((Long) id);
		}
		return true;
	}
	
	public IPageList getDeliveryBy(IQueryObject queryObj) {	
		return this.deliveryDao.findBy(queryObj);		
	}
	
	public boolean updateDelivery(Long id, Delivery delivery) {
		if (id != null) {
			delivery.setId(id);
		} else {
			return false;
		}
		this.deliveryDao.update(delivery);
		return true;
	}	
	
}
