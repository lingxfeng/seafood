package com.eastinno.otransos.shop.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.shop.usercenter.dao.IIntegralHistoryDAO;


/**
 * IntegralHistoryServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class IntegralHistoryServiceImpl implements IIntegralHistoryService{
	@Resource
	private IIntegralHistoryDAO integralHistoryDao;
	
	public void setIntegralHistoryDao(IIntegralHistoryDAO integralHistoryDao){
		this.integralHistoryDao=integralHistoryDao;
	}
	
	public Long addIntegralHistory(IntegralHistory integralHistory) {	
		this.integralHistoryDao.save(integralHistory);
		if (integralHistory != null && integralHistory.getId() != null) {
			return integralHistory.getId();
		}
		return null;
	}
	
	public IntegralHistory getIntegralHistory(Long id) {
		IntegralHistory integralHistory = this.integralHistoryDao.get(id);
		return integralHistory;
		}
	
	public boolean delIntegralHistory(Long id) {	
			IntegralHistory integralHistory = this.getIntegralHistory(id);
			if (integralHistory != null) {
				this.integralHistoryDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelIntegralHistorys(List<Serializable> integralHistoryIds) {
		
		for (Serializable id : integralHistoryIds) {
			delIntegralHistory((Long) id);
		}
		return true;
	}
	
	public IPageList getIntegralHistoryBy(IQueryObject queryObj) {	
		return this.integralHistoryDao.findBy(queryObj);		
	}
	
	public boolean updateIntegralHistory(Long id, IntegralHistory integralHistory) {
		if (id != null) {
			integralHistory.setId(id);
		} else {
			return false;
		}
		this.integralHistoryDao.update(integralHistory);
		return true;
	}

	@Override
	public boolean saveIntegralHistory(Long integral, ShopMember member, String description,Integer type) {
		IntegralHistory integralHistory = new IntegralHistory();
		integralHistory.setUser(member);
		integralHistory.setIntegral(integral);
		integralHistory.setDescription(description);
		integralHistory.setType(type);
		integralHistory.setTenant(TenantContext.getTenant());
		this.integralHistoryDao.save(integralHistory);
		return true;
	}

	@Override
	public boolean saveIntegralHistory(ShopOrderInfo shopOrderInfo,String description) {
		List<ShopOrderdetail> list=shopOrderInfo.getOrderdetails();
		Long integral=0L;
		for (ShopOrderdetail sDetail : list) {
			ShopProduct sProduct=sDetail.getPro();
			integral+=(sDetail.getNum())*(sProduct.getSendPoints());//获取积分
		}
		IntegralHistory integralHistory = new IntegralHistory();
		integralHistory.setUser(shopOrderInfo.getUser());
		integralHistory.setIntegral(integral);
		integralHistory.setDescription(description);
		integralHistory.setTenant(TenantContext.getTenant());
		this.integralHistoryDao.save(integralHistory);
		return true;
	}	
	
	/**
	 * 获取会员的积分变更记录
	 * @param member
	 * @return
	 */
	@Override
	public IPageList getAllIntegralHistoryByMember(ShopMember member){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.setOrderBy("createDate");
		qo.setOrderType("DESC");
		return this.getIntegralHistoryBy(qo);
	}
}

