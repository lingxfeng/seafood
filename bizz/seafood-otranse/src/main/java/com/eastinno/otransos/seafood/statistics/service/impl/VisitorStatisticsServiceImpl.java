package com.eastinno.otransos.seafood.statistics.service.impl;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.statistics.domain.VisitorStatistics;
import com.eastinno.otransos.seafood.statistics.service.IVisitorStatisticsService;
import com.eastinno.otransos.seafood.util.DateUtil;
import com.eastinno.otransos.seafood.statistics.dao.IVisitorStatisticsDAO;


/**
 * VisitorStatisticsServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class VisitorStatisticsServiceImpl implements IVisitorStatisticsService{
	@Resource
	private IVisitorStatisticsDAO visitorStatisticsDao;
	
	public void setVisitorStatisticsDao(IVisitorStatisticsDAO visitorStatisticsDao){
		this.visitorStatisticsDao=visitorStatisticsDao;
	}
	
	public Long addVisitorStatistics(VisitorStatistics visitorStatistics) {	
		this.visitorStatisticsDao.save(visitorStatistics);
		if (visitorStatistics != null && visitorStatistics.getId() != null) {
			return visitorStatistics.getId();
		}
		return null;
	}
	
	public VisitorStatistics getVisitorStatistics(Long id) {
		VisitorStatistics visitorStatistics = this.visitorStatisticsDao.get(id);
		return visitorStatistics;
		}
	
	public boolean delVisitorStatistics(Long id) {	
			VisitorStatistics visitorStatistics = this.getVisitorStatistics(id);
			if (visitorStatistics != null) {
				this.visitorStatisticsDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelVisitorStatisticss(List<Serializable> visitorStatisticsIds) {
		
		for (Serializable id : visitorStatisticsIds) {
			delVisitorStatistics((Long) id);
		}
		return true;
	}
	
	public IPageList getVisitorStatisticsBy(IQueryObject queryObj) {	
		return this.visitorStatisticsDao.findBy(queryObj);		
	}
	
	public boolean updateVisitorStatistics(Long id, VisitorStatistics visitorStatistics) {
		if (id != null) {
			visitorStatistics.setId(id);
		} else {
			return false;
		}
		this.visitorStatisticsDao.update(visitorStatistics);
		return true;
	}
	@Override
	public List<String> getCount(){
		List<String> liststr = new ArrayList<>();
		String sqlip = "SELECT SUM(t.visitorCountIp) from disco_shop_visitorstatistics t ";
		String sqlpv = "SELECT SUM(t.visitorCountPV) from disco_shop_visitorstatistics t";
		String sqlmember = "SELECT SUM(t.visitorCountMember) from disco_shop_visitorstatistics t";
		List<?> listip = this.visitorStatisticsDao.queryBySql(sqlip);
		String strip = JSON.toJSONString(listip);
		System.out.println(strip);
		liststr.add(0, strip);
		List<?> listpv = this.visitorStatisticsDao.queryBySql(sqlpv);
		String strpv = JSON.toJSONString(listpv);
		System.out.println(strpv);
		liststr.add(1, strpv);
		List<?> listmember = this.visitorStatisticsDao.queryBySql(sqlmember);
		String strmember = JSON.toJSONString(listmember);
		System.out.println(strmember);
		liststr.add(2, strmember);
		return liststr;
	}
	@Override
	public List<String> getCountDay(){
		QueryObject qo = new QueryObject();
		List<String> list2 = new ArrayList<>();
		String nowday = DateUtil.getNowDay();
		qo.addQuery("obj.currentDay",nowday, "=");
		List<VisitorStatistics> list = this.visitorStatisticsDao.findBy(qo).getResult();
		if(list != null && list.size() != 0){
			VisitorStatistics vs = list.get(0);
			list2.add(0, String.valueOf(vs.getVisitorCountIp()));
			list2.add(1, String.valueOf(vs.getVisitorCountPV()));
			list2.add(2, String.valueOf(vs.getVisitorCountMember()));
		}else{
			list2.add(0, "0");
			list2.add(1, "0");
			list2.add(2, "0");
		}
		return list2;
	}
	@Override
	public List<String> getCountMonth(){
		List<String> liststr = new ArrayList<>();
		String nowmonth = DateUtil.getNowMonth();
		String sqlip = "SELECT SUM(t.visitorCountIp) from disco_shop_visitorstatistics t where t.monthflag ='"+nowmonth+"' "+"GROUP BY t.monthflag";
		String sqlpv = "SELECT SUM(t.visitorCountPV) from disco_shop_visitorstatistics t where t.monthflag ='"+nowmonth+"' "+"GROUP BY t.monthflag";
		String sqlmember = "SELECT SUM(t.visitorCountMember) from disco_shop_visitorstatistics t where t.monthflag ='"+nowmonth+"' "+"GROUP BY t.monthflag";
		List<?> listip = this.visitorStatisticsDao.queryBySql(sqlip);
		String strip = "0";
		if(listip != null){
			strip = JSON.toJSONString(listip);
		}
		System.out.println(strip);
		liststr.add(0, strip);
		
		List<?> listpv = this.visitorStatisticsDao.queryBySql(sqlpv);
		String strpv = "0";
		if(listpv != null){
			strpv = JSON.toJSONString(listpv);
		}
		System.out.println(strpv);
		liststr.add(1, strpv);
		
		List<?> listmember = this.visitorStatisticsDao.queryBySql(sqlmember);
		String strmember = "0";
		if(listmember != null){
			strmember = JSON.toJSONString(listmember);
		}
		System.out.println(strmember);
		liststr.add(2, strmember);
		return liststr;
	}@Override
	public List<String> getCountYear(){
		List<String> liststr = new ArrayList<>();
		String nowyear = DateUtil.getNowYear();
		String sqlip = "SELECT SUM(t.visitorCountIp) from disco_shop_visitorstatistics t where t.yearflag ='"+nowyear+"' "+"GROUP BY t.yearflag";
		String sqlpv = "SELECT SUM(t.visitorCountPV) from disco_shop_visitorstatistics t where t.yearflag ='"+nowyear+"' "+"GROUP BY t.yearflag";
		String sqlmember = "SELECT SUM(t.visitorCountMember) from disco_shop_visitorstatistics t where t.yearflag ='"+nowyear+"' "+"GROUP BY t.yearflag";
		List<?> listip = this.visitorStatisticsDao.queryBySql(sqlip);
		String strip = "0";
		if(listip != null){
			strip = JSON.toJSONString(listip);
		}
		System.out.println(strip);
		liststr.add(0, strip);
		
		List<?> listpv = this.visitorStatisticsDao.queryBySql(sqlpv);
		String strpv = "0";
		if(listpv != null){
			strpv = JSON.toJSONString(listpv);
		}
		System.out.println(strpv);
		liststr.add(1, strpv);
		
		List<?> listmember = this.visitorStatisticsDao.queryBySql(sqlmember);
		String strmember = "0";
		if(listmember != null){
			strmember = JSON.toJSONString(listmember);
		}
		System.out.println(strmember);
		liststr.add(2, strmember);
		return liststr;
	}
	@Override
	public List<String> getAllDayData() {
		QueryObject qo = new QueryObject();
		List<String> list2 = new ArrayList<>();
		qo.setOrderBy("createDate");
		qo.setOrderType("asc");
		qo.setPageSize(-1);
		StringBuffer keystr = new StringBuffer();
		StringBuffer valstrip = new StringBuffer();
		StringBuffer valstrpv = new StringBuffer();
		StringBuffer valstrmem = new StringBuffer();
		List<VisitorStatistics> list = this.visitorStatisticsDao.findBy(qo).getResult();
		if(list != null && list.size() != 0){
			for(VisitorStatistics vs:list){
				keystr.append("\"").append(vs.getCurrentDay()).append("\",");
				valstrip.append(vs.getVisitorCountIp()).append(",");
				valstrpv.append(vs.getVisitorCountPV()).append(",");
				valstrmem.append(vs.getVisitorCountMember()).append(",");
			}
			list2.add(0, keystr.toString());
			list2.add(1, valstrip.toString());
			list2.add(2, valstrpv.toString());
			list2.add(3, valstrmem.toString());
		}else{
			list2.add(0, "日期");
			list2.add(1, "0");
			list2.add(2, "0");
			list2.add(3, "0");
		}
		return list2;
	}

	@Override
	public List<String> getAllMonthData() {
		List<String> liststr = new ArrayList<>();
		String sqlip = "SELECT t.monthflag,SUM(t.visitorCountIp) from disco_shop_visitorstatistics t GROUP BY t.monthflag ORDER BY t.monthflag";
		String sqlpv = "SELECT t.monthflag,SUM(t.visitorCountPV) from disco_shop_visitorstatistics t GROUP BY t.monthflag ORDER BY t.monthflag";
		String sqlmember = "SELECT t.monthflag,SUM(t.visitorCountMember) from disco_shop_visitorstatistics t GROUP BY t.monthflag ORDER BY t.monthflag";
		List<?> listip = this.visitorStatisticsDao.queryBySql(sqlip);
		String strip = JSON.toJSONString(listip);
		System.out.println(strip);
		liststr.add(0, strip);
		List<?> listpv = this.visitorStatisticsDao.queryBySql(sqlpv);
		String strpv = JSON.toJSONString(listpv);
		System.out.println(strpv);
		liststr.add(1, strpv);
		List<?> listmember = this.visitorStatisticsDao.queryBySql(sqlmember);
		String strmember = JSON.toJSONString(listmember);
		System.out.println(strmember);
		liststr.add(2, strmember);
		return liststr;
	}

	@Override
	public List<String> getAllYearData() {
		List<String> liststr = new ArrayList<>();
		String sqlip = "SELECT t.yearflag,SUM(t.visitorCountIp) from disco_shop_visitorstatistics t GROUP BY t.yearflag ORDER BY t.yearflag";
		String sqlpv = "SELECT t.yearflag,SUM(t.visitorCountPV) from disco_shop_visitorstatistics t GROUP BY t.yearflag ORDER BY t.yearflag";
		String sqlmember = "SELECT t.yearflag,SUM(t.visitorCountMember) from disco_shop_visitorstatistics t GROUP BY t.yearflag ORDER BY t.yearflag";
		List<?> listip = this.visitorStatisticsDao.queryBySql(sqlip);
		String strip = JSON.toJSONString(listip);
		System.out.println(strip);
		liststr.add(0, strip);
		List<?> listpv = this.visitorStatisticsDao.queryBySql(sqlpv);
		String strpv = JSON.toJSONString(listpv);
		System.out.println(strpv);
		liststr.add(1, strpv);
		List<?> listmember = this.visitorStatisticsDao.queryBySql(sqlmember);
		String strmember = JSON.toJSONString(listmember);
		System.out.println(strmember);
		liststr.add(2, strmember);
		return liststr;
	}
	public static void main(String[] args) {
		
	}
}
