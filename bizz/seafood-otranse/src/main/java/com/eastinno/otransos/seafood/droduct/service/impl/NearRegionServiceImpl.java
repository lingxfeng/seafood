package com.eastinno.otransos.seafood.droduct.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.NearRegion;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
import com.eastinno.otransos.seafood.droduct.domain.RemoteRegion;
import com.eastinno.otransos.seafood.droduct.domain.NearRegion;
import com.eastinno.otransos.seafood.droduct.service.INearRegionService;
import com.eastinno.otransos.seafood.droduct.dao.INearRegionDAO;
import com.eastinno.otransos.seafood.droduct.dao.INearRegionDAO;


/**
 * NearRegionServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class NearRegionServiceImpl implements INearRegionService{
	@Resource
	INearRegionDAO nearRegionDAO;
	
	/**
	 * 依据主键id获取附近地区记录
	 * @param id
	 * @return
	 */
	public NearRegion getNearRegion(Long id){
		return this.nearRegionDAO.get(id);
	}
	
	public INearRegionDAO getNearRegionDAO() {
		return nearRegionDAO;
	}

	public void setNearRegionDAO(INearRegionDAO nearRegionDAO) {
		this.nearRegionDAO = nearRegionDAO;
	}

	/**
	 * 添加一条附近地区记录
	 * @param remoteRegion
	 * @return
	 */
	public NearRegion addNearRegion(NearRegion remoteRegion){
		return this.nearRegionDAO.save(remoteRegion);		
	}
	
	/**
	 * 删除一条附近地区记录
	 * @param id
	 * @return
	 */
	public boolean delNearRegion(Long id){
		try{
			this.nearRegionDAO.delete(id);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断该地区是否为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isNearRegion(SystemRegion systemRegion){
		boolean result = false;
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.systemRegion.id", systemRegion.getId(), "=");
		List regionList = this.nearRegionDAO.findBy(qo).getResult();
		if(regionList!=null && regionList.size() == 1){
			result = true;
		}else{
			result = false;
		}			
		return result;
	}
	/**
	 * 判断该地区是否为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isNearRegion2(SystemRegion systemRegion,RegionClass regionClass){
		boolean result = false;
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.systemRegion.id", systemRegion.getId(), "=");
		qo.addQuery("obj.regionClass.id",regionClass.getId(),"=");
		System.out.println("systemRegion.getId()"+systemRegion.getId()+"。regionClass.getId()"+regionClass.getId());
		List regionList = this.nearRegionDAO.findBy(qo).getResult();
		if(regionList!=null && regionList.size() != 0){
			System.out.println("regionList.size()"+regionList.size());
			result = true;
		}else{
			result = false;
		}			
		return result;
	}
	/**
	 * 批量设置list中的地区为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetNearRegion(List<SystemRegion> systemRegionList){
		this.nearRegionDAO.deleteAll();
		for(int i=0; i<systemRegionList.size(); ++i){
			SystemRegion systemRegion = systemRegionList.get(i);
			NearRegion remoteRegion = new NearRegion();
			remoteRegion.setCreateDate(new Date());
			remoteRegion.setModifyDate(new Date());
			remoteRegion.setSystemRegion(systemRegion);
			try{
				this.nearRegionDAO.save(remoteRegion);
			}catch(Exception e){
				return false;
			}
		}		
		return true;
	}
	/**
	 * 批量设置list中的地区为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetNearRegion2(List<SystemRegion> systemRegionList,RegionClass regionClass){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regionClass.id",regionClass.getId(),"=");
		qo.setLimit(-1);
		System.out.println("regionClass.getId()"+regionClass.getId());
		List<NearRegion> list = this.nearRegionDAO.findBy(qo).getResult();
		if(list != null && list.size() != 0){
			System.out.println("需要刪除的附近地區"+list.size()+"条");
			for(NearRegion nearRegion:list){
				this.nearRegionDAO.delete(nearRegion);
			}
			
		}
		for(int i=0; i<systemRegionList.size(); ++i){
			SystemRegion systemRegion = systemRegionList.get(i);
			NearRegion remoteRegion = new NearRegion();
			remoteRegion.setCreateDate(new Date());
			remoteRegion.setModifyDate(new Date());
			remoteRegion.setSystemRegion(systemRegion);
			remoteRegion.setRegionClass(regionClass);
			try{
				this.nearRegionDAO.save(remoteRegion);
			}catch(Exception e){
				return false;
			}
		}		
		return true;
	}
	/**
	 * 获取所有的附近地区
	 * @return
	 */
	public List<NearRegion> getAllNearRegion(){
		return this.nearRegionDAO.findAll();
	}
	/**
	 * 获取所有的附近地区
	 * @return
	 */
	public List<NearRegion> getAllNearRegion2(RegionClass regionClass){
		QueryObject qo = new QueryObject();
		qo.setLimit(-1);
		qo.addQuery("obj.regionClass.id",regionClass.getId(),"=");
		List<NearRegion> list = this.nearRegionDAO.findBy(qo).getResult();
		return list;
	}
}
