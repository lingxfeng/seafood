package com.eastinno.otransos.seafood.statistics.action;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.trade.dao.IShopOrderInfoDAO;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.dao.IShopMemberDAO;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.util.DateUtil;
import com.eastinno.otransos.seafood.util.formatUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 导流统计
 * @author dll
 */
@Action
public class StreamGuidanceAction extends AbstractPageCmdAction {
	@Autowired
	IShopOrderInfoDAO shopOrderInfoDAO;
	@Autowired
	IShopMemberDAO shopMemberDAO;
	
	
	public IShopOrderInfoDAO getShopOrderInfoDAO() {
		return shopOrderInfoDAO;
	}


	public void setShopOrderInfoDAO(IShopOrderInfoDAO shopOrderInfoDAO) {
		this.shopOrderInfoDAO = shopOrderInfoDAO;
	}


	public IShopMemberDAO getShopMemberDAO() {
		return shopMemberDAO;
	}


	public void setShopMemberDAO(IShopMemberDAO shopMemberDAO) {
		this.shopMemberDAO = shopMemberDAO;
	}


	/**
     * 跳转展示页面
     * @param form
     * @return
	 * @throws ParseException 
     */
	public Page doGoShow(WebForm form) throws ParseException{
		Calendar calendar = Calendar.getInstance();
		Date sdate;
	    Date edate;
	    if(form.get("startDate") != null && !"".equals(form.get("startDate"))){
	    	sdate = DateUtil.getStrDate(form.get("startDate").toString(),"yyyy-MM-dd HH:mm:ss");
	    }else{
	    	calendar.set(Calendar.DAY_OF_MONTH, 1);  
			calendar.set(Calendar.HOUR_OF_DAY, 0);  
			calendar.set(Calendar.MINUTE, 0);  
			calendar.set(Calendar.SECOND,0);  
			calendar.set(Calendar.MILLISECOND, 0);  
			sdate = calendar.getTime();  
	    }
	    if(form.get("endDate") != null && !"".equals(form.get("endDate"))){
	    	edate = DateUtil.getStrDate(form.get("endDate").toString(),"yyyy-MM-dd HH:mm:ss");
	    }else{
	    	calendar.add(Calendar.MONTH, 1);  
    		calendar.add(Calendar.MILLISECOND, -1);  
    		edate = calendar.getTime();
	    }
	    form.addResult("startDate",DateUtil.getDateStr(sdate,"yyyy-MM-dd HH:mm:ss"));
	    form.addResult("endDate",DateUtil.getDateStr(edate,"yyyy-MM-dd HH:mm:ss"));
		/**
		 * 订单数量
		 */
		QueryObject qo = new QueryObject();
		qo.setPageSize(-1);
		qo.addQuery("obj.tradeDate",sdate,">=");
		qo.addQuery("obj.tradeDate",edate,"<=");
		qo.addQuery("obj.status in (1,2,3,4,6)");
		qo.addQuery("obj.user.dePath like '@1489317752733%'");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		List<ShopOrderInfo> list = this.shopOrderInfoDAO.findBy(qo).getResult();
		form.addResult("orderNum", list==null?0:list.size());
		/**
		 * 订单详情
		 */
		QueryObject qo1 = new QueryObject();
		Integer currentPage = (Integer) form.get("currentPage");
		if(currentPage == null){
			currentPage=0;
		}
		qo1.setCurrentPage(currentPage);
		qo1.addQuery("obj.tradeDate",sdate,">=");
		qo1.addQuery("obj.tradeDate",edate,"<=");
		qo1.addQuery("obj.status in (1,2,3,4,6)");
		qo1.addQuery("obj.user.dePath like '@1489317752733%'");
		qo1.setOrderBy("ceateDate");
		qo1.setOrderType("desc");
		IPageList pl = this.shopOrderInfoDAO.findBy(qo1);
	    CommUtil.saveIPageList2WebForm(pl, form);
	    form.addResult("pl", pl);
	    form.addResult("fu", formatUtil.fu);
		/**
		 * 用户数量
		 */
		QueryObject qo2 = new QueryObject();
		qo2.setPageSize(-1);
		qo2.addQuery("obj.registerTime",sdate,">=");
		qo2.addQuery("obj.registerTime",edate,"<=");
		qo2.addQuery("obj.dePath like '@1489317752733%'");
//		qo2.setOrderBy("ceateDate");
//		qo2.setOrderType("desc");
		List<ShopMember> listMember = this.shopMemberDAO.findBy(qo2).getResult();
		form.addResult("memberNum", listMember==null?0:listMember.size());
		/**
		 * 订单总金额
		 */
		
		Double totalMoney=0.0;
		if(list != null && list.size() != 0){
			for(ShopOrderInfo orderInfo:list){
				totalMoney= totalMoney+orderInfo.getProduct_price();
			}
		}
		form.addResult("countMoney",totalMoney);
		
    	return new Page("shopmanage/stastic/streamguidance.html");
    }
	
   /* public static void main(String[] args){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MONTH, 1);  
		calendar.add(Calendar.MILLISECOND, -1);  
		System.out.println(calendar.getTime());
    }*/
}