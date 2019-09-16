package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IPrizeProDAO;
import com.eastinno.otransos.platform.weixin.domain.PrizePro;
import com.eastinno.otransos.platform.weixin.service.IPrizeProService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * PrizeProServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class PrizeProServiceImpl implements IPrizeProService{
	@Resource
	private IPrizeProDAO prizeProDao;
	
	public void setprizeProDao(IPrizeProDAO prizeProDao){
		this.prizeProDao=prizeProDao;
	}
	
	public Long addPrizePro(PrizePro entity) {
		this.prizeProDao.save(entity);
		if (entity != null && entity.getId() != null) {
			return entity.getId();
		}
		return null;
	}
	
	public PrizePro getPrizePro(Long id) {
		PrizePro entity = this.prizeProDao.get(id);
		return entity;
		}
	
	
	public IPageList getPrizeProBy(IQueryObject queryObj) {	
		return this.prizeProDao.findBy(queryObj);	
	}
	
	public boolean updatePrizePro(Long id, PrizePro entity) {
		if (id != null) {
			entity.setId(id);
		} else {
			return false;
		}
		this.prizeProDao.update(entity);
		return true;
	}

	@Override
	public boolean delPrizePro(Long id) {
		PrizePro entity = this.getPrizePro(id);
        if (entity != null) {
            this.prizeProDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelPrizePros(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
