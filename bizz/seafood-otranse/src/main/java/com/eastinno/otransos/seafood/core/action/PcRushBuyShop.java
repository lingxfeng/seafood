package com.eastinno.otransos.seafood.core.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRegularService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 秒杀活动
 * @author Wb
 *
 */
@Action
public class PcRushBuyShop extends ShopBaseAction {
	@Inject
	private IRushBuyRegularService regularService;
	@Inject
	private IShopMemberService shopMemberService;
	@Inject
	private IShopProductService shopProductService;
	@Inject
	private IRushBuyRecordService recordService;
	
	/**
	 * 秒杀活动列表页
	 */
	public Page doSecKillList(WebForm form) {
		form.addResult("member", this.getUserInfo());
		form.addResult("hotSaleRegulars", this.getHotSaleRegular("seckill"));
		this.getRegulars(form, "seckill");
		return new Page("/bcd/pcshop/seckill/list.html");
	}
	
	/**
	 * 秒杀活动积分商品详情
	 */
	public Page doSecKillDetail(WebForm form){
		form.addResult("member", this.getUserInfo());
		this.getRegularById(form);
		return new Page("/bcd/pcshop/seckill/detail.html");
	}
	
	/**
	 * 限时抢购列表页
	 */
	public Page doTimeLimitList(WebForm form) {
		form.addResult("member", this.getUserInfo());
		form.addResult("hotSaleRegulars", this.getHotSaleRegular("timelimit"));
		this.getRegulars(form, "timelimit");
		return new Page("/bcd/pcshop/timelimit/list.html");
	}
	
	/**
	 * 限时抢购积分商品详情
	 */
	public Page doTimeLimitDetail(WebForm form){
		form.addResult("member", this.getUserInfo());
		this.getRegularById(form);
		return new Page("/bcd/pcshop/timelimit/detail.html");
	}

