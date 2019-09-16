package com.eastinno.otransos.shop.core.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.eastinno.otransos.cms.domain.LinkImgGroup;
import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.service.ILinkImgGroupService;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRecord;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRegular;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRecordService;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRegularService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IShopAddressService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 积分商城
 * @author Wb
 *
 */
@Action
public class PcIntegralShop extends ShopBaseAction {
	@Inject
	private IIntegralBuyRegularService regularService;
	@Inject
	private ILinkImgGroupService groupService;
	@Inject
	private ILinkImgTypeService groupTypeService;
	@Inject
	private IShopSpecService shopSpecService;
	@Inject
	private IShopAddressService shopAddressService;
	@Inject
	private IShopMemberService shopMemberService;
	@Inject
	private IShopOrderInfoService shopOrderInfoService;
	@Inject
	private IShopOrderdetailService shopOrderdetailService;
	@Inject
	private IShopProductService shopProductService;
	@Inject
	private IIntegralBuyRecordService recordService;
	
	/**
	 * 积分商城首页
	 */
	@Override
	public Page doInit(WebForm form, Module module){		
		form.addResult("member", this.getUserInfo());
		form.addResult("hotSaleRegulars", this.getHotSaleRegular());
		form.addResult("circleImgs", this.getCircleImg());
		form.addResult("recmmendRegulars", this.getRecmmendRegulars());
		return new Page("/bcd/pcshop/integral/index.html");
	}
	
	/**
	 * 积分商城列表页
	 */
	public Page doList(WebForm form) {
		form.addResult("member", this.getUserInfo());
		form.addResult("hotSaleRegulars", this.getHotSaleRegular());
		this.getRegulars(form);
		return new Page("/bcd/pcshop/integral/list.html");
	}
	
	/**
	 * 积分商城积分商品详情
	 */
	public Page doDetail(WebForm form){
		form.addResult("member", this.getUserInfo());
		this.getRegularById(form);
		return new Page("/bcd/pcshop/integral/detail.html");
	}
	
	/**
	 * 确认订单
	 * @param form
	 * @return
	 */
	public Page doConfirmOrder(WebForm form){
		//下面是复制的代码
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(member == null){
			return new Page("/userCenter/login.html");
		}
		String proIdNumsStr = CommUtil.null2String(form.get("proIdNums"));		
		List<Map<String,String>> pros = new ArrayList<Map<String,String>>();		
		if(!"".equals(proIdNumsStr)){
			String[] proIdNums = proIdNumsStr.split(",");
			Integer allAmt = 1;
			for(String proIdNumStr:proIdNums){
				String[] idNums=proIdNumStr.split("_");
				int num = 1;
				//商品数量
				if(idNums.length>1){
					num = Integer.parseInt(idNums[1]);
				}
				
				String id = idNums[0];
				ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
				Map<String,String> promap = new HashMap<String,String>();								
				promap.put("name", pro.getName());				
				promap.put("num", num+"");
				promap.put("amtAll", allAmt+"");
				promap.put("imgPath", pro.getImgPaths().split("_")[0]);
				
				//======begin======
				//这是符合积分订单确认页的修正数据
				String integralIdStr = CommUtil.null2String(form.get("integralId"));		
				Long integralId = Long.parseLong(integralIdStr);
				if(integralIdStr.equals("")){
					form.addResult("msg", "当前提交的信息有误，请重新选购积分商品！");	
					return this.go("list");
				}
				integralId = Long.parseLong(integralIdStr);
				IntegralBuyRegular integral = this.regularService.getIntegralBuyRegular(integralId);
				promap.put("id", pro.getId()+"");
				promap.put("store_price", integral.getIntegralPrice()+"");
				promap.put("allStore_price", integral.getIntegralPrice()+"");
				promap.put("tydAmt", integral.getIntegralPrice()+"");
				promap.put("allTydAmt", integral.getIntegralPrice()+"");
				promap.put("amt", integral.getIntegralPrice()+"");
				allAmt = (int) (integral.getIntegralPrice()*num);
				promap.put("amtAll", allAmt+"");
				form.addResult("integralRegular", integral);
				//======end=========
				
				pros.add(promap);
			}
			form.addResult("allAmt", allAmt); //总价格
		}
		form.addResult("proList", pros);
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user",user,"=");
		List<?> addresses  = this.shopAddressService.getShopAddressBy(qo).getResult();
		form.addResult("addLists", addresses);		
				
		return new Page("/bcd/pcshop/integral/confirmOrder.html");
	}
	
