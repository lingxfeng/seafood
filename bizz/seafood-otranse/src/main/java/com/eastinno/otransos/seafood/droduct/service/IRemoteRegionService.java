package com.eastinno.otransos.seafood.droduct.service;

import java.util.List;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
import com.eastinno.otransos.seafood.droduct.domain.RemoteRegion;
/**
 * 偏远地区事务类
 * @author wb
 *
 */
public interface IRemoteRegionService{
	/**
	 * 依据主键id获取偏远地区记录
	 * @param id
	 * @return
	 */
	public RemoteRegion getRemoteRegion(Long id);
	
	/**
	 * 添加一条偏远地区记录
	 * @param remoteRegion
	 * @return
	 */
	public RemoteRegion addRemoteRegion(RemoteRegion remoteRegion);
	
	/**
	 * 删除一条偏远地区记录
	 * @param id
	 * @return
	 */
	public boolean delRemoteRegion(Long id);
	
	/**
	 * 判断该地区是否为偏远地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isRemoteRegion(SystemRegion systemRegion);
	
	/**
	 * 判断该地区是否为偏远地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isRemoteRegion2(SystemRegion systemRegion,RegionClass regionClass);
	
	/**
	 * 批量设置list中的地区为偏远地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetRemoteRegion(List<SystemRegion> systemRegionList);
	/**
	 * 批量设置list中的地区为偏远地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetRemoteRegion2(List<SystemRegion> systemRegionList,RegionClass regionClass);
	/**
	 * 获取所有的偏远地区
	 * @return
	 */
	public List<RemoteRegion> getAllRemoteRegion();
	/**
	 * 获取所有的偏远地区
	 * @return
	 */
	public List<RemoteRegion> getAllRemoteRegion2(RegionClass regionClass);
}
