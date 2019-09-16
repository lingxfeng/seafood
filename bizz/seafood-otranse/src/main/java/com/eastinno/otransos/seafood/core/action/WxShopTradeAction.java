package com.eastinno.otransos.seafood.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.poi.util.StringUtil;
import org.apache.tools.ant.dispatch.DispatchUtils;
import org.springframework.util.StringUtils;

import com.alibaba.druid.sql.dialect.odps.parser.OdpsExprParser;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.domain.SystemConfig;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.seafood.content.domain.ShopDiscuss;
import com.eastinno.otransos.seafood.content.service.IShopDiscussService;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.seafood.distribu.domain.CommissionDetail;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.ICommissionConfigService;
import com.eastinno.otransos.seafood.distribu.service.ICommissionDetailService;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.seafood.droduct.service.IProductTypeService;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.seafood.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.seafood.promotions.service.IIntegralChangeRuleService;
import com.eastinno.otransos.seafood.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.seafood.promotions.service.IRushBuyRegularService;
import com.eastinno.otransos.seafood.spokesman.service.ISubsidyService;
import com.eastinno.otransos.seafood.trade.domain.CalculateDetail;
import com.eastinno.otransos.seafood.trade.domain.OrderDetailShow;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.seafood.trade.service.IShopPayMentService;
import com.eastinno.otransos.seafood.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;
import com.eastinno.otransos.seafood.usercenter.domain.ShoppingCart;
import com.eastinno.otransos.seafood.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.seafood.usercenter.service.IShopSinceSomeService;
import com.eastinno.otransos.seafood.usercenter.service.IShoppingCartService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.seafood.util.TokenUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 交易控制action
 * @author nsz
 *
 */
