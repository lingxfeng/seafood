package com.eastinno.otransos.seafood.spokesman.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.platform.weixin.mvc.WeixinBaseAction;
import com.eastinno.otransos.seafood.core.action.WxShopBaseAction;
import com.eastinno.otransos.seafood.core.domain.ShopSystemConfig;
import com.eastinno.otransos.seafood.core.service.IShopSystemConfigService;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.distribu.service.IShopDistributorService;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.domain.ShopSpec;
import com.eastinno.otransos.seafood.droduct.query.ShopProductQuery;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.droduct.service.IShopSpecService;
import com.eastinno.otransos.seafood.promotions.domain.CustomCoupon;
import com.eastinno.otransos.seafood.spokesman.domain.PaySpecialAllowance;
import com.eastinno.otransos.seafood.spokesman.domain.Spokesman;
import com.eastinno.otransos.seafood.spokesman.domain.SpokesmanProduct;
import com.eastinno.otransos.seafood.spokesman.query.SpokesmanProductQuery;
import com.eastinno.otransos.seafood.spokesman.service.IPaySpecialAllowanceService;
import com.eastinno.otransos.seafood.spokesman.service.IRestitutionService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanProductService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanRatingService;
import com.eastinno.otransos.seafood.spokesman.service.ISpokesmanService;
import com.eastinno.otransos.seafood.spokesman.service.ISubsidyService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.seafood.trade.service.IShopPayMentService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopAddress;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopAddressService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;
import com.eastinno.otransos.seafood.util.DiscoShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 
 * @author 
 */
