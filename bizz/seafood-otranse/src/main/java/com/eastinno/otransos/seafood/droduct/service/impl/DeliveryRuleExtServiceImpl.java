package com.eastinno.otransos.seafood.droduct.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.dao.IDeliveryRuleExtDAO;
import com.eastinno.otransos.seafood.droduct.domain.DeliveryRuleExt;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleExtService;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class DeliveryRuleExtServiceImpl implements IDeliveryRuleExtService {
	@Resource
	private IDeliveryRuleExtDAO deliveryRuleExtDAO;

	public IDeliveryRuleExtDAO getDeliveryRuleExtDAO() {
		return deliveryRuleExtDAO;
	}

	public void setDeliveryRuleExtDAO(IDeliveryRuleExtDAO deliveryRuleExtDAO) {
		this.deliveryRuleExtDAO = deliveryRuleExtDAO;
	}

	@Override
	public IPageList getDeliveryRuleExtBy(IQueryObject qo) {
		return this.deliveryRuleExtDAO.findBy(qo);
	}

	@Override
	public Long addDeliveryRuleExt(DeliveryRuleExt deliveryRuleExt) {
		this.deliveryRuleExtDAO.save(deliveryRuleExt);
		if (deliveryRuleExt != null && deliveryRuleExt.getId() != null) {
			return deliveryRuleExt.getId();
		}
		return null;
	}

	@Override
	public DeliveryRuleExt getDeliveryRuleExt(Long id) {
		// TODO Auto-generated method stub
		return this.deliveryRuleExtDAO.get(id);
	}

	@Override
	public boolean updateDeliveryRuleExt(Long id, DeliveryRuleExt entry) {
		if (id != null) {
			entry.setId(id);
		} else {
			return false;
		}
		this.deliveryRuleExtDAO.update(entry);
		return true;
	}
}
