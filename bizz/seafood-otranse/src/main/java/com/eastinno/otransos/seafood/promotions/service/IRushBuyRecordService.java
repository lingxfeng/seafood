package com.eastinno.otransos.seafood.promotions.service;

import java.io.Serializable;
import java.util.List;

import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
/**
 * RushBuyRecordService
 * @author ksmwly@gmail.com
 */
public interface IRushBuyRecordService {
	/**
	 * 保存一个RushBuyRecord，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRushBuyRecord(RushBuyRecord domain);
	
	/**
	 * 根据一个ID得到RushBuyRecord
	 * 
	 * @param id
	 * @return
	 */
	RushBuyRecord getRushBuyRecord(Long id);
	
	/**
	 * 删除一个RushBuyRecord
	 * @param id
	 * @return
	 */
	boolean delRushBuyRecord(Long id);
	
	/**
	 * 批量删除RushBuyRecord
	 * @param ids
	 * @return
	 */
	boolean batchDelRushBuyRecords(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到RushBuyRecord
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRushBuyRecordBy(IQueryObject queryObj);
	
	/**
	  * 更新一个RushBuyRecord
	  * @param id 需要更新的RushBuyRecord的id
	  * @param dir 需要更新的RushBuyRecord
	  */
	boolean updateRushBuyRecord(Long id,RushBuyRecord entity);
	
	/**
	 * 获取所有的秒杀活动记录
	 * @return
	 */
	public IPageList getAllSecKillRecord();

	/**
	 * 获取所有对应于秒杀活动活动的抢购记录
	 * @param regular
	 * @return
	 */
	public IPageList getAllSecKillRecordByRegular(RushBuyRegular regular);
	
	/**
	 * 通过查询类QueryObject查询记录
	 * @param qo
	 * @return
	 */
	public IPageList getAllSecKillRecordByQO(QueryObject qo);
	
	/**
	 * 获取所有有效的抢购记录
	 * @return
	 */
	public IPageList getAllAvailableSecKillRecord();
	
	/**
	 * 获取所有有效的对应于秒杀活动活动的抢购记录
	 * @param regular
	 * @return
	 */
	public IPageList getAllAvailableSecKillRecordByRegular(RushBuyRegular regular);
	
	/**
	 * 获取所有有效的对应于用户的抢购记录
	 * @param member
	 * @return
	 */
	public IPageList getAllAvailableSecKillRecordByMember(ShopMember member);
	
	/**
	 * 获取所有有效的对应于用户和活动的抢购记录
	 * @param member
	 * @return
	 */
	public IPageList getAllAvailableSecKillRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member);
	
	/**
	 * 通过活动和会员信息获取记录
	 * @param regular
	 * @param member
	 * @return
	 */
	public RushBuyRecord getSingleAvailableSecKillRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member);
	
	/**
	 * 创建秒杀活动记录
	 * @param record
	 * @return
	 */
	public RushBuyRecord createSecKillRecord(RushBuyRecord record);
	
	/**
	 * 更新秒杀活动记录
	 * @param record
	 * @return
	 */
	public RushBuyRecord updateSecKillRecord(RushBuyRecord record);
	
	/**
	 * 获取所有的限时抢购活动记录
	 * @return
	 */
	public IPageList getAllTimeLimitRecord();

	/**
	 * 获取所有对应于限时抢购活动活动的抢购记录
	 * @param regular
	 * @return
	 */
	public IPageList getAllTimeLimitRecordByRegular(RushBuyRegular regular);
	
	/**
	 * 通过查询类QueryObject查询记录
	 * @param qo
	 * @return
	 */
	public IPageList getAllTimeLimitRecordByQO(QueryObject qo);
	
	/**
	 * 获取所有有效的抢购记录
	 * @return
	 */
	public IPageList getAllAvailableTimeLimitRecord();
	
	/**
	 * 获取所有有效的对应于限时抢购活动活动的抢购记录
	 * @param regular
	 * @return
	 */
	public IPageList getAllAvailableTimeLimitRecordByRegular(RushBuyRegular regular);
	
	/**
	 * 获取所有有效的对应于用户的抢购记录
	 * @param member
	 * @return
	 */
	public IPageList getAllAvailableTimeLimitRecordByMember(ShopMember member);
	
	/**
	 * 获取所有有效的对应于用户和活动的抢购记录
	 * @param member
	 * @return
	 */
	public IPageList getAllAvailableTimeLimitRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member);
	
	/**
	 * 通过活动和会员信息获取记录
	 * @param regular
	 * @param member
	 * @return
	 */
	public RushBuyRecord getSingleAvailableTimeLimitRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member);
	
	/**
	 * 创建限时抢购活动记录
	 * @param record
	 * @return
	 */
	public RushBuyRecord createTimeLimitRecord(RushBuyRecord record);
	
	/**
	 * 更新限时抢购活动记录
	 * @param record
	 * @return
	 */
	public RushBuyRecord updateTimeLimitRecord(RushBuyRecord record);
}
