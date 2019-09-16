package com.eastinno.otransos.shop.promotions.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRecord;
import com.eastinno.otransos.shop.promotions.domain.IntegralBuyRegular;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRecordService;
import com.eastinno.otransos.shop.promotions.service.IIntegralBuyRegularService;
import com.eastinno.otransos.shop.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IDeliveryService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.shop.usercenter.domain.ShopAddress;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.domain.ShoppingCart;
import com.eastinno.otransos.shop.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopAddressService;
import com.eastinno.otransos.shop.usercenter.service.IShoppingCartService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.shop.util.ShopUtil;
import com.eastinno.otransos.shop.util.formatUtil;
import com.eastinno.otransos.shop.util.shopMsgUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 
 * @author  wb
 */
@Action
public class WxIntegralBuyAction extends WxShopBaseAction{
	@Inject
	private IIntegralBuyRegularService regularService;
	@Inject
	private IShopProductService productService;
	@Inject
	private ShopUtil shopUtil;
	@Inject 
	private IIntegralBuyRecordService recordService;	
	@Inject
	private IShopAddressService shopAddressService;
	@Inject
	private IShopDistributorService shopDistributorService;
	@Inject
	private IShopOrderInfoService shopOrderInfoService;
	@Inject
	private IDeliveryRuleService deliveryService;
	@Inject
	private IShoppingCartService shoppingCartService;
	@Inject
	private IShopOrderdetailService shopOrderdetailService;
	@Inject
	private IIntegralHistoryService integralHistoryService;
	@Inject
	private IIntegralRechargeRecordService rechargeRecordService;
	
	public IIntegralRechargeRecordService getRechargeRecordService() {
		return rechargeRecordService;
	}

	public void setRechargeRecordService(IIntegralRechargeRecordService rechargeRecordService) {
		this.rechargeRecordService = rechargeRecordService;
	}

	public IIntegralHistoryService getIntegralHistoryService() {
		return integralHistoryService;
	}