	/**
	 * 创建积分订单
	 * @param form
	 * @return
	 */
	public Page doCreateOrder(WebForm form){
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		user=this.shopMemberService.getShopMember(user.getId());
		if(user==null){			
			return this.ajax(form, "failure", "/userCenter/login.html", "当前登录过期，请重新登录！");
		}
		
		String regularStr = CommUtil.null2String(form.get("regularId"));
		if(regularStr.equals("")){
			return this.ajax(form, "failure", "/pcIntegralShop.java?cmd=list", "未找到对应的积分活动，请重新选购！");
		}
		IntegralBuyRegular regular = this.regularService.getIntegralBuyRegular(Long.parseLong(regularStr));
		
		//检查库存是否满足购买需求
		this.checkInventoryEnough(form, "direct");
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){			
			return this.ajax(form, "failure", null, errorStr);
		}
		
		//检查可用积分是否足够		
		if(user.getAvailableIntegral() < regular.getIntegralPrice()){
			return this.ajax(form, "failure", "pcIntegralShop.java?cmd=list", "您的积分不足以购买该商品，不能下单！<br/><a style=\"color:red;\" href=\"pcIntegralShop.java?cmd=list"+"\">选购其他积分商品</a>");
		}
		
		/**
		 * 生成订单
		 */		
		order.setUser(user);
		order.setCode(new Date().getTime()+"");
		if(order.getUser().getDisType()==0){
			order.setDistributor(order.getUser().getDistributor());
			if(order.getUser().getDistributor()!=null){
				order.setTopDistributor(order.getUser().getDistributor().getTopDistributor());
			}
		}else if(order.getUser().getDisType()==1){
			order.setDistributor(order.getUser().getMyDistributor());
			order.setTopDistributor(order.getUser().getMyDistributor().getTopDistributor());
		}else if(order.getUser().getDisType()==2){
			order.setDistributor(order.getUser().getMyDistributor().getTopDistributor());
			order.setDistributor(order.getUser().getMyDistributor().getTopDistributor());
		}
		order.setType("integral");
		this.shopOrderInfoService.addShopOrderInfo(order);
		
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */
		String proIdNumsStr = CommUtil.null2String(form.get("proIdNums"));
		List<Map<String,String>> pros = new ArrayList<Map<String,String>>();
		if(!"".equals(proIdNumsStr)){
			String[] proIdNums = proIdNumsStr.split(",");
			Double allAmt = 0.0;
			for(String proIdNumStr:proIdNums){
				String[] idNums=proIdNumStr.split("_");
				//购买数量
				int num = 1;
				if(idNums.length>1){
					num = Integer.parseInt(idNums[1]);
				}
				//购买规格
				Long shopspecId = null;
				ShopSpec spec = null;
				if(idNums.length > 2){
					shopspecId = Long.parseLong(idNums[2]);
					spec = this.shopSpecService.getShopSpec(shopspecId);
				}				
				
				String id = idNums[0];
				ShopProduct pro = this.getGoShopService().getProduct(Long.parseLong(id));
				ShopOrderdetail orderDetail = new ShopOrderdetail();
				orderDetail.setPro(pro);
				orderDetail.setNum(num);
				orderDetail.setUser(user);
				orderDetail.setCode(new Date().getTime()+"");
				orderDetail.setUnit_price(pro.getAmt());
				orderDetail.setGross_price(pro.getAmt()*num);
				orderDetail.setOrderInfo(order);
				orderDetail.setShopSpec(spec);
				this.shopOrderdetailService.addShopOrderdetail(orderDetail);
				pro.setSaleNum(pro.getSaleNum()+num);
				pro.setInventory(pro.getInventory()-num);
				this.shopProductService.updateShopProduct(pro.getId(), pro);
				
				order.setGross_price(orderDetail.getGross_price());
				order.setFreight(0D);
				this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
			}
			
		}
		