@Action
public class WxSpokesmanProductAction extends WxShopBaseAction{
    @Inject
    private ISpokesmanProductService service;
    @Inject
    private ISpokesmanService spokesmanservice;
    @Inject
    private ISpokesmanRatingService spokesmanRatingservice;
    @Inject
    private IShopSystemConfigService shopSystemConfigService;
    @Inject
    private IShopProductService shopProductService;
    @Inject
    private IShopSpecService shopSpecService;
    @Inject
    private IShopAddressService shopAddressService;
    @Inject
    private IShopDistributorService shopDistributorService;
    @Inject
    private IShopOrderInfoService shopOrderInfoService;
    @Inject
    private IShopOrderdetailService shopOrderdetailService;
    @Inject
    private IPaymentConfigService paymentConfigService;
    @Inject
    private IShopPayMentService shopPayMentService;
    @Inject
    private ISubsidyService subsidyService;
    @Inject
    private IRestitutionService restitutionService;
    @Inject
    private IPaySpecialAllowanceService paySpecialAllowanceService;
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doProductList(WebForm form) {
//    	ShopMember member = getMemberBySession();
//    	if(member != null){
//    		Spokesman sm = member.getMySpokesman();
//    	}
//    	if(){}else{
//    		
//    	}
    	String productname = CommUtil.null2String(form.get("productname"));
    	QueryObject qo = new QueryObject();
    	if(StringUtils.hasText(productname)){
        	qo.addQuery("obj.product.name like '%"+productname+"%'");
        }
        IPageList pl = this.service.getSpokesmanProductBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl.getResult());
        return new Page("/bcd/wxshop/spokesman/productlist.html");
    }
    /**
     * 代言商品详情页面
     * 
     * @param form
     */
    public Page doProductDetail(WebForm form) {
    	String id = CommUtil.null2String(form.get("id"));
    	/**
    	 * 获取判断当前用户是否已成为代言人
    	 */
    	ShopMember member = getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}else{
			Spokesman sm = member.getMySpokesman();
			if(sm != null){
				form.addResult("identity","1");
			}else{
				form.addResult("identity","0");
			}
			SpokesmanProduct sp = this.service.getSpokesmanProduct(Long.parseLong(id));
	    	int totalmonth = sp.getTotalMonths();
	    	form.addResult("totalmonth", totalmonth);
	        form.addResult("sp", sp);
		}
		
    	
        
        return new Page("/bcd/wxshop/spokesman/productDetails.html");
    }

    /**
     * 检查所输id是否存在
     * 
     * @param form
     */
    public Page doCheckId(WebForm form) {
    	String pspokesmanid = CommUtil.null2String(form.get("pspokesmanid"));
    	QueryObject qo = new QueryObject();
    	if(!"".equals(pspokesmanid)){
    		qo.addQuery("obj.id",Long.parseLong(pspokesmanid),"=");
        	List<Spokesman> list = this.spokesmanservice.getSpokesmanBy(qo).getResult();
        	Map map = new HashMap();
        	if(list != null && list.size() != 0){
        		map.put("msg",1);//数据存在
        		Spokesman sman = list.get(0);
        		Long smanid = sman.getId();
        		String smanname = sman.getMember().getNickname();
        		map.put("id",smanid);
        		map.put("name",smanname);
        	}else{
        		map.put("msg",2);//数据不存在
        	}
        	form.jsonResult(map);
    	}
    	
		return Page.JSONPage;
    }
    

    
    /**
	 * 生成推荐关系
	 */
	public Page doCreateRelation(WebForm form){
		//生成推荐关系
		ShopMember member = getShopMember(form);
		if(member==null){
			return error(form,"操作超时或无法获取用户信息");
		}
    	String pspokesmanid = CommUtil.null2String(form.get("pid"));
    	String identity = "0";
    	if(!"".equals(pspokesmanid)){
    		Spokesman sm = this.spokesmanservice.getSpokesman(Long.parseLong(pspokesmanid));
    		Spokesman spokesman = new Spokesman();
    		spokesman.setPspokesman(sm);
    		spokesman.setMember(member);
    		this.spokesmanservice.addSpokesman(spokesman);
    		spokesman.setDePath(sm.getDePath() + "@" +spokesman.getId());
    		spokesman.setSpokesmanRating(this.spokesmanRatingservice.judgeRating(spokesman));
    		this.spokesmanservice.updateSpokesman(spokesman.getId(), spokesman);
    	}else{
    		Spokesman sman = member.getMySpokesman();
    		if(sman == null){
    			Spokesman spokesman = new Spokesman();
    			spokesman.setMember(member);
    			this.spokesmanservice.addSpokesman(spokesman);
    			spokesman.setDePath("@" +spokesman.getId());
    			spokesman.setSpokesmanRating(this.spokesmanRatingservice.judgeRating(spokesman));
        		this.spokesmanservice.updateSpokesman(spokesman.getId(), spokesman);
    		}else{
    			if(sman.getAgreeProtocal() == 1){
    				identity = "1";
        		}
    		}
    		
    	}
    	//购买跳转页面
		String proId = CommUtil.null2String(form.get("proId"));//商品ID
		String ggId = CommUtil.null2String(form.get("ggId"));//规格ID
		String payProNum = CommUtil.null2String(form.get("payProNum"));//商品数量
		String type = CommUtil.null2String(form.get("type"));//代言订单类型
		String urlstr = "/wxSpokesmanProduct.java?cmd=beforeCreateOrder&proId="+proId+"&ggId="+ggId+"&payProNum="+payProNum+"&type="+type+"&identity="+identity;
    	return DiscoShopUtil.goPage(urlstr);
		
	}
	/**
	 * 生成订单跳转页
	 * @param form
	 * @return
	 */
	public Page doBeforeCreateOrder(WebForm form){
				ShopMember member = getShopMember(form);
				if(member==null){
					return error(form,"操作超时或无法获取用户信息");
				}
				//购买跳转页面
				String pid = CommUtil.null2String(form.get("proId"));//商品ID
				String specid = CommUtil.null2String(form.get("ggId"));//规格ID
				String numstr = CommUtil.null2String(form.get("payProNum"));//商品数量
				String addrid = CommUtil.null2String(form.get("addrid"));
				String type = CommUtil.null2String(form.get("type"));//代言订单类型
				String url = CommUtil.null2String(form.get("url"));
				String identity = CommUtil.null2String(form.get("identity"));
				form.addResult("identity", identity);

				ShopSystemConfig sc = this.shopSystemConfigService.getSystemConfig();
				Double freight = 0D;
				if(sc != null){
					sc.getFreight();
				}
				form.addResult("freight", freight);
				ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
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
				qo.addQuery("obj.user",member, "=");
				IPageList pl = this.shopAddressService.getShopAddressBy(qo);
				if(pl.getResult()!=null){
					ShopAddress addrd = (ShopAddress) pl.getResult().get(0); 
					form.addResult("addrd", addrd);
				}
				form.addResult("type",type);
				return new Page("/bcd/wxshop/spokesman/payorder.html");
	}
	/**
	 * 生成订单
	 * 
	 */
	public Page doCreateOrder(WebForm form){
		
		ShopAddress sa=null;
		String addrid = CommUtil.null2String(form.get("addrid"));
		String msg_self = CommUtil.null2String(form.get("msg_self"));
		String type = CommUtil.null2String(form.get("type"));
		String agreeProtocal = CommUtil.null2String(form.get("agreeProtocal"));
		form.addResult("type",type);
		if(!"".equals(addrid)){
			sa = this.shopAddressService.getShopAddress(Long.parseLong(addrid));
		}
		ShopSpec spec = null;
		Double singleprice= 0.0;
		Double totalprice = 0.0;
		ShopOrderInfo order = form.toPo(ShopOrderInfo.class);
		ShopMember user = this.getShopMember(form);
	
		/**
		 * 更改代言人信息
		 */
		if(!"".equals(agreeProtocal)){
			Spokesman sman = user.getMySpokesman();
			sman.setAgreeProtocal(Short.parseShort(agreeProtocal));
			this.spokesmanservice.updateSpokesman(sman.getId(), sman);
		}
		
		/**
		 * 更新订单信息
		 */
		order.setUser(user);
		order.setAddr(sa);
		order.setMsg_self(msg_self);
		order.setCode(new Date().getTime()+"");
		order.setIsSpokesman(Short.parseShort(type));
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
		if(!"".equals(pid)){
			Double allAmt = 0.0;
			ShopProduct pro = this.shopProductService.getShopProduct(Long.parseLong(pid));
			if(!"".equals(specid)){
				spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
				singleprice = spec.getAmt();
				totalprice = spec.getAmt()*num;
			}else{
				singleprice = pro.getAmt();
				totalprice = pro.getAmt()*num;
			}
			String code = new Date().getTime()+"";
			createOrderDetail(pro,num,user,spec,code,singleprice,totalprice,order,Short.parseShort(type));
			gross_price_pro += totalprice;
			
			//更新订单中返现状态
			int total=0;
			Float restitution=0F;
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.product.id",pro.getId(),"=");
			 List<SpokesmanProduct> listpro = this.service.getSpokesmanProductBy(qo).getResult();
			 if(listpro != null && listpro.size()!= 0){
				 SpokesmanProduct spokesProduct = listpro.get(0);
				 total = spokesProduct.getTotalMonths();
				 restitution = spokesProduct.getRestitution();
				 order.setTotalMonths(total);
				 order.setRestitution(restitution);
			 }
			 this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		}
		
		Double freight = calculateFreight(order);
		order.setFreight(freight);
		order.setProduct_price(gross_price_pro);
		gross_price = gross_price + gross_price_pro + freight;
		order.setGross_price(gross_price);
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		return DiscoShopUtil.goPage("/wxSpokesmanProduct.java?cmd=orderDetail&orderId="+order.getId());
	}
	/**
	 * 生成订单详情
	 * @param pro
	 * @param num
	 * @param user
	 * @param spec
	 * @param code
	 * @param singleprice
	 * @param totalprice
	 * @param order
	 */
	public void createOrderDetail(ShopProduct pro,int num,ShopMember user,ShopSpec spec,String code,Double singleprice,Double totalprice,ShopOrderInfo order,Short type){
		ShopOrderdetail orderDetail = new ShopOrderdetail();
		orderDetail.setPro(pro);
		orderDetail.setNum(num);
		orderDetail.setUser(user);
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
		this.shopProductService.updateShopProductAfterPay(pro,specid,num);
	}
	/**
	 * 计算运费
	 * @param order
	 * @return
	 */
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
     * 客户订单详情
     * @param form
     * @return
     */
    public Page doOrderDetail(WebForm form){
    	
    	//判断身份
    	HttpSession session = ActionContext.getContext().getSession();
    	ShopMember user = (ShopMember) session.getAttribute("SHOPMEMBER");
		
		String flag = "huiyuan";
		form.addResult("flag", flag);
    	
    	String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
    	CustomCoupon cc = order.getMyCoupon();
    	if(cc!=null){
    		form.addResult("coupon", cc);
    	}
    	form.addResult("order", order);
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.type",PayTypeE.WEIXINMPAPI,"=");
    	qo.setPageSize(1);
    	List<PaymentConfig> configs = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
    	if(configs!=null && configs.size()>0){
    		PaymentConfig config = configs.get(0);
    		String reMsg = this.shopPayMentService.paySubmit(order, config, "");
    		form.addResult("jsStr", reMsg);
    	}
    	return new Page("/bcd/wxshop/spokesman/orderDetails.html");
    }
	/**
	 * 模拟支付成功
	 * @param order
	 * @return
	 */
	public Page doTestSpokesman(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
    	if(order == null){
    		return error(form,"用户数据错误！请刷新后重试！");
    	}
    	ShopMember member = order.getUser();
    	if(member == null){
    		return error(form,"用户数据错误！请刷新后重试！");
    	}
		
		
		//更改代言人状态
		Spokesman sman = member.getMySpokesman();
		if(sman.getStatus() == 0){
			sman.setStatus(Short.parseShort("1"));
			sman.setCreateDate(new Date());
			this.spokesmanservice.updateSpokesman(sman.getId(),sman);
		}
		//计算补贴
		this.subsidyService.calculateSubsidyFirst(order);
		//更改订单信息
		order.setFinishRestitution(Short.parseShort("0"));
		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
		return DiscoShopUtil.goPage("/wxSpokesmanProduct.java?cmd=calculateTeamAccount&orderId="+order.getId());
		
    	
	}
	public Page doCalculateTeamAccount(WebForm form){
		String orderId = CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderId));
    	//团队业绩累计
    	this.spokesmanservice.calculateTeamAccount(order);
    	return new Page("/bcd/wxshop/spokesman/sucsses.html");
	}
	public Page doBatchUpdateRating(WebForm form){
		//更新等级
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.status",Short.parseShort("1"),"=");
    	qo.addQuery("obj.customRating",Short.parseShort("0"),"=");
    	List<Spokesman> smanlist = this.spokesmanservice.getSpokesmanBy(qo).getResult();
    	if(smanlist != null && smanlist.size() != 0){
    		for(Spokesman sman: smanlist){
    			sman.setSpokesmanRating(this.spokesmanRatingservice.judgeRating(sman));
    			this.spokesmanservice.updateSpokesman(sman.getId(),sman);
    		}
    	}
    	return new Page("/bcd/wxshop/spokesman/sucsses.html");
	}
	
	
	
	
	
	/**
	 * 测试定时任务
	 * @param service
	 */
	public Page doTestTime(WebForm form){
		//返现操作
				QueryObject qo = new QueryObject();
				qo.addQuery("obj.isSpokesman",Short.parseShort("2"),"=");
				qo.addQuery("obj.finishRestitution",Short.parseShort("0"), "=");
				qo.addQuery("obj.status = 1 or obj.status = 2 or obj.status = 3");
				qo.setPageSize(-1);
				List<ShopOrderInfo> list = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
				if(list != null && list.size() != 0){
					for(ShopOrderInfo order:list){
						this.restitutionService.calcuteRestitution(order);
						System.out.println("============返现一次");
					}
				}
				
				//补贴发放操作
				QueryObject qo2 = new QueryObject();
				qo2.addQuery("obj.isSpokesman",Short.parseShort("2"),"=");
				qo2.addQuery("obj.finishSubsidy",Short.parseShort("0"), "=");
				qo.addQuery("obj.status = 1 or obj.status = 2 or obj.status = 3");
				qo2.setPageSize(-1);
				List<ShopOrderInfo> list2 = this.shopOrderInfoService.getShopOrderInfoBy(qo2).getResult();
				if(list != null && list.size() != 0){
					for(ShopOrderInfo order:list2){
						if(order.getIsCalculate() == 1){
							this.subsidyService.disPatchSubsidy(order);
						}else{
							this.subsidyService.calculateSubsidyFirst(order);
							this.subsidyService.disPatchSubsidy(order);
						}
						System.out.println("============补贴一次");
						
					}
				}
				//特殊津贴发放
				int total = this.paySpecialAllowanceService.calculateSpecialAllowance();//总份数 
				Float totalAllowance = 0F;
				QueryObject qo3 = new QueryObject();
				qo3.addQuery("obj.spokesmanRating.rating",Short.parseShort("3"), "=");
				qo3.setPageSize(-1);
				List<Spokesman> list3 = this.spokesmanservice.getSpokesmanBy(qo3).getResult();
				if(list3 != null && list3.size() != 0){
					for(Spokesman sman:list3){
						int num = 0; 
						num = this.paySpecialAllowanceService.calculateSpecialAllowancepart(sman);
						if(num != 0){
							PaySpecialAllowance pa = new PaySpecialAllowance();
							pa.setPartNum(num);
							pa.setSpokesman(sman);
							pa.setSpecialAllowance(totalAllowance/total * num);
							this.paySpecialAllowanceService.addPaySpecialAllowance(pa);
						}
					}
				}
				
		return new Page("/bcd/wxshop/spokesman/sucsses.html");		
	}
    public void setService(ISpokesmanProductService service) {
        this.service = service;
    }
	public ISpokesmanService getSpokesmanservice() {
		return spokesmanservice;
	}
	public void setSpokesmanservice(ISpokesmanService spokesmanservice) {
		this.spokesmanservice = spokesmanservice;
	}
	public ISpokesmanRatingService getSpokesmanRatingservice() {
		return spokesmanRatingservice;
	}
	public void setSpokesmanRatingservice(
			ISpokesmanRatingService spokesmanRatingservice) {
		this.spokesmanRatingservice = spokesmanRatingservice;
	}
	public IShopSystemConfigService getShopSystemConfigService() {
		return shopSystemConfigService;
	}
	public void setShopSystemConfigService(
			IShopSystemConfigService shopSystemConfigService) {
		this.shopSystemConfigService = shopSystemConfigService;
	}
	public IShopProductService getShopProductService() {
		return shopProductService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
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
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
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
	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}
	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}
	public ISpokesmanProductService getService() {
		return service;
	}
	public IRestitutionService getRestitutionService() {
		return restitutionService;
	}
	public void setRestitutionService(IRestitutionService restitutionService) {
		this.restitutionService = restitutionService;
	}
	public IPaySpecialAllowanceService getPaySpecialAllowanceService() {
		return paySpecialAllowanceService;
	}
	public void setPaySpecialAllowanceService(
			IPaySpecialAllowanceService paySpecialAllowanceService) {
		this.paySpecialAllowanceService = paySpecialAllowanceService;
	}
	
	
}
