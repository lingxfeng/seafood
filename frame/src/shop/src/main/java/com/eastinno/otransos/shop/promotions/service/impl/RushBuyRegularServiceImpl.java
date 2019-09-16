package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRegularService;
import com.eastinno.otransos.shop.promotions.dao.IRushBuyRegularDAO;


/**
 * RushBuyRegularServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class RushBuyRegularServiceImpl implements IRushBuyRegularService{
	@Resource
	private IRushBuyRegularDAO rushBuyRegularDao;
	
	public void setRushBuyRegularDao(IRushBuyRegularDAO rushBuyRegularDao){
		this.rushBuyRegularDao=rushBuyRegularDao;
	}
	
	public Long addRushBuyRegular(RushBuyRegular rushBuyRegular) {
		//更新位置
		this.updateShowPosition(rushBuyRegular, "pcShowPosition");
		this.updateShowPosition(rushBuyRegular, "phoneShowPosition");
		
		this.rushBuyRegularDao.save(rushBuyRegular);
		if (rushBuyRegular != null && rushBuyRegular.getId() != null) {
			return rushBuyRegular.getId();
		}
		return null;
	}
	
	public RushBuyRegular getRushBuyRegular(Long id) {
		RushBuyRegular rushBuyRegular = this.rushBuyRegularDao.get(id);
		return rushBuyRegular;
	}
	
	public boolean delRushBuyRegular(Long id) {	
			RushBuyRegular rushBuyRegular = this.getRushBuyRegular(id);
			if (rushBuyRegular != null) {
				this.rushBuyRegularDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelRushBuyRegulars(List<Serializable> rushBuyRegularIds) {		
		for (Serializable id : rushBuyRegularIds) {
			delRushBuyRegular((Long) id);
		}
		return true;
	}
	
	public IPageList getRushBuyRegularBy(IQueryObject queryObj) {
		((QueryObject)queryObj).setOrderBy("pcShowPosition asc, obj.phoneShowPosition asc,obj.startDate desc");
		((QueryObject)queryObj).setOrderType(null);
		return this.rushBuyRegularDao.findBy(queryObj);		
	}
	
	public boolean updateRushBuyRegular(Long id, RushBuyRegular rushBuyRegular) {
		this.updateShowPosition(rushBuyRegular, "pcShowPosition");
		this.updateShowPosition(rushBuyRegular, "phoneShowPosition");		
		
		if (id != null) {
			rushBuyRegular.setId(id);
		} else {
			return false;
		}
		this.rushBuyRegularDao.update(rushBuyRegular);
		return true;
	}	
	
	@Override
	public IPageList getAllSecKillRegular() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.activityType", 0, "=");
		return this.getRushBuyRegularBy(qo);		
	}

	@Override
	public IPageList getAllSecKillRegularByQO(QueryObject qo) {
		qo.addQuery("obj.activityType", 0, "=");
		return this.getRushBuyRegularBy(qo);
	}

	@Override
	public RushBuyRegular createSecKillRegular(RushBuyRegular regular) {
		if(regular == null){
			return null;
		}
		regular.setActivityType(0);
        regular.setCode(""+new Date().getTime());
        regular.setCreateDate(new Date());
		Long id = this.addRushBuyRegular(regular);
		return this.getRushBuyRegular(id);
	}

	@Override
	public RushBuyRegular updateSecKillRegular(RushBuyRegular regular) {
		if(regular.getActivityType() != 0){
			return null;
		}
		if(!this.updateRushBuyRegular(regular.getId(), regular)){
			return null;
		}
		return this.getRushBuyRegular(regular.getId()); 
	}
	
	@Override
	public IPageList getAllSecKillRegularForHome(){		
		Date currDate = new Date();
		QueryObject qo = new QueryObject();
		qo.addQuery("activityType", 0, "=");
		qo.addQuery("shelvingDate", currDate, "<=");
		qo.addQuery("unshelvingDate", currDate, ">=");
		qo.setOrderBy("pcShowPosition asc, obj.phoneShowPosition asc,obj.startDate desc");
		return this.getRushBuyRegularBy(qo);
	}
	
	@Override
	public IPageList getAllTimeLimitRegular() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.activityType", 1, "=");
		qo.setOrderBy("pcShowPosition asc, obj.phoneShowPosition asc,obj.startDate desc");
		return this.getRushBuyRegularBy(qo);		
	}

	@Override
	public IPageList getAllTimeLimitRegularByQO(QueryObject qo) {
		qo.addQuery("obj.activityType", 1, "=");
		qo.setOrderBy("pcShowPosition asc, obj.phoneShowPosition asc,obj.startDate desc");
		return this.getRushBuyRegularBy(qo);
	}

	@Override
	public RushBuyRegular createTimeLimitRegular(RushBuyRegular regular) {
		if(regular == null){
			return null;
		}
		regular.setActivityType(1);
        regular.setCode(""+new Date().getTime());
        regular.setCreateDate(new Date());
		Long id = this.addRushBuyRegular(regular);
		return this.getRushBuyRegular(id);
	}

	@Override
	public RushBuyRegular updateTimeLimitRegular(RushBuyRegular regular) {
		if(regular.getActivityType() != 1){
			return null;
		}
		if(!this.updateRushBuyRegular(regular.getId(), regular)){
			return null;
		}
		return this.getRushBuyRegular(regular.getId()); 
	}
	
	@Override
	public IPageList getAllTimeLimitRegularForHome(){		
		Date currDate = new Date();
		QueryObject qo = new QueryObject();
		qo.addQuery("activityType", 1, "=");
		qo.addQuery("shelvingDate", currDate, "<=");
		qo.addQuery("unshelvingDate", currDate, ">=");
		qo.setOrderBy("pcShowPosition asc, obj.phoneShowPosition asc,obj.startDate desc");
		return this.getRushBuyRegularBy(qo);
	}

	@Override
	public void updateShowPosition(RushBuyRegular regular, String type) {
		//更新pc显示位置
		String jpql = "";
		if(regular.getPcShowPosition() != null && type.equals("pcShowPosition")){
			jpql = "update disco_shop_rushbuyregular "
					+ " set pcShowPosition=pcShowPosition+1"
					+ " where pcShowPosition>="+regular.getPcShowPosition()
					+ " and id!="+regular.getId();
			this.rushBuyRegularDao.executeNativeSQL(jpql);
		}
		//更新phone显示位置
		if(regular.getPhoneShowPosition() != null && type.equals("phoneShowPosition")){
			jpql = "update disco_shop_rushbuyregular "
					+ " set phoneShowPosition=phoneShowPosition+1"
					+ " where phoneShowPosition>="+regular.getPhoneShowPosition()
					+ " and id!="+regular.getId();
			this.rushBuyRegularDao.executeNativeSQL(jpql);	
		}		
	}
}
