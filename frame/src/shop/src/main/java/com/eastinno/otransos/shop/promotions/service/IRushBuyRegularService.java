package com.eastinno.otransos.shop.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRegular;
/**
 * RushBuyRegularService
 * @author ksmwly@gmail.com
 */
public interface IRushBuyRegularService {
	/**
	 * 保存一个RushBuyRegular，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRushBuyRegular(RushBuyRegular domain);
	
	/**
	 * 根据一个ID得到RushBuyRegular
	 * 
	 * @param id
	 * @return
	 */
	RushBuyRegular getRushBuyRegular(Long id);
	
	/**
	 * 删除一个RushBuyRegular
	 * @param id
	 * @return
	 */
	boolean delRushBuyRegular(Long id);
	
	/**
	 * 批量删除RushBuyRegular
	 * @param ids
	 * @return
	 */
	boolean batchDelRushBuyRegulars(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到RushBuyRegular
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRushBuyRegularBy(IQueryObject queryObj);
	
	/**
	  * 更新一个RushBuyRegular
	  * @param id 需要更新的RushBuyRegular的id
	  * @param dir 需要更新的RushBuyRegular
	  */
	boolean updateRushBuyRegular(Long id,RushBuyRegular entity);
	
	/**
	 * 获取所有的秒杀活动记录
	 * @return
	 */
	public IPageList getAllSecKillRegular();
	
	/**
	 * 通过查询类QueryObject查询记录
	 * @param qo
	 * @return
	 */
	public IPageList getAllSecKillRegularByQO(QueryObject qo);
	
	/**
	 * 获取秒杀列表页展示的秒杀活动
	 * @return
	 */
	public IPageList getAllSecKillRegularForHome();
	
	/**
	 * 创建秒杀活动记录
	 * @param record
	 * @return
	 */
	public RushBuyRegular createSecKillRegular(RushBuyRegular regular);
	
	/**
	 * 更新秒杀活动
	 * @param record
	 * @return
	 */
	public RushBuyRegular updateSecKillRegular(RushBuyRegular regular);
	
	/**
	 * 获取所有的限时抢购活动记录
	 * @return
	 */
	public IPageList getAllTimeLimitRegular();
	
	/**
	 * 通过查询类QueryObject查询记录
	 * @param qo
	 * @return
	 */
	public IPageList getAllTimeLimitRegularByQO(QueryObject qo);
	
	/**
	 * 获取秒杀列表页展示的限时抢购活动
	 * @return
	 */
	public IPageList getAllTimeLimitRegularForHome();
	
	/**
	 * 创建限时抢购活动记录
	 * @param record
	 * @return
	 */
	public RushBuyRegular createTimeLimitRegular(RushBuyRegular regular);
	
	/**
	 * 更新限时抢购活动
	 * @param record
	 * @return
	 */
	public RushBuyRegular updateTimeLimitRegular(RushBuyRegular regular);
	
	/**
	 * 更新显示位置
	 * @param regular
	 */
	public void updateShowPosition(RushBuyRegular regular, String type);
}
