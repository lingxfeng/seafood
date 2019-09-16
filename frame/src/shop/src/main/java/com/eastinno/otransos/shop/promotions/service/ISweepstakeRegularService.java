package com.eastinno.otransos.shop.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeRegular;
/**
 * SweepstakeRegularService
 * @author ksmwly@gmail.com
 */
public interface ISweepstakeRegularService {
	/**
	 * 保存一个SweepstakeRegular，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSweepstakeRegular(SweepstakeRegular domain);
	
	/**
	 * 根据一个ID得到SweepstakeRegular
	 * 
	 * @param id
	 * @return
	 */
	SweepstakeRegular getSweepstakeRegular(Long id);
	
	/**
	 * 删除一个SweepstakeRegular
	 * @param id
	 * @return
	 */
	boolean delSweepstakeRegular(Long id);
	
	/**
	 * 批量删除SweepstakeRegular
	 * @param ids
	 * @return
	 */
	boolean batchDelSweepstakeRegulars(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SweepstakeRegular
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSweepstakeRegularBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SweepstakeRegular
	  * @param id 需要更新的SweepstakeRegular的id
	  * @param dir 需要更新的SweepstakeRegular
	  */
	boolean updateSweepstakeRegular(Long id,SweepstakeRegular entity);
	/**
	  * 获取中奖信息
	  * @param 
	  * 
	  */
	int checkSweepstake(int basemin);
	int returnangle(int position);
	int randomNum(int min,int max);
}
