package com.eastinno.otransos.shop.promotions.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeSystemConfig;
import com.eastinno.otransos.shop.promotions.service.ISweepstakeSystemConfigService;
import com.eastinno.otransos.shop.promotions.dao.ISweepstakeSystemConfigDAO;


/**
 * SweepstakeSystemConfigServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SweepstakeSystemConfigServiceImpl implements ISweepstakeSystemConfigService{
	@Resource
	private ISweepstakeSystemConfigDAO sweepstakeSystemConfigDao;
	
	public void setSweepstakeSystemConfigDao(ISweepstakeSystemConfigDAO sweepstakeSystemConfigDao){
		this.sweepstakeSystemConfigDao=sweepstakeSystemConfigDao;
	}
	
	public Long addSweepstakeSystemConfig(SweepstakeSystemConfig sweepstakeSystemConfig) {	
		this.sweepstakeSystemConfigDao.save(sweepstakeSystemConfig);
		if (sweepstakeSystemConfig != null && sweepstakeSystemConfig.getId() != null) {
			return sweepstakeSystemConfig.getId();
		}
		return null;
	}
	
	public SweepstakeSystemConfig getSweepstakeSystemConfig(Long id) {
		SweepstakeSystemConfig sweepstakeSystemConfig = this.sweepstakeSystemConfigDao.get(id);
		return sweepstakeSystemConfig;
		}
	
	public boolean delSweepstakeSystemConfig(Long id) {	
			SweepstakeSystemConfig sweepstakeSystemConfig = this.getSweepstakeSystemConfig(id);
			if (sweepstakeSystemConfig != null) {
				this.sweepstakeSystemConfigDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSweepstakeSystemConfigs(List<Serializable> sweepstakeSystemConfigIds) {
		
		for (Serializable id : sweepstakeSystemConfigIds) {
			delSweepstakeSystemConfig((Long) id);
		}
		return true;
	}
	
	public IPageList getSweepstakeSystemConfigBy(IQueryObject queryObj) {	
		return this.sweepstakeSystemConfigDao.findBy(queryObj);		
	}
	
	public boolean updateSweepstakeSystemConfig(Long id, SweepstakeSystemConfig sweepstakeSystemConfig) {
		if (id != null) {
			sweepstakeSystemConfig.setId(id);
		} else {
			return false;
		}
		this.sweepstakeSystemConfigDao.update(sweepstakeSystemConfig);
		return true;
	}	
	
}
