package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRegular;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRegularService;
import com.eastinno.otransos.shop.promotions.dao.IIntegralBuyRegularDAO;


/**
 * IntegralBuyRegularServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class IntegralBuyRegularServiceImpl implements IIntegralBuyRegularService{
	@Resource
	private IIntegralBuyRegularDAO integralBuyRegularDao;
	
	public void setIntegralBuyRegularDao(IIntegralBuyRegularDAO integralBuyRegularDao){
		this.integralBuyRegularDao=integralBuyRegularDao;
	}
	
	public Long addIntegralBuyRegular(IntegralBuyRegular integralBuyRegular) {	
		this.integralBuyRegularDao.save(integralBuyRegular);
		if (integralBuyRegular != null && integralBuyRegular.getId() != null) {
			return integralBuyRegular.getId();
		}
		return null;
	}
	
	public IntegralBuyRegular getIntegralBuyRegular(Long id) {
		IntegralBuyRegular integralBuyRegular = this.integralBuyRegularDao.get(id);
		return integralBuyRegular;
		}
	
	public boolean delIntegralBuyRegular(Long id) {	
			IntegralBuyRegular integralBuyRegular = this.getIntegralBuyRegular(id);
			if (integralBuyRegular != null) {
				this.integralBuyRegularDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelIntegralBuyRegulars(List<Serializable> integralBuyRegularIds) {
		
		for (Serializable id : integralBuyRegularIds) {
			delIntegralBuyRegular((Long) id);
		}
		return true;
	}
	
	public IPageList getIntegralBuyRegularBy(IQueryObject queryObj) {	
		return this.integralBuyRegularDao.findBy(queryObj);		
	}
	
	public boolean updateIntegralBuyRegular(Long id, IntegralBuyRegular integralBuyRegular) {
		if (id != null) {
			integralBuyRegular.setId(id);
		} else {
			return false;
		}
		this.integralBuyRegularDao.update(integralBuyRegular);
		return true;
	}

	@Override
	public IntegralBuyRegular addIntegralBuyByAdmin(IntegralBuyRegular entity) {
		if(entity == null){
			return entity;
		}
		entity.setCode(new Date().getTime()+"");
		entity.setCreateDate(new Date());
		entity.setWeight(new Date().getTime());
		Long id = this.addIntegralBuyRegular(entity);
		return this.getIntegralBuyRegular(id);
	}

	@Override
	public boolean updateIntegralBuyByAdmin(IntegralBuyRegular entity) {
		return this.updateIntegralBuyRegular(entity.getId(), entity);	
	}
	
	@Override
	public IPageList getAllIntegralRegularForHomeList(){
		QueryObject qo = new QueryObject();
		Date currDate = new Date();
		qo.addQuery("shelvingDate", currDate, "<=");
		qo.addQuery("unshelvingDate", currDate, ">=");
		qo.setOrderBy("weight");
		qo.setOrderType("DESC");
		qo.setPageSize(-1);
		return this.getIntegralBuyRegularBy(qo);
	}
}
