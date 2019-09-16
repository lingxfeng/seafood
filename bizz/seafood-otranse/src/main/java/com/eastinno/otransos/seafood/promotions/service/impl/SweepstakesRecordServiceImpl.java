package com.eastinno.otransos.seafood.promotions.service.impl;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakesRecord;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakesRecordService;
import com.eastinno.otransos.seafood.promotions.dao.ISweepstakesRecordDAO;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;


/**
 * SweepstakesRecordServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class SweepstakesRecordServiceImpl implements ISweepstakesRecordService{
	@Resource
	private ISweepstakesRecordDAO sweepstakesRecordDao;
	
	public void setSweepstakesRecordDao(ISweepstakesRecordDAO sweepstakesRecordDao){
		this.sweepstakesRecordDao=sweepstakesRecordDao;
	}
	
	public Long addSweepstakesRecord(SweepstakesRecord sweepstakesRecord) {	
		this.sweepstakesRecordDao.save(sweepstakesRecord);
		if (sweepstakesRecord != null && sweepstakesRecord.getId() != null) {
			return sweepstakesRecord.getId();
		}
		return null;
	}
	
	public SweepstakesRecord getSweepstakesRecord(Long id) {
		SweepstakesRecord sweepstakesRecord = this.sweepstakesRecordDao.get(id);
		return sweepstakesRecord;
		}
	
	public boolean delSweepstakesRecord(Long id) {	
			SweepstakesRecord sweepstakesRecord = this.getSweepstakesRecord(id);
			if (sweepstakesRecord != null) {
				this.sweepstakesRecordDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelSweepstakesRecords(List<Serializable> sweepstakesRecordIds) {
		
		for (Serializable id : sweepstakesRecordIds) {
			delSweepstakesRecord((Long) id);
		}
		return true;
	}
	
	public IPageList getSweepstakesRecordBy(IQueryObject queryObj) {	
		return this.sweepstakesRecordDao.findBy(queryObj);		
	}
	
	public boolean updateSweepstakesRecord(Long id, SweepstakesRecord sweepstakesRecord) {
		if (id != null) {
			sweepstakesRecord.setId(id);
		} else {
			return false;
		}
		this.sweepstakesRecordDao.update(sweepstakesRecord);
		return true;
	}	
	public Integer getTodayCount(ShopMember member){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id",member.getId(), "=");
		qo.addQuery("obj.createTime",this.getZore(), ">=");
		qo.addQuery("obj.createTime",this.getFinal(), "<=");
		List<SweepstakesRecord> list = this.sweepstakesRecordDao.findBy(qo).getResult();
		Integer count = 0;
		if(list!=null){
			count = list.size();
		}
		return count;
	}
	public static Date getZore(){
		long current=System.currentTimeMillis();//当前时间毫秒数
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		Timestamp time1 = new Timestamp(zero);
		Date time2 = new Date(time1.getTime());
		return time2;
	}
	public static Date getFinal(){
		long current=System.currentTimeMillis();//当前时间毫秒数
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
		Timestamp time1 = new Timestamp(twelve);
		Date time2 = new Date(time1.getTime());
		return time2;
	}

	@Override
	public SweepstakesRecord getMenberTodayLastSR(ShopMember member) {
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id",member.getId(), "=");
		qo.setOrderBy("createTime");
		qo.setOrderType("desc");
		qo.setPageSize(1);
		qo.addQuery("obj.createTime",this.getZore(), ">=");
		qo.addQuery("obj.createTime",this.getFinal(), "<=");
		List<SweepstakesRecord> list = this.sweepstakesRecordDao.findBy(qo).getResult();
		SweepstakesRecord sweepstakesRecord=null;
		if(list!=null){
			sweepstakesRecord = (SweepstakesRecord)list.get(0);
		}
		return sweepstakesRecord;
	}
}