@Action
public class WxShopTradeAction extends WxShopBaseAction{
	@Inject
	private ISystemRegionService systemRegionService;
	@Inject
    private IShopAddressService shopAddressService;
	@Inject
    private IShopOrderInfoService shopOrderInfoService;
	@Inject
    private IShopOrderdetailService shopOrderdetailService;
	@Inject
    private IPaymentConfigService paymentConfigService;
	@Inject
	private IShopPayMentService shopPayMentService;
	@Inject
	private IShopProductService shopProductService;
	@Inject
	private IShoppingCartService shoppingCartService;
	@Inject
	private IShopDistributorService shopDistributorService;
	@Inject
	private IShopSpecService shopSpecService;
	@Inject
	private ICommissionConfigService commissionConfigService;
	@Inject
	private IShopSystemConfigService shopSystemConfigService;
	@Inject
	private IShopMemberService shopMemberService;
	@Inject
	private ICommissionDetailService commissionDetailService;
	@Inject 
	private IDeliveryRuleService deliveryService;
	@Inject
	private IRushBuyRegularService regularService;	
	@Inject
	private IRushBuyRecordService recordService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IIntegralRechargeRecordService integralRechargeRecordService;
	@Inject
	private IIntegralChangeRuleService integralChangeRuleService;
	@Inject
	private IIntegralHistoryService integralHistoryService;
	@Inject
	private ISubsidyService subsidyService;
	@Inject
	private IShopSinceSomeService shopSinceSomeService;
	@Inject
	private IShopDiscussService shopDiscussService;
	/**
	 * 进入添加收货地址页面
	 * @param form
	 * @return
	 */
	public Page doAddAdress(WebForm form){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent is EMPTY");
		List<?> list = systemRegionService.getRootSystemRegions().getResult();
		form.addResult("rootRegions", list);
		return new Page("/trade/addAdress.html");
	}
	/**
	 * 添加收货地址
	 * @param form
	 * @return
	 */
	public Page doSaveAddress(WebForm form){
		ShopAddress obj = form.toPo(ShopAddress.class);
		ShopMember user = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		obj.setUser(user);
		if (!hasErrors()) {
            Long id = this.shopAddressService.addShopAddress(obj);
        }
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user",user,"=");
		List<?> list = this.shopAddressService.getShopAddressBy(qo).getResult();
		form.addResult("list", list);
		return new Page("/trade/addresses.html");
	}
	/**
	 * 编辑收货地址
	 * @param form
	 * @return
	 */
	public Page doToEditAddress(WebForm form){
		String id = CommUtil.null2String(form.get("id"));
		ShopAddress obj = this.shopAddressService.getShopAddress(Long.parseLong(id));
		form.addResult("entry", obj);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.parent is EMPTY");
		List<?> list = systemRegionService.getRootSystemRegions().getResult();
		form.addResult("rootRegions", list);
		return new Page("/trade/editAddress.html");
	}
	/**
	 * 更新收货地址
	 * @param form
	 * @return
	 */
	public Page doUpdateAddress(WebForm form){
		String id = CommUtil.null2String(form.get("id"));
		ShopAddress entry = this.shopAddressService.getShopAddress(Long.parseLong(id));
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = this.shopAddressService.updateShopAddress(Long.parseLong(id), entry);
        }
        form.addResult("entry", entry);
		return new Page("/trade/oneAddress.html");
	}
	/**
	 * 设为默认地址
	 * @param form
	 * @return
	 */
	public Page doSetDeafaultAdd(WebForm form){
		String id = CommUtil.null2String(form.get("id"));
		ShopAddress entry = this.shopAddressService.getShopAddress(Long.parseLong(id));
		QueryObject qo = new QueryObject();
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		qo.addQuery("obj.user",user,"=");
		qo.addQuery("obj.isDefault",Boolean.TRUE,"=");
		List<ShopAddress> list = this.shopAddressService.getShopAddressBy(qo).getResult();
		if(list!=null){
			for(ShopAddress add:list){
				add.setIsDefault(false);
				this.shopAddressService.updateShopAddress(add.getId(), add);
			}
		}
		entry.setIsDefault(true);
		this.shopAddressService.updateShopAddress(Long.parseLong(id), entry);
		form.jsonResult(true);
		return Page.JSONPage;
	}
		
	/**
	 * 购物车购买跳转页面
	 */
	public Page doBeforeCreateOrder2(WebForm form){
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		String addrid = CommUtil.null2String(form.get("addrid"));
		String carid = CommUtil.null2String(form.get("cIds"));
		String url = CommUtil.null2String(form.get("url"));
		
		Double freight = this.shopSystemConfigService.getSystemConfig().getFreight();
		form.addResult("freight", freight);
		//ShopAddress addr = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
		if(!"".equals(carid)){
			String[] carIdNums = carid.split(",");
			List<ShoppingCart> list = new ArrayList<>();
			for(String id:carIdNums){
				ShoppingCart car = this.shoppingCartService.getShoppingCart(Long.parseLong(id));
				list.add(car);
			}
			form.addResult("list",list);
		}else{
			String msg = "请刷新后重试！";
			form.addResult("msg", msg);
		}
		if(!"".equals(addrid)){
			ShopAddress addr = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
			form.addResult("addr", addr);
		}
		form.addResult("carid",carid);
		if(!"".equals(url)){
			form.addResult("url", url);
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.isDefault", true, "=");
		qo.addQuery("obj.user", user, "=");
		IPageList pl = this.shopAddressService.getShopAddressBy(qo);
		if(pl.getResult()!=null){
			ShopAddress addrd = (ShopAddress) pl.getResult().get(0); 
			form.addResult("addrd", addrd);
		}
		
		
		//判断身份
		String flag = null;
		QueryObject qos = new QueryObject();
		qos.addQuery("obj.member",user,"=");
		List<ShopDistributor> listdis = this.shopDistributorService.getShopDistributorBy(qos).getResult();
		if(listdis!=null && listdis.size()!=0){
			ShopDistributor mydis = listdis.get(0);
			if(mydis.getStatus()==1 && mydis.getExStatus()!=1){
				flag = "weidian";
			}else if(mydis.getExStatus()==1){
				flag = "tiyandian";
			}else{
				flag = "huiyuan";
			}
		}else{
			flag = "huiyuan";
		}
		form.addResult("flag", flag);
		
		//生成SESSION表单令牌，并放入MODEL
		form.addResult("formToken", TokenUtil.createSessionToken());
		String smzt = CommUtil.null2String(form.get("smzt"));
		if(StringUtils.hasText(smzt)){
			ShopSinceSome sss = this.shopSinceSomeService.getShopSinceSome(Long.valueOf(smzt));
			form.addResult("sss", sss);
		}
		return new Page("/bcd/wxshop/trading/payorder.html");
	}
	/**
	 * 购物车生成订单2
	 */
	synchronized public Page doCreateOrder2(WebForm form){
		//检查表单令牌是否存在
		String formToken = form.get("formToken").toString();
		if(!TokenUtil.existsSessionTokenThenDelete(formToken)){
			return this.error(form, "对不起，您当前订单已经失效，请重新下单！");
		}
		
		//检查库存是否满足购买需求
		checkInventoryEnough(form, "cart");
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){
			return error(form, errorStr);
		}
		
		String carid = CommUtil.null2String(form.get("cIds"));
		ShopAddress sa=null;
		String addrid = CommUtil.null2String(form.get("addrid"));
		ShopSinceSome sss = null;
		String ShopSinceSomeId =CommUtil.null2String(form.get("ShopSinceSomeId"));
		String msg_self = CommUtil.null2String(form.get("msg_self"));
		
		if(StringUtils.hasText(ShopSinceSomeId)){
			sss = this.shopSinceSomeService.getShopSinceSome(Long.valueOf(ShopSinceSomeId));
		}else{
			if(!"".equals(addrid)){
				sa = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
			}
		}
		ShopMember user = this.getShopMember(form);
		
		ShopDistributor distri = null;
		ShopMember puser = null;
		ShopDistributor mydis = null;
		
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
		//判定自己是不是店的身份
		QueryObject qos = new QueryObject();
		qos.addQuery("obj.member",user,"=");
		List<ShopDistributor> listdis = this.shopDistributorService.getShopDistributorBy(qos).getResult();
		String flag=null;
		if(listdis!=null && listdis.size()!=0){
			mydis = listdis.get(0);
			if(mydis.getStatus()==1 && mydis.getExStatus()!=1){
				flag = "weidian";
			}else if(mydis.getExStatus()==1){
				flag = "tiyandian";
			}else{
				flag = "huiyuan";
			}
		}else{
			flag = "huiyuan";
		}
		form.addResult("flag", flag);
	
		ShopOrderInfo order = new ShopOrderInfo();
		order.setShopSinceSome(sss);
		order.setDistributor(distri);
		order.setTopDistributor(topdistri);
		String codestr = new Date().getTime()+"";
		order.setCode(codestr);
		order.setUser(user);
		order.setAddr(sa);
		order.setMsg_self(msg_self);
		order.setOrderType((short)2);
		this.shopOrderInfoService.addShopOrderInfo(order);
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */	
		if(!"".equals(carid)){
			String[] carIdNums = carid.split(",");
			Double allAmt = 0.0;
			Double gross_price_pro = 0.0;//商品价值
			Double gross_price = 0.0;//商品+运费价值
			for(String id:carIdNums){
				
				Double singleprice= 0.0;
				Double totalprice = 0.0;
				//获取一条购物车数据
				ShoppingCart car = this.shoppingCartService.getShoppingCart(Long.parseLong(id));
				ShopProduct pro = car.getShopProduct();
				int num=car.getBuyNum();
				ShopSpec spec = car.getShopSpec();
				//将子订单信息添加进大订单
				String code = new Date().getTime()+""+id;
					//是微店
				if(flag.equals("weidian")){
					if(spec!=null){
						singleprice = spec.getStore_price();
						totalprice = spec.getStore_price()*num;
					}else{
						singleprice = pro.getStore_price();
						totalprice = pro.getStore_price()*num;
					}
					
				//是体验店
				}else if(flag.equals("tiyandian")){
					if(spec!=null){
						singleprice = spec.getTydAmt();
						totalprice = spec.getTydAmt()*num;
					}else{
						singleprice = pro.getTydAmt();
						totalprice = pro.getTydAmt()*num;
					}
					
				}else{
					if(spec!=null){
						singleprice = spec.getAmt();
						totalprice = spec.getAmt()*num;
					}else{
						singleprice = pro.getAmt();
						totalprice = pro.getAmt()*num;
					}
				}
				
				createOrderDetail(pro,num,user,distri,spec,code,singleprice,totalprice,order);	
				this.shoppingCartService.delShoppingCart(Long.parseLong(id));
				gross_price_pro += totalprice;
			}
			//Double freight = calculateFreight(order);
			Double freight = this.deliveryService.getDeliveryCost(order);
			order.setFreight(freight);
			gross_price = gross_price + gross_price_pro + freight;
			order.setGross_price(gross_price);
			order.setProduct_price(gross_price_pro);
			this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		}		
		return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=orderDetail2&orderId="+order.getId());
	}

	synchronized public Page doCreateOrder3(WebForm form){

		//检查表单令牌是否存在
		String formToken = form.get("formToken").toString();
		if(!TokenUtil.existsSessionTokenThenDelete(formToken)){
			return this.error(form, "对不起，您当前订单已经失效，请重新下单！");
		}
		
		//检查库存是否满足购买需求
		this.checkInventoryEnough(form, "direct");
		String errorStr = (String) form.getDiscoResult().get("inventoryError");
		if(errorStr != null){
			return error(form, errorStr);
		}
		
		/**
		 * 直接购买生成订单2
		 */
		ShopAddress sa=null;
		ShopSinceSome sss = null;
		String addrid = CommUtil.null2String(form.get("addrid"));
		String msg_self = CommUtil.null2String(form.get("msg_self"));
		String ShopSinceSomeId =CommUtil.null2String(form.get("ShopSinceSomeId"));
		if(StringUtils.hasText(ShopSinceSomeId)){
			sss = this.shopSinceSomeService.getShopSinceSome(Long.valueOf(ShopSinceSomeId));
		}else{
			if(!"".equals(addrid)){
				sa = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
			}
		}
		ShopSpec spec = null;
		Double singleprice= 0.0;
		Double totalprice = 0.0;
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = this.getShopMember(form);
		
		ShopDistributor distri = null;
		ShopMember puser = null;
		
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
		//判断自己是什么身份
		ShopDistributor mydis = null;
		QueryObject qos = new QueryObject();
		qos.addQuery("obj.member",user,"=");
		List<ShopDistributor> listdis = this.shopDistributorService.getShopDistributorBy(qos).getResult();
		String flag=null;
		if(listdis!=null && listdis.size()!=0){
			mydis = listdis.get(0);
			if(mydis.getStatus()==1 && mydis.getExStatus()!=1){
				flag = "weidian";
			}else if(mydis.getExStatus()==1){
				flag = "tiyandian";
			}else{
				flag = "huiyuan";
			}
		}else{
			flag = "huiyuan";
		}
		form.addResult("flag", flag);
		
		
		order.setShopSinceSome(sss);
		order.setUser(user);
		order.setAddr(sa);
		order.setMsg_self(msg_self);
		order.setDistributor(distri);
		order.setTopDistributor(topdistri);
		order.setCode(new Date().getTime()+"");
		order.setOrderType((short)2);
		this.shopOrderInfoService.addShopOrderInfo(order);
		/**
		 * 生成订单结束
		 * 生成订单明细
		 */
		String pid = CommUtil.null2String(form.get("pid"));//商品ID
		String specid = CommUtil.null2String(form.get("specid"));//规格ID
		String numstr = CommUtil.null2String(form.get("num"));//商品数量
		int num = Integer.parseInt(numstr);
		Double gross_price_pro = 0.0;//商品价值
		Double gross_price = 0.0;//商品+运费价值
		
		Double allAmt = 0.0;
		ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
			//是微店
			if(flag.equals("weidian")){
				if(!"".equals(specid)){
					spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
					singleprice = spec.getStore_price();
					totalprice = spec.getStore_price()*num;
				}else{
					singleprice = pro.getStore_price();
					totalprice = pro.getStore_price()*num;
				}
			//是体验店
			}else if(flag.equals("tiyandian")){
				if(!"".equals(specid)){
					spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
					singleprice = spec.getTydAmt();
					totalprice = spec.getTydAmt()*num;
				}else{
					singleprice = pro.getTydAmt();
					totalprice = pro.getTydAmt()*num;
				}
				
			}else{
				if(!"".equals(specid)){
					spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
					singleprice = spec.getAmt();
					totalprice = spec.getAmt()*num;
				}else{
					singleprice = pro.getAmt();
					totalprice = pro.getAmt()*num;
				}
			}
		String code = new Date().getTime()+"";
		createOrderDetail(pro,num,user,distri,spec,code,singleprice,totalprice,order);
		gross_price_pro += totalprice;
		
		gross_price = gross_price + gross_price_pro;
		order.setGross_price(gross_price);
		order.setProduct_price(gross_price_pro);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		
		//邮费计算和设置,该计算必须在上面更新完订单的操作后进行
		//Double freight = calculateFreight(order);
		Double freight = this.deliveryService.getDeliveryCost(order);
		gross_price = gross_price+freight;
		order.setFreight(freight);
		order.setGross_price(gross_price);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		
		//促销活动回调函数
		try {
			this.createOrderCallBack(order, pro, "direct");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=orderDetail2&orderId="+order.getId());
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
			String specid = CommUtil.null2String(form.get("specid"));//规格ID
			if(pid.equals("")){
				form.addResult("inventoryError", "该订单没有指定商品，请选购商品重新下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/wxShopBase.java?cmd=init", this.getAccount(form))+"\">去首页选购商品</a>");				
			}
			if(numstr.equals("")){
				form.addResult("inventoryError", "该订单商品选购数量为0，不能下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/shopProduct.java?cmd=toProDet&pId="+pid, this.getAccount(form))+"\">重新选购商品</a>");
			}
			int num = Integer.parseInt(numstr);
			int inventory = 0;
			if(specid.equals("")){
				ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
				inventory = pro.getInventory();								
			}else{
				ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
				inventory = spec.getInventory();
			}
			if(0 > (inventory-num) ){
				form.addResult("inventoryError", "该订单商品库存【"+inventory+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+"/goShop.java?cmd=init"+"\">去首页选购其他商品</a>");
			}
		}else if(orderFrom.equals("cart")){
			String carid = CommUtil.null2String(form.get("cIds"));
			String[] carIdNums = carid.split(",");
			for(String id:carIdNums){
				//获取一条购物车数据
				ShoppingCart car = this.shoppingCartService.getShoppingCart(Long.parseLong(id));
				ShopProduct pro = car.getShopProduct();
				int num=car.getBuyNum();
				ShopSpec spec = car.getShopSpec();
				
				if(spec!=null){
					if(0 > (spec.getInventory()-num) ){
						String preError = (String) form.get("inventoryError");
						form.addResult("inventoryError", preError+"<br/>该订单中 <<<"+spec.getProduct().getName()+">>>库存【"+spec.getInventory()+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/shoppingCart.java?cmd=toShoppingCart", this.getAccount(form))+"\">调整数量再下单</a>");
					}
				}else{
					if(0 > (pro.getInventory()-num) ){
						String preError = (String) form.get("inventoryError");
						form.addResult("inventoryError", preError+"<br/>该订单中 <<<"+pro.getName()+">>>库存【"+pro.getInventory()+"】低于购买量【"+num+"】，不能下单！<br/><a style=\"color:red;\" href=\""+WeixinBaseUtils.getWxUrl(WeixinBaseUtils.getDomain()+"/shoppingCart.java?cmd=toShoppingCart", this.getAccount(form))+"\">调整数量再下单</a>");
					}
				}
			}
		}
	}
	
	/**
	 * 创建完订单后，回调函数
	 * @param orderInfo
	 * @throws Exception 
	 */
	private void createOrderCallBack(ShopOrderInfo orderInfo, ShopProduct pro, String orderFrom) throws Exception{
		if(orderFrom.equals("direct")){
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.pro.id", pro.getId(), "=");
			//商品是否属于秒杀活动商品
			List<RushBuyRegular> regularList = this.regularService.getAllSecKillRegularByQO(qo).getResult();
			if(regularList != null){
				int listNum = regularList.size();
				for(int i=0; i<listNum; ++i){
					RushBuyRegular tempRegular = regularList.get(i);
					QueryObject tempQo = new QueryObject();
					tempQo.addQuery("obj.regular.id", tempRegular.getId(), "=");
					tempQo.addQuery("obj.member.id", orderInfo.getUser().getId(), "=");
					tempQo.addQuery("obj.createDate", orderInfo.getCeateDate(), ">=");
					RushBuyRecord record = this.recordService.getSingleAvailableSecKillRecordByMemberAndRegular(tempRegular, orderInfo.getUser());
					if(record == null){
						System.out.println("下单错误：用户[id="+orderInfo.getUser().getId()+"]未参加促销活动[code="+tempRegular.getCode()+"]，却进入下单页，请程序员检查该BUG!");
						continue;
					}
					record.setOrder(orderInfo);
					this.recordService.updateSecKillRecord(record);
					orderInfo.setType("seckill");
					this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
				}
			}
			//商品是否属于限时抢购活动商品
			qo = new QueryObject();
			qo.addQuery("obj.pro.id", pro.getId(), "=");
			regularList = this.regularService.getAllTimeLimitRegularByQO(qo).getResult();
			if(regularList != null){
				int listNum = regularList.size();
				for(int i=0; i<listNum; ++i){
					RushBuyRegular tempRegular = regularList.get(i);
					QueryObject tempQo = new QueryObject();
					tempQo.addQuery("obj.regular.id", tempRegular.getId(), "=");
					tempQo.addQuery("obj.member.id", orderInfo.getUser().getId(), "=");
					tempQo.addQuery("obj.createDate", orderInfo.getCeateDate(), ">=");
					RushBuyRecord record = this.recordService.getSingleAvailableTimeLimitRecordByMemberAndRegular(tempRegular, orderInfo.getUser());
					if(record == null){
						System.out.println("下单错误：用户[id="+orderInfo.getUser().getId()+"]未参加促销活动[code="+tempRegular.getCode()+"]，却进入下单页，请程序员检查该BUG!");
						continue;
					}
					record.setOrder(orderInfo);
					this.recordService.updateTimeLimitRecord(record);
					orderInfo.setType("timelimit");
					this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
				}
			}
		}else if(orderFrom.equals("cart")){
			//购物车的暂时没写，待定
		}
	}
	
	  /**
     * 测试佣金计算
     * 
     * @param form
     */
	public Page doTestDistributorAmt(WebForm form) {
		String orderid = CommUtil.null2String(form.get("orderid"));//商品ID
		if(!"".equals(orderid)){
			ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderid));
			this.shopOrderInfoService.disTributorAmt(order);//处理佣金
		}
		
		return new Page("/bcd/wxshop/trading/sucsses.html");
	}
	
	/**
     * 递归获取上第一级微店（店）
     * 
     * @param form
     */
	public ShopDistributor getDis(ShopMember member){
		if(member!=null){
			QueryObject qo  = new QueryObject();
			qo.addQuery("obj.member",member,"=");
			qo.setPageSize(1);
			List<ShopDistributor> list = this.shopDistributorService.getShopDistributorBy(qo).getResult();
			if(list == null || list.size()==0){
				ShopMember pMember = member.getPmember();
				if(pMember==null){
					return null;
				}
				return getDis(pMember);
			}
			return list.get(0);
		}
		return null;
	}
	
	
	private void createOrderDetail(ShopProduct pro,int num,ShopMember user,ShopDistributor distri,ShopSpec spec,String code,Double singleprice,Double totalprice,ShopOrderInfo order){
		ShopOrderdetail orderDetail = new ShopOrderdetail();
		orderDetail.setPro(pro);
		orderDetail.setNum(num);
		orderDetail.setUser(user);
		orderDetail.setSeller(distri);
		orderDetail.setShopSpec(spec);
		orderDetail.setCode(code);
		orderDetail.setUnit_price(singleprice);//单价
		orderDetail.setGross_price(totalprice);//总价
		orderDetail.setOrderInfo(order);
		this.shopOrderdetailService.addShopOrderdetail(orderDetail);
		Long specid = 0L;
		if(spec != null){
			specid = spec.getId();
		}
		this.shopProductService.updateShopProductAfterPay(pro,specid,num);
	}
	
	public Double calculateFreight(ShopOrderInfo order){

		QueryObject qo = new QueryObject();
		qo.addQuery("obj.orderInfo",order,"=");
		List<ShopOrderdetail> list = this.shopOrderdetailService.getShopOrderdetailBy(qo).getResult();
		Double price=0.0;
		Double freight = 0.0;
		for(ShopOrderdetail orderd:list){
			int numd = orderd.getNum();
			ShopSpec specd = orderd.getShopSpec();
			if(specd!=null){
				price += specd.getAmt()*numd;
			}else{
				price +=orderd.getPro().getAmt()*numd;
			}
		}
		Double sysfreight = this.shopSystemConfigService.getSystemConfig().getFreight();
		if(price<100){
			freight = sysfreight;
		}
		return freight;
	}
	/**
	 * 立即支付跳转
	 * @param form
	 * @return
	 */
	public Page doPayNow(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
		return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=orderDetail2&orderId="+Long.parseLong(orderId));
	}
	

	/**
	 * 进入支付页面
	 * @param form
	 * @return
	 */
	public Page doToPay(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
		if(order.getStatus()!=0){
			return DiscoShopUtil.goPage("/shopTrade.java?cmd=paySuccess&orderId="+orderId);
		}
		form.addResult("order", order);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.status",Integer.parseInt("1"),"=");
		List<?> payConfigs = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
		form.addResult("payConfs", payConfigs);
		return new Page("/trade/paySubmit.html");
	}
	/**
	 * 支付
	 * @param form
	 * @return
	 */
	public Page doPaySubmit(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
        String payTypeId = CommUtil.null2String(form.get("payTypeId"));
        String defaultbank = CommUtil.null2String(form.get("defaultbank"));
        PaymentConfig payConfig = this.paymentConfigService.getPaymentConfig(Long.parseLong(payTypeId));
        ShopOrderInfo orderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
        if(orderInfo.getStatus()!=0){
        	return DiscoShopUtil.goPage("/shopTrade.java?cmd=paySuccess&orderId="+orderId);
        }
        String reMsg = this.shopPayMentService.paySubmit(orderInfo, payConfig, defaultbank);
        return PaymentUtil.sentmsg(reMsg);
	}

	/**
	 * 直接购买跳转页面
	 */
	public Page doBeforeCreateOrder(WebForm form){
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		String pid = CommUtil.null2String(form.get("proId"));//商品ID
		String specid = CommUtil.null2String(form.get("ggId"));//规格ID
		String numstr = CommUtil.null2String(form.get("payProNum"));//商品数量
		String addrid = CommUtil.null2String(form.get("addrid"));
		String url = CommUtil.null2String(form.get("url"));
		
		ShopSystemConfig sc = this.shopSystemConfigService.getSystemConfig();
		Double freight = 0D;
		if(sc != null){
			sc.getFreight();
		}
		form.addResult("freight", freight);
		ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
		/**----------------cl----------------**/
//		String dep=pro.getProductType().getDePath();
//		String ptId=dep.split("@")[1];
//		ProductType pType=this.productTypeService.getProductTypeByCode(ptId);
//		if(pType.getIsSpecialProType()){
//			if(user.getDisType()==0){
//				QueryObject qo = new QueryObject();
//				qo.addQuery("obj.orderInfo.user", user, "=");
//				qo.addQuery("obj.pro.productType.dePath", pType.getDePath()+"%", "like");
//				qo.addQuery("obj.status > 0");
//				List<?> list=this.shopOrderdetailService.getShopOrderdetailBy(qo).getResult();
//				if(list!=null){
//					return error(form, "你已经买过此类商品，不需要再次购买");
//				}
//			}else{
//				return error(form, "您是店铺，无需购买此类产品");
//			}
//		}
		/**----------------cl----------------**/
		if(!"".equals(specid)){
			ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
			form.addResult("spec",spec);
		}
		if(!"".equals(addrid)){
			ShopAddress addr = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
			form.addResult("addr", addr);
		}
		
		form.addResult("pro",pro);
		
		form.addResult("numstr",numstr);
		if(!"".equals(url)){
			form.addResult("url", url);
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.isDefault", true, "=");
		qo.addQuery("obj.user",user, "=");
		IPageList pl = this.shopAddressService.getShopAddressBy(qo);
		if(pl.getResult()!=null){
			ShopAddress addrd = (ShopAddress) pl.getResult().get(0); 
			form.addResult("addrd", addrd);
		}
		
		//判断身份
		String flag = null;
		QueryObject qos = new QueryObject();
		qos.addQuery("obj.member",user,"=");
		List<ShopDistributor> listdis = this.shopDistributorService.getShopDistributorBy(qos).getResult();
		if(listdis!=null && listdis.size()!=0){
			ShopDistributor mydis = listdis.get(0);
			if(mydis.getStatus()==1 && mydis.getExStatus()!=1){
				flag = "weidian";
			}else if(mydis.getExStatus()==1){
				flag = "tiyandian";
			}else{
				flag = "huiyuan";
			}
		}else{
			flag = "huiyuan";
		}
		form.addResult("flag", flag);
		
		//生成SESSION表单令牌，并放入MODEL
		form.addResult("formToken", TokenUtil.createSessionToken());
		
		
		String smzt = CommUtil.null2String(form.get("smzt"));
		if(StringUtils.hasText(smzt)){
			ShopSinceSome sss = this.shopSinceSomeService.getShopSinceSome(Long.valueOf(smzt));
			form.addResult("sss", sss);
		}
		
		return new Page("/bcd/wxshop/trading/payorder.html");
	}

	/**
	 * 支付
	 * @param form
	 * @return
	 */
	public Page doToPayOrder(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
		String c = CommUtil.null2String(form.get("c"));
		ShopOrderInfo orderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
		if(orderInfo.getStatus() == 0){
			QueryObject qo = new QueryObject();
	    	qo.addQuery("obj.type",PayTypeE.WEIXINMPAPI,"=");
	    	qo.setPageSize(1);
	    	List<PaymentConfig> configs = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
	    	if(configs!=null && configs.size()>0){
	    		PaymentConfig config = configs.get(0);
	    		String reMsg="";
	    		if("true".equals(c)){
	    			ShopMember member = orderInfo.getUser();
	    			member = this.shopMemberService.getShopMember(member.getId());
	    			if(member.getRemainderAmt()>0){
	    				reMsg = this.shopPayMentService.paySubmitByAmt2(orderInfo, config, "", orderInfo.getUser());
	    			}else{
	    				return null;
	    			}
	    		}else{
	    			reMsg = this.shopPayMentService.paySubmit(orderInfo, config, "");
	    		}
	    		if("-1".equals(reMsg)){
	    			return null;
	    		}else if("0".equals(reMsg)){
	    			PayReturnObj pro = new PayReturnObj(orderInfo.getUuid(), orderInfo.getCode(), new Date().getTime()+"");
	    			this.updOrder(orderInfo, pro);
	    			form.addResult("jsStr", "0");
	    			return new Page("/bcd/wxshop/trading/payjs2.html");
	    		}
	    		
	    		form.addResult("jsStr", reMsg);
	    		System.out.println("------------------1111-----------------------------------------------------");
	    		System.out.println("jsStr============"+reMsg);
	    		System.out.println("------------------1111-----------------------------------------------------");
	    	}
		}else{
			return error(form, "请勿重复支付！");
		}
		
		return new Page("/bcd/wxshop/trading/payjs.html");
	}
	
	/**
     * 修改订单
     * @param order
     * @param payreturn
     */
    public void updOrder(ShopOrderInfo order,PayReturnObj payreturn){
		if(order != null && order.getStatus() != 1) {
			System.out.println("余额支付订单----------------------"+order.getId());
			order.setTradeCode(payreturn.getTransactionId());
			order.setTradeDate(new Date());
			this.shopMemberService.UpdateMemberAfterPay(order);
			this.shopOrderInfoService.disPaySuccess(order);
			if(order.getIsSpokesman() == 0){
				this.shopOrderInfoService.disTributorAmt(order);//处理佣金
			}else{
				this.subsidyService.calculateSubsidyFirst(order);//计算第一次补贴
				this.shopOrderInfoService.spokesmanTeam(order);//处理团队业绩
			}
		}
    }
	
    /**
     * 处理订单
     * 董亮亮（处理特殊业务使用，非本人勿用！）
     * @param order
     * @param payreturn
     */
    public void updOrderspecial(WebForm form){
    	String id = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(id));
		if(order != null && order.getStatus() != 1) {
			System.out.println("特殊处理订单----------------------"+order.getId());
			order.setTradeDate(new Date());
			this.shopOrderInfoService.disPaySuccess(order);
			this.shopOrderInfoService.disTributorAmt(order);//处理佣金
		}
    }
	
	/**
	 * 支付成功回调页面
	 * @param form
	 * @return
	 */
	public Page doPaySuccess(WebForm form){
		String paySign = CommUtil.null2String(form.get("paySign"));
		if(StringUtils.isEmpty(paySign)){
			return Page.JSONPage;
		}
		String orderId = CommUtil.null2String(form.get("orderId"));
		ShopOrderInfo orderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
		form.addResult("order", orderInfo);
		/*Map<String,String> map = new HashMap();
		List<?> orderList = orderInfo.getOrderdetails();
		if(orderList.size()==1){
			ShopOrderdetail shopOrderdetail=(ShopOrderdetail)orderList.get(0);
			String dep=shopOrderdetail.getPro().getProductType().getDePath();
			String ptId=dep.split("@")[1];
			ProductType pType=this.productTypeService.getProductTypeByCode(ptId);
			if(pType.getIsSpecialProType()){
				form.jsonResult("1");
				map.put("status", "1");
			}
		}
		form.jsonResult(map);*/
		return Page.JSONPage;
	}
	/**
	 * 异步支付成功回调
	 * @param form
	 * @return
	 */
