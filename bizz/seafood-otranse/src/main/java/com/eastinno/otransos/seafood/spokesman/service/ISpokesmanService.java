package com.eastinno.otransos.seafood.spokesman.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.seafood.spokesman.domain.Spokesman;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
/**
 * SpokesmanService
 * @author ksmwly@gmail.com
 */
public interface ISpokesmanService {
	/**
	 * 保存一个Spokesman，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addSpokesman(Spokesman domain);
	
	/**
	 * 根据一个ID得到Spokesman
	 * 
	 * @param id
	 * @return
	 */
	Spokesman getSpokesman(Long id);
	
	/**
	 * 删除一个Spokesman
	 * @param id
	 * @return
	 */
	boolean delSpokesman(Long id);
	
	/**
	 * 批量删除Spokesman
	 * @param ids
	 * @return
	 */
	boolean batchDelSpokesmans(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Spokesman
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getSpokesmanBy(IQueryObject queryObj);
	
	/**
	  * 更新一个Spokesman
	  * @param id 需要更新的Spokesman的id
	  * @param dir 需要更新的Spokesman
	  */
	boolean updateSpokesman(Long id,Spokesman entity);
	/**
	 * 计算获取极差补贴的人
	 * 
	 * @param properties
	 * @return
	 */
	Map getSubsiSpokesman(Spokesman sman);
	/**
	 * 递归获取上级
	 * 
	 * @param properties
	 * @return
	 */
	public Spokesman getRecursionSpokesman(Spokesman sman); 
	/**
	 * 计算团队业绩累计
	 * @param order
	 */
	public void calculateTeamAccount(ShopOrderInfo order);
	/**
	 * 获取团队业绩累计明细
	 * @param spokesmanid
	 * @return
	 */
	public List getTeamOrderInfo(Long spokesmanid);
}
