package com.eastinno.otransos.shop.usercenter.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.shop.usercenter.domain.RemainderAmtHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * RemainderAmtHistoryService
 * @author ksmwly@gmail.com
 */
public interface IRemainderAmtHistoryService {
	/**
	 * 保存一个RemainderAmtHistory，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRemainderAmtHistory(RemainderAmtHistory domain);
	
	/**
	 * 根据一个ID得到RemainderAmtHistory
	 * 
	 * @param id
	 * @return
	 */
	RemainderAmtHistory getRemainderAmtHistory(Long id);
	
	/**
	 * 删除一个RemainderAmtHistory
	 * @param id
	 * @return
	 */
	boolean delRemainderAmtHistory(Long id);
	
	/**
	 * 批量删除RemainderAmtHistory
	 * @param ids
	 * @return
	 */
	boolean batchDelRemainderAmtHistorys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到RemainderAmtHistory
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRemainderAmtHistoryBy(IQueryObject queryObj);
	
	/**
	  * 更新一个RemainderAmtHistory
	  * @param id 需要更新的RemainderAmtHistory的id
	  * @param dir 需要更新的RemainderAmtHistory
	  */
	boolean updateRemainderAmtHistory(Long id,RemainderAmtHistory entity);
	
	/**
	  * 保存一个RemainderAmtHistory
	  * @param member 用户
	  * @param amt 金额
	  * @param description 描述
	  */
	boolean addRemainderAmtHistory(ShopMember member,Integer type,Double amt,String description);
}