		//生成积分购买记录
		if(this.setRecord(user, order, regular, form) == null){
			String integralListUrl = "<br /><a style=\"color:red;\" href=\"/pcIntegralShop.java?cmd=list\">查看其他积分活动</a>";
			return this.ajax(form, "failure", "pcIntegralShop.java?cmd=list", "您本次购买失败！"+integralListUrl);
		}
		ShopMember member = order.getUser();
		Follower f=member.getFollower();
		if(f!=null){
			Account a = f.getAccount();
			WeixinBaseUtils.sendMsgToFollower(a, f, "【百春达电子商务有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+order.getCeateDate()+"所下订单:"+order.getCode()+"已成功，我们将及时为您发货，如需服务，请致电400-601-2721；");
		}
		return this.ajax(form, "success", "pcIntegralShop.java?cmd=list", "恭喜您积分兑换成功！");
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
		return new Page("bcd/pcshop/integral/ajax.html");
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
		form.addResult("alertMsg", StringEscapeUtils.escapeJava(alertMsg));
		form.addResult("returnUrl", StringEscapeUtils.escapeJava(returnUrl));
		form.addResult("staticMsg", StringEscapeUtils.escapeJava(staticMsg));
		return new Page("/bcd/pcshop/integral/error.html");
	}
	
	/**
	 * 检查提交的购买需求在库存方面是否满足
	 * @param form
	 * @param orderFrom
	 */
	private void checkInventoryEnough(WebForm form, String orderFrom){
		if(orderFrom.equals("direct")){
			String proIdNums = CommUtil.null2String(form.get("proIdNums"));//商品ID,商品数量
			String[] data = proIdNums.split("_");
			if(data[0].equals("")){
				form.addResult("inventoryError", "该订单没有指定商品，请选购商品重新下单！<br/><a style=\"color:red;\" href=\"pcIntegralShop.java?cmd=init\">去积分首页选购商品</a>");
				return;
			}
			if(data[1].equals("")){
				form.addResult("inventoryError", "该订单商品选购数量为0，不能下单！<br/><a style=\"color:red;\" href=\"pcIntegralShop.java?cmd=list\">重新选购商品</a>");
				return;
			}
			int num = Integer.parseInt(data[1]);
			ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(data[0]));
			if(0 > (pro.getInventory()-num) ){
				form.addResult("inventoryError", "该订单商品库存【"+pro.getInventory()+"】低于兑换量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\"pcIntegralShop.java?cmd=list\">重新选购商品</a>");
			}
		}
	}
	
	/**
     * 创建 积分购物 系统的资格记录
     * @param member
     * @param regular
     * @param form
     * @return
     */
    private IntegralBuyRecord setRecord(ShopMember member, ShopOrderInfo orderInfo,IntegralBuyRegular regular, WebForm form){
    	IntegralBuyRecord record = null;
		record = new IntegralBuyRecord();
		record.setRegular(regular);
    	record.setMember(member);
    	record.setOrder(orderInfo);
    	record.setIpAddress(ActionContext.getContext().getRequest().getRemoteHost());
    	record.setCreateDate(new Date());
    	this.recordService.addIntegralBuyRecord(record);
    	
    	//添加积分变更记录,变更用户积分
    	this.shopMemberService.delIntegralByShopping(member, regular.getIntegralPrice());
    	
    	//更改订单状态
    	orderInfo.setType("integral");
		orderInfo.setTradeDate(new Date());
		orderInfo.setStatus(1);
		orderInfo.setPayType("积分支付");
    	this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
		
    	return record;
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
	 * 获取热卖积分活动
	 * @return
	 */
	private List<IntegralBuyRegular> getHotSaleRegular(){
		List<IntegralBuyRegular> regularList;
		QueryObject qo = new QueryObject();
		qo.setPageSize(13);
		qo.setOrderBy("pro.saleNum DESC, weight DESC");
		
		//积分活动必须在上架时间和下架时间之间
		qo.addQuery("obj.unshelvingDate", new Date(), ">=");
		qo.addQuery("obj.shelvingDate", new Date(), "<=");
		
		IPageList pageList = this.regularService.getIntegralBuyRegularBy(qo);
		regularList = pageList.getResult();
		return regularList;
	}
	
	/**
	 * 轮播图
	 * @return
	 */
	private List<LinkImgGroup> getCircleImg(){
		List<LinkImgGroup> imgList;
		LinkImgType groupType = this.groupTypeService.getLinkImgTypeByCode("integralCircle");
		if(groupType != null){
			imgList = this.groupService.getLinkImgGroupByType(groupType);
		}else{
			imgList = new ArrayList();
		}		
		return imgList;
	}
	
	/**
	 * 获取推荐积分活动列表
	 * @return
	 */
	private List<IntegralBuyRegular> getRecmmendRegulars(){
		List<IntegralBuyRegular> regularList;
		QueryObject qo = new QueryObject();
		qo.setPageSize(8);
		qo.addQuery("obj.isRecmmend", true, "=");
		qo.setOrderBy("weight DESC, createDate DESC");
		
		//积分活动必须在上架时间和下架时间之间
		qo.addQuery("obj.unshelvingDate", new Date(), ">=");
		qo.addQuery("obj.shelvingDate", new Date(), "<=");
		
		regularList = this.regularService.getIntegralBuyRegularBy(qo).getResult();
		return regularList;
	}
	
	private void getRegulars(WebForm form){
		//表单验证
		String startStr = CommUtil.null2String(form.get("startPoint"));
		String endStr = CommUtil.null2String(form.get("endPoint"));
		String orderType = CommUtil.null2String(form.get("orderType"));
		String currentPageStr = CommUtil.null2String(form.get("currentPage"));
		Long startPoint = 0L;
		Long endPoint = 0L;
		int currentPage;
		if(startStr.equals("")){
			startPoint = null;
		}else{
			startPoint = Long.parseLong(startStr);
		}		
		if(endStr.equals("")){
			endPoint = null;
		}else{
			endPoint = Long.parseLong(endStr);
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
			qo.addQuery("obj.integralPrice", startPoint, ">=");
		}
		if(endPoint != null){
			qo.addQuery("obj.integralPrice", endPoint, "<=");
		}
		String orderBy = "";
		if(orderType != null){
			orderBy = "integralPrice "+orderType+", weight DESC";
		}else{
			orderBy = "weight DESC";
		}
		
		//积分活动必须在上架时间和下架时间之间
		qo.addQuery("obj.unshelvingDate", new Date(), ">=");
		qo.addQuery("obj.shelvingDate", new Date(), "<=");
		
		qo.setOrderBy(orderBy);
		qo.setPageSize(16);
		qo.setCurrentPage(currentPage);
		IPageList pageList = this.regularService.getIntegralBuyRegularBy(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);		
	}
	
	private void getRegularById(WebForm form){
		String idStr = CommUtil.null2String(form.get("id"));
		IntegralBuyRegular regular = null;
		if(!idStr.equals("")){
			Long id = Long.parseLong(idStr);
			regular = this.regularService.getIntegralBuyRegular(id);
		}
		form.addResult("regular", regular);
	}
	
	public IIntegralBuyRegularService getRegularService() {
		return regularService;
	}

	public void setRegularService(IIntegralBuyRegularService regularService) {
		this.regularService = regularService;
	}

	public ILinkImgGroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(ILinkImgGroupService groupService) {
		this.groupService = groupService;
	}

	public ILinkImgTypeService getGroupTypeService() {
		return groupTypeService;
	}

	public void setGroupTypeService(ILinkImgTypeService groupTypeService) {
		this.groupTypeService = groupTypeService;
	}
	public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}

	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}

	public IShopAddressService getShopAddressService() {
		return shopAddressService;
	}

	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}
	
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public IShopOrderdetailService getShopOrderdetailService() {
		return shopOrderdetailService;
	}

	public void setShopOrderdetailService(IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}

	public IShopProductService getShopProductService() {
		return shopProductService;
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}

	public IIntegralBuyRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(IIntegralBuyRecordService recordService) {
		this.recordService = recordService;
	}
}
