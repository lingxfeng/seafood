package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IPrizeHistoryDAO;
import com.eastinno.otransos.platform.weixin.domain.PrizeHistory;
import com.eastinno.otransos.platform.weixin.service.IPrizeHistoryService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * PrizeHistoryServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class PrizeHistoryServiceImpl implements IPrizeHistoryService{
	@Resource
	private IPrizeHistoryDAO prizeHistoryDao;
	
	public void setprizeHistoryDao(IPrizeHistoryDAO prizeHistoryDao){
		this.prizeHistoryDao=prizeHistoryDao;
	}
	
	public Long addPrizeHistory(PrizeHistory entity) {
		this.prizeHistoryDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public PrizeHistory getPrizeHistory(Long id) {
		PrizeHistory entity = this.prizeHistoryDao.get(id);
		return entity;
		}
	
	
	public IPageList getPrizeHistoryBy(IQueryObject queryObj) {	
		return this.prizeHistoryDao.findBy(queryObj);	
	}
	
	public boolean updatePrizeHistory(Long id, PrizeHistory entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.prizeHistoryDao.update(entity);
		return true;
	}

	@Override
	public boolean delPrizeHistory(Long id) {
		PrizeHistory entity = this.getPrizeHistory(id);
        if (entity != null) {
            this.prizeHistoryDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelPrizeHistorys(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
