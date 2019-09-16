package com.eastinno.otransos.shop.droduct.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.droduct.dao.IRemoteRegionDAO;
import com.eastinno.otransos.shop.droduct.domain.RemoteRegion;
import com.eastinno.otransos.shop.droduct.service.IRemoteRegionService;

@Service
public class RemoteRegionServiceImpl implements IRemoteRegionService{
	@Resource
	IRemoteRegionDAO remoteRegionDAO;
	
	/**
	 * 依据主键id获取偏远地区记录
	 * @param id
	 * @return
	 */
	public RemoteRegion getRemoteRegion(Long id){
		return this.remoteRegionDAO.get(id);
	}
	
	public IRemoteRegionDAO getRemoteRegionDAO() {
		return remoteRegionDAO;
	}

	public void setRemoteRegionDAO(IRemoteRegionDAO remoteRegionDAO) {
		this.remoteRegionDAO = remoteRegionDAO;
	}

	/**
	 * 添加一条偏远地区记录
	 * @param remoteRegion
	 * @return
	 */
	public RemoteRegion addRemoteRegion(RemoteRegion remoteRegion){
		return this.remoteRegionDAO.save(remoteRegion);		
	}
	
	/**
	 * 删除一条偏远地区记录
	 * @param id
	 * @return
	 */
	public boolean delRemoteRegion(Long id){
		try{
			this.remoteRegionDAO.delete(id);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断该地区是否为偏远地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isRemoteRegion(SystemRegion systemRegion){
		boolean result = false;
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.systemRegion.id", systemRegion.getId(), "=");
		List regionList = this.remoteRegionDAO.findBy(qo).getResult();
		if(regionList!=null && regionList.size() == 1){
			result = true;
		}else{
			result = false;
		}			
		return result;
	}
	
	/**
	 * 批量设置list中的地区为偏远地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetRemoteRegion(List<SystemRegion> systemRegionList){
		this.remoteRegionDAO.deleteAll();
		for(int i=0; i<systemRegionList.size(); ++i){
			SystemRegion systemRegion = systemRegionList.get(i);
			RemoteRegion remoteRegion = new RemoteRegion();
			remoteRegion.setCreateDate(new Date());
			remoteRegion.setModifyDate(new Date());
			remoteRegion.setSystemRegion(systemRegion);
			try{
				this.remoteRegionDAO.save(remoteRegion);
			}catch(Exception e){
				return false;
			}
		}		
		return true;
	}
	
	/**
	 * 获取所有的偏远地区
	 * @return
	 */
	public List<RemoteRegion> getAllRemoteRegion(){
		return this.remoteRegionDAO.findAll();
	}
}