//	public Page doPaySuccessAjax(WebForm form){
//		String paySign = CommUtil.null2String(form.get("paySign"));
//		if(StringUtils.isEmpty(paySign)){
//			return Page.JSONPage;
//		}
//		
//		Map<String,String> map = new HashMap();
//		String orderId = CommUtil.null2String(form.get("orderId"));		
//		ShopOrderInfo orderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
//		ShopMember member = orderInfo.getUser();
//		ShopDistributor dis = member.getMyDistributor();
//		if(orderInfo.getStatus()==0){
//			orderInfo.setTradeDate(new Date());
//			try {
//				this.shopMemberService.UpdateMemberAfterPay(orderInfo);
//				this.shopOrderInfoService.disPaySuccess(orderInfo);
//	            this.shopOrderInfoService.disTributorAmt(orderInfo);//处理佣金
//			} catch (Exception e) {
//				System.out.println("=====================================+"+e);
//			}finally{
//				System.out.println("=====================================支付未成功，重新支付！！================");
//			}
//		}
//		List<?> orderList = orderInfo.getOrderdetails();
//		if(orderList.size()==1){
//			ShopOrderdetail shopOrderdetail=(ShopOrderdetail)orderList.get(0);
//			String dep=shopOrderdetail.getPro().getProductType().getDePath();
//			String ptId=dep.split("@")[1];
//			ProductType pType=this.productTypeService.getProductTypeByCode(ptId);
//			if(pType.getIsSpecialProType()){
//				form.jsonResult("1");
//				map.put("status", "1");
//			}
//		}
//		form.jsonResult(map);
//		return Page.JSONPage;
//	}
	
	/**
	 * 充值支付
	 * @param form
	 * @return
	 */
	public Page doToPayRecharge(WebForm form){
		String recordId = CommUtil.null2String(form.get("recordId"));
		IntegralRechargeRecord rechargeRecord = this.integralRechargeRecordService.getIntegralRechargeRecord(Long.parseLong(recordId));
		QueryObject qo = new QueryObject();
    	qo.addQuery("obj.type",PayTypeE.WEIXINMPAPI,"=");
    	qo.setPageSize(1);
    	List<PaymentConfig> configs = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
    	if(configs!=null && configs.size()>0){
    		PaymentConfig config = configs.get(0);
    		String reMsg = this.shopPayMentService.rechargePaySubmit(rechargeRecord, config, "");
    		form.addResult("jsStr", reMsg);
    	}
		return new Page("/bcd/wxshop/trading/payjs.html");
	}
	
	/**
	 * 异步充值成功回调
	 * @param form
	 * @return
	 */
	public Page doChargeSuccess(WebForm form){
		String paySign = CommUtil.null2String(form.get("paySign"));
		if(StringUtils.isEmpty(paySign)){
			return Page.JSONPage;
		}
		String recordId = CommUtil.null2String(form.get("recordId"));
		IntegralRechargeRecord rechargeRecord = this.integralRechargeRecordService.getIntegralRechargeRecord(Long.parseLong(recordId));
		
		form.addResult("order", rechargeRecord);
		return Page.JSONPage;
	}
	
	
	/**
	 * 异步充值成功回调
	 * @param form
	 * @return
	 */
