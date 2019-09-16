package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
import com.eastinno.otransos.seafood.droduct.service.IRegionClassService;
import com.eastinno.otransos.seafood.droduct.dao.IRegionClassDAO;


/**
 * RegionClassServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class RegionClassServiceImpl implements IRegionClassService{
	@Resource
	private IRegionClassDAO regionClassDao;
	
	public void setRegionClassDao(IRegionClassDAO regionClassDao){
		this.regionClassDao=regionClassDao;
	}
	
	public Long addRegionClass(RegionClass regionClass) {	
		this.regionClassDao.save(regionClass);
		if (regionClass != null && regionClass.getId() != null) {
			return regionClass.getId();
		}
		return null;
	}
	
	public RegionClass getRegionClass(Long id) {
		RegionClass regionClass = this.regionClassDao.get(id);
		return regionClass;
		}
	
	public boolean delRegionClass(Long id) {	
			RegionClass regionClass = this.getRegionClass(id);
			if (regionClass != null) {
				this.regionClassDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelRegionClasss(List<Serializable> regionClassIds) {
		
		for (Serializable id : regionClassIds) {
			delRegionClass((Long) id);
		}
		return true;
	}
	
	public IPageList getRegionClassBy(IQueryObject queryObj) {	
		return this.regionClassDao.findBy(queryObj);		
	}
	
	public boolean updateRegionClass(Long id, RegionClass regionClass) {
		if (id != null) {
			regionClass.setId(id);
		} else {
			return false;
		}
		this.regionClassDao.update(regionClass);
		return true;
	}	
	
}