	/**
     * 秒杀-请求是否可以进入确认订单页
     * @param form
     * @return
     */
    public Page doRequestSecKillRight(WebForm form) {		
		ShopMember member = this.getUserInfo();
		if(member == null){
			return this.ajax(form, "failure", "/shopMemberCenter.java?cmd=toLogin", "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}		
		Long regularId = Long.parseLong(form.get("regularId").toString());
		RushBuyRegular regular = this.regularService.getRushBuyRegular(regularId);
		String secKillListUrl = "<br /><a style=\"color:red;\" href=\"/pcRushBuyShop.java?cmd=secKillList\">查看其他秒杀活动</a>";
		//活动状态
		if(regular.getState().equals("end") || regular.getState().equals("unshelving")){
			return this.ajax(form, "failure", null, "请求的活动已经结束！"+secKillListUrl);
		}
		if(regular.getState().equals("shelving") || regular.getState().equals("create") || regular.getState().equals("error")){
			return this.ajax(form, "failure", null, "请求的活动还没有开始！"+secKillListUrl);
		}
		//库存数量
		if(regular.getPro().getInventory().compareTo(0) <= 0){
			return this.ajax(form, "failure", null, "该商品已经售罄！"+secKillListUrl);
		}
		//抢购数量
		IPageList list = this.recordService.getAllAvailableSecKillRecordByRegular(regular);
		if(list.getRowCount() > regular.getBuyNum()*2){
			return this.ajax(form, "failure", null, "目前系统中抢购的人数较多，您可能没有机会了！"+secKillListUrl);
		}
		//有秒杀资格添加秒杀资格记录
		if(this.setRecord(member, regular, form) == null){
			return this.ajax(form, "failure", null, "您本次抢购失败！"+secKillListUrl);
		}		
		//有秒杀资格，进入下单页
		return this.ajax(form, "success", this.getActivityProUrl(regular, form), "抢购资格获取成功，进入下单页！");		
	}
    
    /**
     * 秒杀-请求是否可以进入确认订单页
     * @param form
     * @return
     */
    public Page doRequestTimeLimitRight(WebForm form) {		
		ShopMember member = this.getUserInfo();
		if(member == null){
			return this.ajax(form, "failure", "/shopMemberCenter.java?cmd=toLogin", "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}		
		Long regularId = Long.parseLong(form.get("regularId").toString());
		RushBuyRegular regular = this.regularService.getRushBuyRegular(regularId);
		String secKillListUrl = "<br /><a style=\"color:red;\" href=\"/pcRushBuyShop.java?cmd=timeLimitList\">查看其他限时活动</a>";
		//活动状态
		if(regular.getState().equals("end") || regular.getState().equals("unshelving")){
			return this.ajax(form, "failure", null, "请求的活动已经结束！"+secKillListUrl);
		}
		if(regular.getState().equals("shelving") || regular.getState().equals("create") || regular.getState().equals("error")){
			return this.ajax(form, "failure", null, "请求的活动还没有开始！"+secKillListUrl);
		}
		//库存数量
		if(regular.getPro().getInventory().compareTo(0) <= 0){
			return this.ajax(form, "failure", null, "该商品已经售罄！"+secKillListUrl);
		}
		//抢购数量(王彬你看看，你竟然这么写)
		//IPageList list = this.recordService.getAllAvailableSecKillRecordByRegular(regular);
		IPageList list = this.recordService.getAllAvailableTimeLimitRecordByRegular(regular);
		if(list == null){
			return this.ajax(form, "failure", null, "请求的活动还没有开始！"+secKillListUrl);
		}else if(list.getRowCount() > regular.getBuyNum()*2){
			return this.ajax(form, "failure", null, "目前系统中抢购的人数较多，您可能没有机会了！"+secKillListUrl);
		}
		//有秒杀资格添加秒杀资格记录
		if(this.setRecord(member, regular, form) == null){
			return this.ajax(form, "failure", null, "您本次抢购失败！"+secKillListUrl);
		}		
		//有秒杀资格，进入下单页
		return this.ajax(form, "success", this.getActivityProUrl(regular, form), "抢购资格获取成功，进入下单页！");		
	}
    
    /**
     * 创建进入秒杀 和 限时抢购 系统的资格记录
     * @param member
     * @param regular
     * @param form
     * @return
     */
    private RushBuyRecord setRecord(ShopMember member, RushBuyRegular regular, WebForm form){
    	RushBuyRecord record = null;
    	if(regular.getActivityType() == 0){
    		record = this.recordService.getSingleAvailableSecKillRecordByMemberAndRegular(regular, member);
    	}else if(regular.getActivityType() == 1){
    		record = this.recordService.getSingleAvailableTimeLimitRecordByMemberAndRegular(regular, member);
    	}
    	if(record == null){
    		record = new RushBuyRecord();
    		record.setRegular(regular);
        	record.setOutExpire(false);
        	record.setMember(member);
        	record.setIpAddress(ActionContext.getContext().getRequest().getRemoteHost());
        	record.setCreateDate(new Date());
        	if(regular.getActivityType() == 0){
        		record = this.recordService.createSecKillRecord(record);
        	}else if(regular.getActivityType() == 1){
        		record = this.recordService.createTimeLimitRecord(record);
        	}        	
    	}else if(record != null && record.getOrder() != null){	//已经进入过系统并且下单成功
    		record = null;
    	}
    	return record;
    }
    
    /**
     * 服务器端秒杀商品下单页
     * @param regular
     * @param form
     * @return
     */
    public String getActivityProUrl(RushBuyRegular regular, WebForm form){
    	String result = "";
    	result += "/shopTrade.java?cmd=confirmOrder&proIdNums="+form.get("proIdNums");
    	return result;
    }
	
	/**
	 * ajax信息返回页
	 * @param form
	 * @param result
	 * @param returnUrl
	 * @param data
	 * @return
	 */
	private Page ajax(WebForm form, String result, String returnUrl, String data){
		form.addResult("result", StringEscapeUtils.escapeJava(result));
		form.addResult("returnUrl", StringEscapeUtils.escapeJava(returnUrl));
		form.addResult("data", StringEscapeUtils.escapeJava(data));		
		return new Page("bcd/pcshop/seckill/ajax.html");
	}
	
	/**
	 * 错误页
	 * @param form
	 * @param alertMsg
	 * @param returnUrl
	 * @param staticMsg
	 * @return
	 */
	private Page error(WebForm form, String alertMsg, String returnUrl, String staticMsg){
		form.addResult("alertMsg", alertMsg);
		form.addResult("returnUrl", returnUrl);
		form.addResult("staticMsg", staticMsg);
		return new Page("/bcd/pcshop/seckill/error.html");
	}
	
	/**
	 * 获取登录用户信息
	 * @return
	 */
	private ShopMember getUserInfo(){
		ShopMember member = this.getGoShopService().getShopMember();
		if(member == null){
			this.addError("userInfo", "获取用户信息失败！");
		}
		return member;
	}
	
	/**
	 * 获取热卖秒杀活动
	 * @return
	 */
	private List<RushBuyRegular> getHotSaleRegular(String type){
		List<RushBuyRegular> regularList;
		QueryObject qo = new QueryObject();
		qo.setPageSize(13);
		qo.setOrderBy("pro.saleNum DESC, createDate DESC");
		IPageList pageList = null;
		if(type.equals("timelimit")){
			pageList = this.regularService.getAllTimeLimitRegularByQO(qo);
		}else if(type.equals("seckill")){
			pageList = this.regularService.getAllSecKillRegularByQO(qo);
		}
		regularList = pageList.getResult();
		return regularList;
	}
	
	private void getRegulars(WebForm form, String type){
		//表单验证
		String startStr = CommUtil.null2String(form.get("startPoint"));
		String endStr = CommUtil.null2String(form.get("endPoint"));
		String orderType = CommUtil.null2String(form.get("orderType"));
		String currentPageStr = CommUtil.null2String(form.get("currentPage"));
		Double startPoint = 0D;
		Double endPoint = 0D;
		int currentPage;
		if(startStr.equals("")){
			startPoint = null;
		}else{
			startPoint = Double.parseDouble(startStr);
		}		
		if(endStr.equals("")){
			endPoint = null;
		}else{
			endPoint = Double.parseDouble(endStr);
		}
		if(currentPageStr.equals("")){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(currentPageStr);
		}
		orderType = orderType.toUpperCase();
		if(!orderType.equals("DESC") && !orderType.equals("ASC")){
			orderType = null;
		}
		
		//构造查询
		QueryObject qo = new QueryObject();		
		if(startPoint != null){
			qo.addQuery("obj.activityPrice", startPoint, ">=");
		}
		if(endPoint != null){
			qo.addQuery("obj.activityPrice", endPoint, "<=");
		}
		String orderBy = "";
		if(orderType != null){
			orderBy = "activityPrice "+orderType+", createDate DESC";
		}else{
			orderBy = "createDate DESC";
		}
		
		qo.addQuery("obj.shelvingDate", new Date(), "<=");
		qo.addQuery("obj.unshelvingDate", new Date(), ">=");
		
		qo.setOrderBy(orderBy);
		qo.setPageSize(16);
		qo.setCurrentPage(currentPage);
		IPageList pageList = null;
		if(type.equals("timelimit")){
			pageList = this.regularService.getAllTimeLimitRegularByQO(qo);
		}else if(type.equals("seckill")){
			pageList = this.regularService.getAllSecKillRegularByQO(qo);
		}
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);		
	}
		
	private void getRegularById(WebForm form){
		String idStr = CommUtil.null2String(form.get("id"));
		RushBuyRegular regular = null;
		if(!idStr.equals("")){
			Long id = Long.parseLong(idStr);
			regular = this.regularService.getRushBuyRegular(id);
		}
		form.addResult("regular", regular);
	}

	public IRushBuyRegularService getRegularService() {
		return regularService;
	}

	public void setRegularService(IRushBuyRegularService regularService) {
		this.regularService = regularService;
	}

	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}

	public IShopProductService getShopProductService() {
		return shopProductService;
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}

	public IRushBuyRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(IRushBuyRecordService recordService) {
		this.recordService = recordService;
	}
	
}