//	public Page doChargeSuccessAjax(WebForm form){
//		String paySign = CommUtil.null2String(form.get("paySign"));
//		if(StringUtils.isEmpty(paySign)){
//			return Page.JSONPage;
//		}
//		
//		Map<String,String> map = new HashMap();
//		String recordId = CommUtil.null2String(form.get("recordId"));
//		IntegralRechargeRecord rechargeRecord = this.integralRechargeRecordService.getIntegralRechargeRecord(Long.parseLong(recordId));
//		ShopMember member = rechargeRecord.getMember();
//		Long ruler = this.integralChangeRuleService.getIntegralCashRate();
//		Long money = ruler*(rechargeRecord.getIntegral());
//		if(rechargeRecord.getStatus()==0){
//			rechargeRecord.setTradeDate(new Date());
//			rechargeRecord.setStatus(Short.parseShort("1"));
//			this.integralRechargeRecordService.updateIntegralRechargeRecord(rechargeRecord.getId(), rechargeRecord);
//			IntegralHistory ih = new IntegralHistory();
//			ih.setIntegral(money);
//			ih.setType(2);
//			ih.setUser(member);
//			this.integralHistoryService.addIntegralHistory(ih);
//			member.setTotalIntegral(member.getTotalIntegral()+rechargeRecord.getIntegral());
//			member.setAvailableIntegral(member.getAvailableIntegral()+ rechargeRecord.getIntegral());
//			this.shopMemberService.updateShopMember(member.getId(), member);
//		}
//		
//		map.put("flag", "充值成功！");
//		form.jsonResult(map);
//		return Page.JSONPage;
//	}
//	
	/**
	 * 选择余额支付
	 * @param form
	 * @return
	 */
	public Page doUserbal(WebForm form){
		ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
		String total=CommUtil.null2String(form.get("total"));
		Map<String,String> map = new HashMap<String,String>();
		if(member==null){
			map.put("success", "false");
			map.put("msg", "登录超时，请退出重新登陆");
			form.jsonResult(map);
			pageForExtForm(form);
		}
		member = this.shopMemberService.getShopMember(member.getId());
		Double rAmt=member.getRemainderAmt();
		Double total_=Double.valueOf(total);
		BigDecimal amt = new BigDecimal(Double.toString(rAmt));
		BigDecimal tot = new BigDecimal(Double.toString(total_));
		if(rAmt>=total_){
			Double ye = amt.subtract(tot).doubleValue();//还剩余额
			BigDecimal ye2 = new BigDecimal(ye);
			Double ye3 = ye2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位
			map.put("success", "true");
			map.put("syje", total_.toString());//使用金额
			map.put("ye", ye3.toString());//还剩余额
			map.put("hxje", "0");//还需余额
		}else{
			Double ye = tot.subtract(amt).doubleValue();//还需金额
			BigDecimal ye2 = new BigDecimal(ye);
			Double ye3 = ye2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位
			Double amt3 = amt.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位
			map.put("success", "true");
			map.put("syje", amt3.toString());//使用金额
			map.put("ye", "0");//还剩余额
			map.put("hxje", ye3+"");
		}
		form.jsonResult(map);
		return Page.JSONPage;
	}
	/**
	 * 恢复错误数据
	 * @param form
	 * @return
	 */
	public Page doRecorver(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.code",orderId, "=");
		List<ShopOrderInfo> list = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		if(list != null && list.size() != 0){
			ShopOrderInfo  order = list.get(0);
			if(order != null){
				order.setIsDisAmtEnd(false);
				this.shopOrderInfoService.disTributorAmt(order);
				System.out.println("分配完成！");
			}else{
				System.out.println("分配失败！");
			}
		}
		
		return Page.JSONPage;
	}
	
	/**
	 * 确认收货
	 * @param form
	 * @return
	 */
	public Page doQrsh(WebForm form){
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		String id = CommUtil.null2String(form.get("id"));
		if(user==null){
			this.addError("msg", "登录超时，请重新登录！！！！！！");
			return pageForExtForm(form);
		}
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.valueOf(id));
		if(order.getUser().getId()!=user.getId()){
			this.addError("msg", "你没有权限修改此订单");
			return pageForExtForm(form);
		}
		order.setStatus(3);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		return pageForExtForm(form);
	}
	/**
	 * 累加消费金额（dll非本人不可用）
	 * @return
	 */
	public Page doDsolvecounntconsum(){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.status",0,"!=");
		qo.addQuery("obj.status",-1,"!=");
		qo.addQuery("obj.status",5,"!=");
		qo.setPageSize(-1);
		List<ShopOrderInfo> list = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
		for(ShopOrderInfo info:list){
			ShopMember member = info.getUser();
			if(member != null){
				member.setConsumptionAmount(member.getConsumptionAmount() + info.getGross_price());
			}
			this.shopMemberService.updateShopMember(member.getId(),member);
		}
		return null;
	}
	/**
	 * 跳转进行评价页面
	 * @param form
	 * @return
	 */
	public Page doGoShopDiscuss(WebForm form){
		String orderDetailId = CommUtil.null2String(form.get("detailId"));
		if(!orderDetailId.equals("")){
			ShopOrderdetail detail = this.shopOrderdetailService.getShopOrderdetail(Long.parseLong(orderDetailId));
			ShopDiscuss discuss = detail.getShopDiscuss();
			if(discuss != null){
				System.out.println("已经评论过，不能多次评价。orderDetailID:"+orderDetailId);
				return error(form,"已经评论过，不能多次评价。");
			}else{
				form.addResult("detail", detail);
				return new Page("/bcd/wxshop/trading/discuss.html");
			}
		}else{
			return error(form,"detailID is null系统错误，请联系管理员。");
		}
		
	}
	
	/**
	 * 跳转进行评价页面
	 * @param form
	 * @return
	 */
	public Page doAddGoShopDiscuss(WebForm form){
		String orderDetailId = CommUtil.null2String(form.get("detailId"));
		String starts = CommUtil.null2String(form.get("starts"));
		String content = CommUtil.null2String(form.get("content"));
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		if(user == null){
			return error(form,"用户登录超时，请刷新后重试！");
		}
		if(!orderDetailId.equals("")){
			ShopOrderdetail detail = this.shopOrderdetailService.getShopOrderdetail(Long.parseLong(orderDetailId));

			ShopDiscuss discuss = detail.getShopDiscuss();
			if(discuss != null){
				System.out.println("已经评论过，不能多次评价。orderDetailID:"+orderDetailId);
				return error(form,"已经评论过，不能多次评价");
			}else{
				discuss = new ShopDiscuss();
				discuss.setContent(content);
				discuss.setIsShow(true);
				discuss.setPro(detail.getPro());
				discuss.setShopOrderdetail(detail);
				discuss.setStartdis(Integer.parseInt(starts));
				discuss.setUser(user);
				this.shopDiscussService.addShopDiscuss(discuss);
				detail.setIsDiscuss(true);
				this.shopOrderdetailService.updateShopOrderdetail(detail.getId(),detail);
			}
		}else{
			System.out.println("orderDetailID为空:"+orderDetailId);
			return error(form,"数据出错，请联系管理员");
		}
		return DiscoShopUtil.goPage("/wxShopMemberCenter.java?cmd=toOrder");
	}
	
	
	
	
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}
	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}
	public IShopAddressService getShopAddressService() {
		return shopAddressService;
	}
	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}
	public ISystemRegionService getSystemRegionService() {
		return systemRegionService;
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
	public void setShopOrderdetailService(
			IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}
	public IPaymentConfigService getPaymentConfigService() {
		return paymentConfigService;
	}
	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}
	public IShopPayMentService getShopPayMentService() {
		return shopPayMentService;
	}
	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}
	public IShopProductService getShopProductService() {
		return shopProductService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	public IShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}
	public void setShoppingCartService(IShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}
	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
	}
	public void setCommissionConfigService(
			ICommissionConfigService commissionConfigService) {
		this.commissionConfigService = commissionConfigService;
	}
	public IShopSystemConfigService getShopSystemConfigService() {
		return shopSystemConfigService;
	}
	public void setShopSystemConfigService(
			IShopSystemConfigService shopSystemConfigService) {
		this.shopSystemConfigService = shopSystemConfigService;
	}
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	public ICommissionDetailService getCommissionDetailService() {
		return commissionDetailService;
	}
	public void setCommissionDetailService(
			ICommissionDetailService commissionDetailService) {
		this.commissionDetailService = commissionDetailService;
	}
	public IDeliveryRuleService getDeliveryService() {
		return deliveryService;
	}
	public void setDeliveryService(IDeliveryRuleService deliveryService) {
		this.deliveryService = deliveryService;
	}
	public IRushBuyRegularService getRegularService() {
		return regularService;
	}
	public void setRegularService(IRushBuyRegularService regularService) {
		this.regularService = regularService;
	}
	public IRushBuyRecordService getRecordService() {
		return recordService;
	}
	public void setRecordService(IRushBuyRecordService recordService) {
		this.recordService = recordService;
	}
	public IIntegralRechargeRecordService getIntegralRechargeRecordService() {
		return integralRechargeRecordService;
	}
	public void setIntegralRechargeRecordService(
			IIntegralRechargeRecordService integralRechargeRecordService) {
		this.integralRechargeRecordService = integralRechargeRecordService;
	}
	public ICommissionConfigService getCommissionConfigService() {
		return commissionConfigService;
	}
	public IProductTypeService getProductTypeService() {
		return productTypeService;
	}
	public IIntegralChangeRuleService getIntegralChangeRuleService() {
		return integralChangeRuleService;
	}
	public void setIntegralChangeRuleService(
			IIntegralChangeRuleService integralChangeRuleService) {
		this.integralChangeRuleService = integralChangeRuleService;
	}
	public IIntegralHistoryService getIntegralHistoryService() {
		return integralHistoryService;
	}
	public void setIntegralHistoryService(
			IIntegralHistoryService integralHistoryService) {
		this.integralHistoryService = integralHistoryService;
	}
	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}
	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}
	public void setShopSinceSomeService(IShopSinceSomeService shopSinceSomeService) {
		this.shopSinceSomeService = shopSinceSomeService;
	}
	public IShopDiscussService getShopDiscussService() {
		return shopDiscussService;
	}
	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}
	
}
