package com.eastinno.otransos.seafood.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakesRecord;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * SweepstakesRecordService
 * @author ksmwly@gmail.com
 */
public interface ISweepstakesRecordService {
	/**
	 * 保存一个SweepstakesRecord，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSweepstakesRecord(SweepstakesRecord domain);
	
	/**
	 * 根据一个ID得到SweepstakesRecord
	 * 
	 * @param id
	 * @return
	 */
	SweepstakesRecord getSweepstakesRecord(Long id);
	
	/**
	 * 删除一个SweepstakesRecord
	 * @param id
	 * @return
	 */
	boolean delSweepstakesRecord(Long id);
	
	/**
	 * 批量删除SweepstakesRecord
	 * @param ids
	 * @return
	 */
	boolean batchDelSweepstakesRecords(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到SweepstakesRecord
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSweepstakesRecordBy(IQueryObject queryObj);
	
	/**
	  * 更新一个SweepstakesRecord
	  * @param id 需要更新的SweepstakesRecord的id
	  * @param dir 需要更新的SweepstakesRecord
	  */
	boolean updateSweepstakesRecord(Long id,SweepstakesRecord entity);
	/**
	 * 获取当日抽奖次数
	 * @return
	 */
	Integer getTodayCount(ShopMember member);
	
	/**
	 * 获取用户今天最后一次抽奖记录
	 * @return
	 */
	SweepstakesRecord getMenberTodayLastSR(ShopMember member);
}
