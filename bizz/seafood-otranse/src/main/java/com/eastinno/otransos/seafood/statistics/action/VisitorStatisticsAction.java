package com.eastinno.otransos.seafood.statistics.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.statistics.domain.MonthTradeDetail;
import com.eastinno.otransos.seafood.statistics.domain.ProdTradeDetail;
import com.eastinno.otransos.seafood.statistics.domain.TradeDetail;
import com.eastinno.otransos.seafood.statistics.domain.VisitorStatistics;
import com.eastinno.otransos.seafood.statistics.service.IVisitorStatisticsService;
import com.eastinno.otransos.seafood.util.DateUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * VisitorStatisticsAction
 * 
 * @author
 */
@Action
public class VisitorStatisticsAction extends AbstractPageCmdAction {
	@Inject
	private IVisitorStatisticsService service;

	/**
	 * 跳转展示页面
	 * 
	 * @param form
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Page doGoShow(WebForm form) {
		// 总计统计
		List listcount = this.service.getCount();
		form.addResult("ipall", listcount.get(0));
		form.addResult("pvall", listcount.get(1));
		form.addResult("memberall", listcount.get(2));
		// 当日数据
		List listcountcurrentday = this.service.getCountDay();
		form.addResult("ipcurrentday", listcountcurrentday.get(0));
		form.addResult("pvcurrentday", listcountcurrentday.get(1));
		form.addResult("membercurrentday", listcountcurrentday.get(2));
		// 当月统计
		List listcountcurrentmonth = this.service.getCountMonth();
		form.addResult("ipcurrentmonth", listcountcurrentmonth.get(0));
		form.addResult("pvcurrentmonth", listcountcurrentmonth.get(1));
		form.addResult("membercurrentmonth", listcountcurrentmonth.get(2));
		// 当年统计
		List listcountcurrentyear = this.service.getCountYear();
		form.addResult("ipcurrentyear", listcountcurrentyear.get(0));
		form.addResult("pvcurrentyear", listcountcurrentyear.get(1));
		form.addResult("membercurrentyear", listcountcurrentyear.get(2));

		// 每日数据
		List listipday = this.service.getAllDayData();
		form.addResult("keystrday", listipday.get(0));
		form.addResult("valstrdayip", listipday.get(1));
		form.addResult("valstrdaypv", listipday.get(2));
		form.addResult("valstrdaymember", listipday.get(3));
		// 每月数据
		List listipmonth = this.service.getAllMonthData();
		form.addResult("monthip", listipmonth.get(0));
		form.addResult("monthpv", listipmonth.get(1));
		form.addResult("monthmember", listipmonth.get(2));
		// 每年数据
		List listipyear = this.service.getAllYearData();
		form.addResult("yearip", listipyear.get(0));
		form.addResult("yearpv", listipyear.get(1));
		form.addResult("yearmember", listipyear.get(2));
		return new Page("shopmanage/stastic/testEcharts.html");
	}

	/**
	 * 跳转展示页面
	 * 
	 * @param form
	 * @return
	 */
	public Page doGoMonth(WebForm form) {

		List<MonthTradeDetail> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		Date sdate = null;
		Date edate = null;
		if (form.get("startDate") != null && !"".equals(form.get("startDate"))) {
			try {
				sdate = DateUtil.getStrDate(form.get("startDate").toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			sdate = calendar.getTime();
		}
		if (form.get("endDate") != null && !"".equals(form.get("endDate"))) {
			try {
				edate = DateUtil.getStrDate(form.get("endDate").toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			edate = calendar.getTime();
		}
		form.addResult("startDate", DateUtil.getDateStr(sdate, "yyyy-MM-dd HH:mm:ss"));
		form.addResult("endDate", DateUtil.getDateStr(edate, "yyyy-MM-dd HH:mm:ss"));

		try {
			Properties prop = new Properties();
			prop.put("user", "sea_root");
			prop.put("password", "Newpass2017sea&*()");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection("jdbc:mysql://rm-bp1akj2449hac3rsjo.mysql.rds.aliyuncs.com:3306/seafood", prop);
			Statement stmt = con.createStatement();
			String sql = "(SELECT '2018-01',sum(summoney)" + "FROM	(SELECT	pro_id,sum(gross_price) AS summoney	"
					+ "FROM disco_shop_shoporderdetail WHERE orderInfo_id IN "
					+ "(SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1 AND `status` NOT IN (-1) "
					+ "AND ceateDate >= '2018-01-01 00:00:00' AND ceateDate <= '2018-02-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME) "
					+ "union all " + "(SELECT '2018-02',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-02-01 00:00:00' AND ceateDate <= '2018-03-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-03',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-03-01 00:00:00' AND ceateDate <= '2018-04-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-04',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-04-01 00:00:00' AND ceateDate <= '2018-05-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-05',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-05-01 00:00:00' AND ceateDate <= '2018-06-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-06',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-06-01 00:00:00' AND ceateDate <= '2018-07-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-07',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-07-01 00:00:00' AND ceateDate <= '2018-08-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-08',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-08-01 00:00:00' AND ceateDate <= '2018-09-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-09',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-09-01 00:00:00' AND ceateDate <= '2018-10-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-10',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-10-01 00:00:00' AND ceateDate <= '2018-11-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-11',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-11-01 00:00:00' AND ceateDate <= '2018-12-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '2018-12',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-12-01 00:00:00' AND ceateDate <= '2019-01-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)" + "union all "
					+ "(SELECT '合计',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-01-01 00:00:00' AND ceateDate <= '2019-01-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)";
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				MonthTradeDetail tt = new MonthTradeDetail();
				tt.setMonth(res.getString(1));
				tt.setTotal(res.getString(2));
				list.add(tt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		form.addResult("final", list);
		return new Page("shopmanage/stastic/MonthEcharts.html");
	}

	/**
	 * 跳转商品统计
	 * 
	 * @param form
	 * @return
	 */
	public Page doGoProTrade(WebForm form) {
		return new Page("shopmanage/stastic/productdetail.html");
	}

	/**
	 * 查询商品统计信息
	 * 
	 * @param form
	 * @return
	 */
	public Page doGetProTrade(WebForm form) {
		List<ProdTradeDetail> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		Date sdate = null;
		Date edate = null;
		if (form.get("startDate") != null && !"".equals(form.get("startDate"))) {
			try {
				sdate = DateUtil.getStrDate(form.get("startDate").toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			sdate = calendar.getTime();
		}
		if (form.get("endDate") != null && !"".equals(form.get("endDate"))) {
			try {
				edate = DateUtil.getStrDate(form.get("endDate").toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			edate = calendar.getTime();
		}
		form.addResult("startDate", DateUtil.getDateStr(sdate, "yyyy-MM-dd HH:mm:ss"));
		form.addResult("endDate", DateUtil.getDateStr(edate, "yyyy-MM-dd HH:mm:ss"));

		try {
			Properties prop = new Properties();
			prop.put("user", "sea_root");
			prop.put("password", "Newpass2017sea&*()");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection("jdbc:mysql://rm-bp1akj2449hac3rsjo.mysql.rds.aliyuncs.com:3306/seafood", prop);
			Statement stmt = con.createStatement();
			String sql = "SELECT a.pro_id,b.`name`,a.num,a.price from "
					+ "(SELECT	pro_id,sum(num) as num,sum(gross_price) as price FROM	disco_shop_shoporderdetail "
					+ "WHERE 	orderInfo_id IN (SELECT	id	FROM disco_shop_shoporderinfo	WHERE	`status` not in(-1) "
					+ "and ceateDate < '" + DateUtil.getDateStr(edate, "yyyy-MM-dd HH:mm:ss") + "'	and ceateDate >='"
					+ DateUtil.getDateStr(sdate, "yyyy-MM-dd HH:mm:ss")
					+ "')GROUP BY pro_id) a LEFT JOIN disco_shop_product b on a.pro_id = b.id ORDER BY a.price desc";
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				ProdTradeDetail tt = new ProdTradeDetail();
				tt.setName(res.getString(2));
				tt.setId(res.getString(1));
				tt.setNum(res.getString(3));
				tt.setTotal(res.getString(4));
				list.add(tt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		form.addResult("final", list);

		return new Page("shopmanage/stastic/productdetail.html");
	}

	/**
	 * 跳转展示页面
	 * 
	 * @param form
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Page doGoDetail(WebForm form) {
		List<TradeDetail> list = new ArrayList<>();
		try {
			Properties prop = new Properties();
			prop.put("user", "sea_root");
			prop.put("password", "Newpass2017sea&*()");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection("jdbc:mysql://rm-bp1akj2449hac3rsjo.mysql.rds.aliyuncs.com:3306/seafood", prop);
			Statement stmt = con.createStatement();
			String sql = "(SELECT c.`name`, c.id, c.parent_id, b.id,b.`name`,summoney "
					+ "FROM	(SELECT	pro_id,sum(gross_price) AS summoney	"
					+ "FROM disco_shop_shoporderdetail WHERE orderInfo_id IN "
					+ "(SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1 AND `status` NOT IN (-1) "
					+ "AND ceateDate >= '2018-01-01 00:00:00' AND ceateDate <= '2019-01-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME) "
					+ "union all " + "(SELECT '合计', '', '', '','',sum(summoney) FROM	"
					+ "(SELECT	pro_id,sum(gross_price) AS summoney	FROM disco_shop_shoporderdetail "
					+ "WHERE orderInfo_id IN (SELECT	id FROM disco_shop_shoporderinfo WHERE 	1 = 1"
					+ " AND `status` NOT IN (-1) AND ceateDate >= '2018-01-01 00:00:00' AND ceateDate <= '2019-01-01 00:00:00')"
					+ "GROUP BY	pro_id) a LEFT JOIN disco_shop_product b ON a.pro_id = b.id "
					+ "LEFT JOIN disco_shop_producttype c ON b.productType_id = c.id ORDER BY	c. NAME)";
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				TradeDetail tt = new TradeDetail();
				tt.setName(res.getString(1));
				tt.setTid(res.getString(2));
				tt.setPtid(res.getString(3));
				tt.setPid(res.getString(4));
				tt.setPname(res.getString(5));
				tt.setTotal(res.getString(6));
				list.add(tt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		form.addResult("final", list);
		return new Page("shopmanage/stastic/trade.html");
	}

	/**
	 * 列表页面
	 * 
	 * @param form
	 */
	public Page doList(WebForm form) {
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		IPageList pageList = this.service.getVisitorStatisticsBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	/**
	 * 保存数据
	 * 
	 * @param form
	 */
	public Page doSave(WebForm form) {
		VisitorStatistics entry = (VisitorStatistics) form.toPo(VisitorStatistics.class);
		form.toPo(entry);
		if (!hasErrors()) {
			Long id = this.service.addVisitorStatistics(entry);
			if (id != null) {
				form.addResult("msg", "添加成功");
			}
		}
		Page page = pageForExtForm(form);
		page.setContentType("html");
		return page;
	}

	/**
	 * 修改数据
	 * 
	 * @param form
	 */
	public Page doUpdate(WebForm form) {
		Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
		VisitorStatistics entry = this.service.getVisitorStatistics(id);
		form.toPo(entry);
		if (!hasErrors()) {
			boolean ret = service.updateVisitorStatistics(id, entry);
			if (ret) {
				form.addResult("msg", "修改成功");
			}
		}
		Page page = pageForExtForm(form);
		page.setContentType("html");
		return page;
	}

	/**
	 * 删除数据
	 * 
	 * @param form
	 */
	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		this.service.delVisitorStatistics(id);
		Page page = pageForExtForm(form);
		page.setContentType("html");
		return page;
	}

	public void setService(IVisitorStatisticsService service) {
		this.service = service;
	}
}