package com.eastinno.otransos.seafood.droduct.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.droduct.domain.NearRegion;
import com.eastinno.otransos.seafood.droduct.domain.RegionClass;
/**
 * NearRegionService
 * @author ksmwly@gmail.com
 */
public interface INearRegionService {
	/**
	 * 依据主键id获取附近地区记录
	 * @param id
	 * @return
	 */
	public NearRegion getNearRegion(Long id);
	
	/**
	 * 添加一条附近地区记录
	 * @param remoteRegion
	 * @return
	 */
	public NearRegion addNearRegion(NearRegion remoteRegion);
	
	/**
	 * 删除一条附近地区记录
	 * @param id
	 * @return
	 */
	public boolean delNearRegion(Long id);
	
	/**
	 * 判断该地区是否为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isNearRegion(SystemRegion systemRegion);
	/**
	 * 判断该地区是否为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean isNearRegion2(SystemRegion systemRegion,RegionClass regionClass);
	
	/**
	 * 批量设置list中的地区为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetNearRegion(List<SystemRegion> systemRegionList);
	/**
	 * 批量设置list中的地区为附近地区
	 * @param systemRegion
	 * @return
	 */
	public boolean batchSetNearRegion2(List<SystemRegion> systemRegionList,RegionClass regionClass);
	
	/**
	 * 获取所有的附近地区
	 * @return
	 */
	public List<NearRegion> getAllNearRegion();
	/**
	 * 获取所有的附近地区
	 * @return
	 */
	public List<NearRegion> getAllNearRegion2(RegionClass regionClass);
	
}
