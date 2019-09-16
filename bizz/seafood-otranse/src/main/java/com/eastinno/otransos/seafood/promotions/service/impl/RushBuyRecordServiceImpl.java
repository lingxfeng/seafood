package com.eastinno.otransos.seafood.promotions.service.impl;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.promotions.dao.IRushBuyRecordDAO;


/**
 * RushBuyRecordServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class RushBuyRecordServiceImpl implements IRushBuyRecordService{
	@Resource
	private IRushBuyRecordDAO rushBuyRecordDao;
	
	public void setRushBuyRecordDao(IRushBuyRecordDAO rushBuyRecordDao){
		this.rushBuyRecordDao=rushBuyRecordDao;
	}
	
	public Long addRushBuyRecord(RushBuyRecord rushBuyRecord) {	
		this.rushBuyRecordDao.save(rushBuyRecord);
		if (rushBuyRecord != null && rushBuyRecord.getId() != null) {
			return rushBuyRecord.getId();
		}
		return null;
	}
	
	public RushBuyRecord getRushBuyRecord(Long id) {
		RushBuyRecord rushBuyRecord = this.rushBuyRecordDao.get(id);
		return rushBuyRecord;
		}
	
	public boolean delRushBuyRecord(Long id) {	
			RushBuyRecord rushBuyRecord = this.getRushBuyRecord(id);
			if (rushBuyRecord != null) {
				this.rushBuyRecordDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelRushBuyRecords(List<Serializable> rushBuyRecordIds) {
		
		for (Serializable id : rushBuyRecordIds) {
			delRushBuyRecord((Long) id);
		}
		return true;
	}
	
	public IPageList getRushBuyRecordBy(IQueryObject queryObj) {	
		return this.rushBuyRecordDao.findBy(queryObj);		
	}
	
	public boolean updateRushBuyRecord(Long id, RushBuyRecord rushBuyRecord) {
		if (id != null) {
			rushBuyRecord.setId(id);
		} else {
			return false;
		}
		this.rushBuyRecordDao.update(rushBuyRecord);
		return true;
	}	
	
	@Override
	public IPageList getAllSecKillRecord() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 0, "=");
		return this.getRushBuyRecordBy(qo);		
	}

	@Override
	public IPageList getAllAvailableSecKillRecord() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 0, "=");		
		return this.filterExpireResult(this.getRushBuyRecordBy(qo), false);
	}

	@Override
	public IPageList getAllSecKillRecordByRegular(RushBuyRegular regular) {
		if(regular.getActivityType() != 0){
			return null;
		}
		QueryObject qo = new QueryObject();		
		qo.addQuery("obj.regular", regular, "=");
		return this.getRushBuyRecordBy(qo);
	}

	@Override
	public IPageList getAllSecKillRecordByQO(QueryObject qo) {	
		qo.addQuery("obj.regular.activityType", 0, "=");
		return this.getRushBuyRecordBy(qo);
	}

	@Override
	public IPageList getAllAvailableSecKillRecordByRegular(RushBuyRegular regular) {
		if(regular.getActivityType() != 0){
			return null;
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular", regular, "=");
		qo.addQuery("obj.order.status",-1, "!=");
		IPageList pageList = this.filterExpireResult(this.getRushBuyRecordBy(qo), false);
		return pageList;
	}

	@Override
	public IPageList getAllAvailableSecKillRecordByMember(ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 0, "=");
		qo.addQuery("obj.member", member, "=");		
		return this.filterExpireResult(this.getRushBuyRecordBy(qo), false);
	}

	@Override
	public RushBuyRecord createSecKillRecord(RushBuyRecord record) {
		if(record.getRegular().getActivityType() != 0){
			return null;
		}
		Long id = this.addRushBuyRecord(record);
		return this.getRushBuyRecord(id);
	}

	@Override
	public RushBuyRecord updateSecKillRecord(RushBuyRecord record) {
		if(record.getRegular().getActivityType() != 0){
			return null;
		}
		if(this.updateRushBuyRecord(record.getId(), record)){
			return this.getRushBuyRecord(record.getId());
		}
		return null;
	}

	@Override
	public IPageList getAllAvailableSecKillRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 0, "=");
		qo.addQuery("obj.regular.id", regular.getId(), "=");
		qo.addQuery("obj.member.id", member.getId(), "=");
		IPageList pageList = this.getRushBuyRecordBy(qo);
		return this.filterExpireResult(pageList, false);
	}
	
	@Override
	public RushBuyRecord getSingleAvailableSecKillRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member){
		IPageList pageList = this.getAllAvailableSecKillRecordByMemberAndRegular(regular, member);
		List<RushBuyRecord> pageResult = pageList.getResult();
		if(pageResult == null){
			return null;
		}
		if(pageResult.size() == 0){
			return null;
		}
		return pageResult.get(0);
	}
	@Override
	public IPageList getAllTimeLimitRecord() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 1, "=");
		return this.getRushBuyRecordBy(qo);		
	}

	@Override
	public IPageList getAllAvailableTimeLimitRecord() {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 1, "=");		
		return this.filterExpireResult(this.getRushBuyRecordBy(qo), false);
	}

	@Override
	public IPageList getAllTimeLimitRecordByRegular(RushBuyRegular regular) {
		if(regular.getActivityType() != 1){
			return null;
		}
		QueryObject qo = new QueryObject();		
		qo.addQuery("obj.regular", regular, "=");
		return this.getRushBuyRecordBy(qo);
	}

	@Override
	public IPageList getAllTimeLimitRecordByQO(QueryObject qo) {	
		qo.addQuery("obj.regular.activityType", 1, "=");
		return this.getRushBuyRecordBy(qo);
	}

	@Override
	public IPageList getAllAvailableTimeLimitRecordByRegular(RushBuyRegular regular) {
		if(regular.getActivityType() != 1){
			return null;
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular", regular, "=");
		qo.addQuery("obj.order.status",-1, "!=");
		IPageList pageList = this.filterExpireResult(this.getRushBuyRecordBy(qo), false);
		return pageList;
	}

	@Override
	public IPageList getAllAvailableTimeLimitRecordByMember(ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 1, "=");
		qo.addQuery("obj.member", member, "=");		
		return this.filterExpireResult(this.getRushBuyRecordBy(qo), false);
	}

	@Override
	public RushBuyRecord createTimeLimitRecord(RushBuyRecord record) {
		if(record.getRegular().getActivityType() != 1){
			return null;
		}
		Long id = this.addRushBuyRecord(record);
		return this.getRushBuyRecord(id);
	}

	@Override
	public RushBuyRecord updateTimeLimitRecord(RushBuyRecord record) {
		if(record.getRegular().getActivityType() != 1){
			return null;
		}
		if(this.updateRushBuyRecord(record.getId(), record)){
			return this.getRushBuyRecord(record.getId());
		}
		return null;
	}

	@Override
	public IPageList getAllAvailableTimeLimitRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.regular.activityType", 1, "=");
		qo.addQuery("obj.regular.id", regular.getId(), "=");
		qo.addQuery("obj.member.id", member.getId(), "=");
		IPageList pageList = this.getRushBuyRecordBy(qo);
		return this.filterExpireResult(pageList, false);
	}
	
	@Override
	public RushBuyRecord getSingleAvailableTimeLimitRecordByMemberAndRegular(RushBuyRegular regular, ShopMember member){
		IPageList pageList = this.getAllAvailableTimeLimitRecordByMemberAndRegular(regular, member);
		List<RushBuyRecord> pageResult = pageList.getResult();
		if(pageResult == null){
			return null;
		}
		if(pageResult.size() == 0){
			return null;
		}
		return pageResult.get(0);
	}
		
	/**
	 * 按照记录过期与否筛选记录
	 * @param pageList
	 * @return
	 */
	private IPageList filterExpireResult(IPageList pageList, boolean isOutExpire){
		List<RushBuyRecord> list = pageList.getResult();
		if(list == null){
			return pageList;
		}
		for(int i=0; i<list.size(); ++i){
			if(list.get(i).isOutExpire() != isOutExpire && list.get(i).getOrder() != null){
				list.remove(i);
				--i;
			}
		}
		return pageList;
	}
}
