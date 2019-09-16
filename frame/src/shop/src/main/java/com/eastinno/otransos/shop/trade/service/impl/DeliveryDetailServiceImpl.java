package com.eastinno.otransos.shop.trade.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.trade.domain.DeliveryDetail;
import com.eastinno.otransos.shop.trade.service.IDeliveryDetailService;
import com.eastinno.otransos.shop.trade.dao.IDeliveryDetailDAO;


/**
 * DeliveryDetailServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class DeliveryDetailServiceImpl implements IDeliveryDetailService{
	@Resource
	private IDeliveryDetailDAO deliveryDetailDao;
	
	public void setDeliveryDetailDao(IDeliveryDetailDAO deliveryDetailDao){
		this.deliveryDetailDao=deliveryDetailDao;
	}
	
	public Long addDeliveryDetail(DeliveryDetail deliveryDetail) {	
		this.deliveryDetailDao.save(deliveryDetail);
		if (deliveryDetail != null && deliveryDetail.getId() != null) {
			return deliveryDetail.getId();
		}
		return null;
	}
	
	public DeliveryDetail getDeliveryDetail(Long id) {
		DeliveryDetail deliveryDetail = this.deliveryDetailDao.get(id);
		return deliveryDetail;
		}
	
	public boolean delDeliveryDetail(Long id) {	
			DeliveryDetail deliveryDetail = this.getDeliveryDetail(id);
			if (deliveryDetail != null) {
				this.deliveryDetailDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelDeliveryDetails(List<Serializable> deliveryDetailIds) {
		
		for (Serializable id : deliveryDetailIds) {
			delDeliveryDetail((Long) id);
		}
		return true;
	}
	
	public IPageList getDeliveryDetailBy(IQueryObject queryObj) {	
		return this.deliveryDetailDao.findBy(queryObj);		
	}
	
	public boolean updateDeliveryDetail(Long id, DeliveryDetail deliveryDetail) {
		if (id != null) {
			deliveryDetail.setId(id);
		} else {
			return false;
		}
		this.deliveryDetailDao.update(deliveryDetail);
		return true;
	}	
	
}