	public void setIntegralHistoryService(IIntegralHistoryService integralHistoryService) {
		this.integralHistoryService = integralHistoryService;
	}

	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}

	public void setShopDistributorService(IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public IDeliveryRuleService getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(IDeliveryRuleService deliveryService) {
		this.deliveryService = deliveryService;
	}

	public IShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	public void setShoppingCartService(IShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	public IShopOrderdetailService getShopOrderdetailService() {
		return shopOrderdetailService;
	}

	public void setShopOrderdetailService(IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}
	public IShopAddressService getShopAddressService() {
		return shopAddressService;
	}

	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}

	public IIntegralBuyRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(IIntegralBuyRecordService recordService) {
		this.recordService = recordService;
	}

	public ShopUtil getShopUtil() {
		return shopUtil;
	}

	public void setShopUtil(ShopUtil shopUtil) {
		this.shopUtil = shopUtil;
	}

	public IShopProductService getProductService() {
		return productService;
	}

	public void setProductService(IShopProductService productService) {
		this.productService = productService;
	}

	public IIntegralBuyRegularService getRegularService() {
		return regularService;
	}

	public void setRegularService(IIntegralBuyRegularService regularService) {
		this.regularService = regularService;
	}

	/**
	 * 微信端积分充值记录
	 * @param form
	 * @return
	 */
	public Page doToIntegralRechargeRecord(WebForm form){
		QueryObject qo = new QueryObject();
		ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请关闭窗口重新进入系统！");
		}
		qo.addQuery("obj.member.id", member.getId(), "=");
		IPageList pageList = this.rechargeRecordService.getIntegralRechargeRecordBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        form.addResult("fu", formatUtil.fu);
		return new Page("/bcd/wxshop/promotions/integral/integralRechargeRecord.html");
	}
	
	/**
	 * 积分商品列表页
	 * @param form
	 * @return
	 */
	public Page doToIntegralProductList(WebForm form){
		IPageList pageList = this.regularService.getAllIntegralRegularForHomeList();
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
		return new Page("/bcd/wxshop/promotions/integral/integralList.html");
	}
	
	/**
	 * 积分商品详情页
	 * @param form
	 * @return
	 */
	public Page doToIntegralProductDetail(WebForm form){
		String regularId=CommUtil.null2String(form.get("regularId"));
		IntegralBuyRegular regular = this.regularService.getIntegralBuyRegular(Long.parseLong(regularId));
    	form.addResult("regular", regular);
    	
    	//向volecity模板中传入商品信息
    	this.addResultForSKDetail(form);
    	
		return new Page("/bcd/wxshop/promotions/integral/integralDetail.html");
	}
	
	/**
     * 限时抢购-请求是否可以进入确认订单页
     * @param form
     * @return
     */
    public Page doRequestIntegralPro(WebForm form) {		
		ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}		
		Long regularId = Long.parseLong(form.get("regularId").toString());
		IntegralBuyRegular regular = this.regularService.getIntegralBuyRegular(regularId);
		String integralListUrl = "<br /><a style=\"color:red;\" href=\"/wxIntegralBuy.java?cmd=toProductIntegralList\">查看其他积分活动</a>";
		//活动状态
		if(!regular.getState().equals("shelving")){
			return this.error(form, "请求的活动还没有开始！"+integralListUrl);
		}
		//库存数量
		if(regular.getPro().getInventory().compareTo(0) <= 0){
			return this.error(form, "该商品已经售罄！"+integralListUrl);
		}
		//积分数量不足
		if(regular.getIntegralPrice().compareTo(member.getAvailableIntegral()) > 0){
			return this.error(form, "您只有"+member.getAvailableIntegral()+"可用，不足以购买该积分商品！"+integralListUrl);
		}
				
		//有资格，进入下单页
		return DiscoShopUtil.goPage(this.getActivityProUrl(regular, form));		
	}
    
	/**
	 * 积分商品下单页
	 * @param form
	 * @return
	 */
	public Page doToIntegralProductOrder(WebForm form){
		String regularStr = CommUtil.null2String(form.get("regularId"));
		String payProNum = CommUtil.null2String(form.get("payProNum")); //商品数量
		String addrid = CommUtil.null2String(form.get("addrid"));		
		String url = CommUtil.null2String(form.get("url"));
		
		if(regularStr.equals("")){
			return this.error(form, "请从活动页进入！");
		}
		IntegralBuyRegular regular = this.regularService.getIntegralBuyRegular(Long.parseLong(regularStr));
		form.addResult("regular", regular);
		if(!"".equals(addrid)){
			ShopAddress addr = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
			form.addResult("addr", addr);
		}
		form.addResult("pro", regular.getPro());
		
		form.addResult("numstr", payProNum);
		if(!"".equals(url)){
			form.addResult("url", url);
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.isDefault", true, "=");
		qo.addQuery("obj.user", this.getShopMember(form), "=");
		IPageList pl = this.shopAddressService.getShopAddressBy(qo);
		if(pl.getResult()!=null){
			ShopAddress addrd = (ShopAddress) pl.getResult().get(0); 
			form.addResult("addrd", addrd);
		}
		
		return new Page("/bcd/wxshop/promotions/integral/integralOrder.html");
	}
	
	/**
     * 客户订单详情
     * @param form
     * @return
     */
    public Page doToIntegralOrderDetail(WebForm form){
    	String orderId = CommUtil.null2String(form.get("orderId"));
    	String regularId = CommUtil.null2String(form.get("regularId"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
    	IntegralBuyRegular regular = this.regularService.getIntegralBuyRegular(Long.parseLong(regularId));
    	form.addResult("order", order);
    	form.addResult("regular", regular);    
    	return new Page("/bcd/wxshop/promotions/integral/orderDetails.html");
    }
	
	/**
	 * 我的订单 显示页
	 * @param form
	 * @return
	 */
	public Page doToIntegralOrderRecord(WebForm form){
		ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请重新登录！");
		}
		form.addResult("member", member);
		QueryObject qo = new QueryObject();
    	qo.addQuery("obj.member.id", member.getId(), "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
		List<IntegralBuyRecord> orderList = (List<IntegralBuyRecord>) this.recordService.getIntegralBuyRecordBy(qo).getResult();
		form.addResult("records", orderList);
		
		return new Page("/bcd/wxshop/promotions/integral/integralOrderRecord.html");
	}
	
	/**
	 * 创建积分购物订单
	 * @param form
	 * @return
	 */
	public Page doIntegralProductOrder(WebForm form){
		ShopMember member = this.getShopMember(form);
		String regularStr = CommUtil.null2String(form.get("regularId"));
		if(regularStr.equals("")){
			return this.error(form, "该商品只能从【积分购买】板块进入购买！");
		}
		IntegralBuyRegular regular = this.regularService.getIntegralBuyRegular(Long.parseLong(regularStr));
		
		//检查库存是否满足购买需求
		this.checkInventoryEnough(form, "direct");
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){
			return error(form, errorStr);
		}
		
		//检查可用积分是否足够		
		if(member.getAvailableIntegral() < regular.getIntegralPrice()){
			return error(form, "您的积分不足以购买该商品，不能下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/wxIntegralBuy.java?cmd=toIntegralProductList", this.getAccount(form))+"\">选购其他积分商品</a>");
		}
		
		/**
		 * 直接购买生成订单2
		 */
		ShopAddress sa=null;
		String addrid = CommUtil.null2String(form.get("addrid"));
		String msg_self = CommUtil.null2String(form.get("msg_self"));
		if(!"".equals(addrid)){
			sa = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
		}
		ShopSpec spec = null;
		Double singleprice= 0.0;
		Double totalprice = 0.0;
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = this.getShopMember(form);
		
		ShopDistributor distri = null;		
		//确定是谁的店
		if(user.getDisType() == 0){
			distri = user.getDistributor();
		}else{
			distri = user.getMyDistributor();
		}
		ShopDistributor topdistri = null;
		if(distri != null){
			if(distri.getExStatus() == 1){
				topdistri = distri;
			}else{
				topdistri = distri.getTopDistributor();
			}
		}
		
		order.setUser(user);
		order.setAddr(sa);
		order.setMsg_self(msg_self);
		order.setDistributor(distri);
		order.setTopDistributor(topdistri);
		order.setCode(new Date().getTime()+"");
		
		this.shopOrderInfoService.addShopOrderInfo(order);
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */
		String pid = CommUtil.null2String(form.get("pid"));//商品ID
		String numstr = CommUtil.null2String(form.get("num"));//商品数量
		int num = Integer.parseInt(numstr);
		Double gross_price_pro = 0.0;//商品价值
		Double gross_price = 0.0;//商品+运费价值
		
		ShopProduct pro = this.productService.getShopProduct(Long.parseLong(pid));
		
		String code = new Date().getTime()+"";
		totalprice = 1.0*num*regular.getIntegralPrice();
		this.createOrderDetail(pro, num, user, distri, spec, code, singleprice, totalprice, order);
		gross_price_pro += totalprice;
		
		gross_price = gross_price + gross_price_pro;
		order.setGross_price(gross_price);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		
		//邮费计算和设置,该计算必须在上面更新完订单的操作后进行
		//Double freight = calculateFreight(order);
		//Double freight = this.deliveryService.getDeliveryCost(order);
		//gross_price = gross_price+freight;
		order.setFreight(0D);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		
		//生成积分购买记录
		if(this.setRecord(member, order, regular, form) == null){
			String integralListUrl = "<br /><a style=\"color:red;\" href=\"/wxIntegralBuy.java?cmd=toProductIntegralList\">查看其他积分活动</a>";
			return this.error(form, "您本次购买失败！"+integralListUrl);
		}
		
		//积分购买成功通知
		Follower f=member.getFollower();
		if(f!=null){
			Account a = f.getAccount();
			WeixinBaseUtils.sendMsgToFollower(a, f, shopMsgUtil.getPaySuccessMsg(member, order));
		}
		
		return this.error(form, "<div style='border:1px dotted gray; margin-left:0px; margin-right:0px; text-align:center;'><br /><p style='color:red; font-size:20px;'>恭喜您，该积分商品已经兑换成功！</p> "
				+ "<br /><a href='wxIntegralBuy.java?cmd=toIntegralOrderRecord' style='font-size:16px;'>查看积分订单</a> "
				+ "<br /><a href='wxIntegralBuy.java?cmd=toIntegralProductList' style='font-size:16px;'>查看其他积分商品</a></div>");
	}
	
	/**
	 * 积分商品支付页
	 * @param form
	 * @return
	 */
	public Page doToIntegralProductPay(WebForm form){
		return new Page("/bcd/wxshop/promotions/integral/integralPay.html");
	}
	
	/**
	 * 通过佣金充值积分
	 * @param form
	 * @return
	 */
	public Page doRechargeIntegralByWithdraw(WebForm form){
		return Page.JSONPage;
	}
	
	/**
	 * 通过微信支付充值积分
	 * @param form
	 * @return
	 */
	public Page doRechargeIntegralByWeixin(WebForm form){
		return Page.JSONPage;
	}
		
	private void createOrderDetail(ShopProduct pro,int num,ShopMember user,ShopDistributor distri,ShopSpec spec,String code,Double singleprice,Double totalprice,ShopOrderInfo order){
		ShopOrderdetail orderDetail = new ShopOrderdetail();
		orderDetail.setPro(pro);
		orderDetail.setNum(num);
		orderDetail.setUser(user);
		orderDetail.setSeller(distri);
		orderDetail.setShopSpec(spec);
		orderDetail.setCode(new Date().getTime()+"");
		orderDetail.setUnit_price(singleprice);//单价
		orderDetail.setGross_price(totalprice);//总价
		orderDetail.setOrderInfo(order);
		this.shopOrderdetailService.addShopOrderdetail(orderDetail);
		Long specid = 0L;
		if(spec != null){
			specid = spec.getId();
		}
		this.productService.updateShopProductAfterPay(pro,specid,num);
	}
	
	/**
	 * 检查提交的购买需求在库存方面是否满足
	 * @param form
	 * @param orderFrom
	 */
	private void checkInventoryEnough(WebForm form, String orderFrom){
		if(orderFrom.equals("direct")){
			String pid = CommUtil.null2String(form.get("pid"));//商品ID
			String numstr = CommUtil.null2String(form.get("num"));//商品数量			
			if(pid.equals("")){
				form.addResult("inventoryError", "该订单没有指定商品，请选购商品重新下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/wxShopBase.java?cmd=init", this.getAccount(form))+"\">去首页选购商品</a>");				
			}
			if(numstr.equals("")){
				form.addResult("inventoryError", "该订单商品选购数量为0，不能下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/wxIntegralBuy.java?cmd=toIntegralProductDetail&regularId="+form.get("regularId"), this.getAccount(form))+"\">重新选购商品</a>");
			}
			int num = Integer.parseInt(numstr);
			ShopProduct pro = this.productService.getShopProduct(Long.parseLong(pid));
			if(0 > (pro.getInventory()-num) ){
				form.addResult("inventoryError", "该订单商品库存【"+pro.getInventory()+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/wxIntegralBuy.java?cmd=toIntegralProductDetail&regularId="+form.get("regularId"), this.getAccount(form))+"\">重新选购商品</a>");
			}
		}
	}
	
	/**
     * 抢购详情页的商品信息类数据添加
     * @param form
     */
    private void addResultForSKDetail(WebForm form){
    	String id=CommUtil.null2String(form.get("pId"));        
        ShopProduct shopProduct=this.productService.getShopProduct(Long.valueOf(id));
        form.addResult("pro", shopProduct);
        
        //分享
        form.addResult("su", this.shopUtil);
        
        //推荐商品信息
        QueryObject qo = new QueryObject();
        Tenant t = (Tenant) TenantContext.getTenant();
        qo.addQuery("obj.tenant", t, "=");
        qo.addQuery("obj.isRecommend", true, "=");
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        qo.setPageSize(5);
        List<?> proList=this.productService.getShopProductBy(qo).getResult();
        Integer proCount=this.productService.getShopProductBy(qo).getRowCount();
        form.addResult("proList", proList);
        form.addResult("proCount", proCount);
        
        WeixinBaseUtils.setWeixinjs(form, getAccount(form));
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
     * 服务器端秒杀商品下单页
     * @param regular
     * @param form
     * @return
     */
    private String getActivityProUrl(IntegralBuyRegular regular, WebForm form){
    	String result = "";
    	result += "/wxIntegralBuy.java?cmd=toIntegralProductOrder&regularId="+regular.getId()+"&payProNum="+form.get("payProNum");
    	return result;
    }
}
