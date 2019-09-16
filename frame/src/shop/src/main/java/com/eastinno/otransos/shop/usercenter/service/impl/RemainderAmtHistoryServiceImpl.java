package com.eastinno.otransos.shop.usercenter.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.shop.usercenter.domain.RemainderAmtHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.shop.usercenter.dao.IRemainderAmtHistoryDAO;


/**
 * RemainderAmtHistoryServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class RemainderAmtHistoryServiceImpl implements IRemainderAmtHistoryService{
	@Resource
	private IRemainderAmtHistoryDAO remainderAmtHistoryDao;
	
	public void setRemainderAmtHistoryDao(IRemainderAmtHistoryDAO remainderAmtHistoryDao){
		this.remainderAmtHistoryDao=remainderAmtHistoryDao;
	}
	
	public Long addRemainderAmtHistory(RemainderAmtHistory remainderAmtHistory) {	
		this.remainderAmtHistoryDao.save(remainderAmtHistory);
		if (remainderAmtHistory != null && remainderAmtHistory.getId() != null) {
			return remainderAmtHistory.getId();
		}
		return null;
	}
	
	public RemainderAmtHistory getRemainderAmtHistory(Long id) {
		RemainderAmtHistory remainderAmtHistory = this.remainderAmtHistoryDao.get(id);
		return remainderAmtHistory;
		}
	
	public boolean delRemainderAmtHistory(Long id) {	
			RemainderAmtHistory remainderAmtHistory = this.getRemainderAmtHistory(id);
			if (remainderAmtHistory != null) {
				this.remainderAmtHistoryDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelRemainderAmtHistorys(List<Serializable> remainderAmtHistoryIds) {
		
		for (Serializable id : remainderAmtHistoryIds) {
			delRemainderAmtHistory((Long) id);
		}
		return true;
	}
	
	public IPageList getRemainderAmtHistoryBy(IQueryObject queryObj) {	
		return this.remainderAmtHistoryDao.findBy(queryObj);		
	}
	
	public boolean updateRemainderAmtHistory(Long id, RemainderAmtHistory remainderAmtHistory) {
		if (id != null) {
			remainderAmtHistory.setId(id);
		} else {
			return false;
		}
		this.remainderAmtHistoryDao.update(remainderAmtHistory);
		return true;
	}

	@Override
	public boolean addRemainderAmtHistory(ShopMember member,Integer type,Double amt,String description) {
		RemainderAmtHistory rah = new RemainderAmtHistory();
		rah.setUser(member);
		rah.setType(type);
		rah.setAmt(amt);
		rah.setDescription(description);
		rah.setTenant(TenantContext.getTenant());
		this.remainderAmtHistoryDao.save(rah);
		return true;
	}	
	
}
