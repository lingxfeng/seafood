package com.eastinno.otransos.shop.promotions.action;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.core.domain.ShopSystemConfig;
import com.eastinno.otransos.shop.core.service.ICusUploadFileService;
import com.eastinno.otransos.shop.core.service.IShopSystemConfigService;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.domain.ShopSpec;
import com.eastinno.otransos.shop.droduct.service.IProductTypeService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.IShopSpecService;
import com.eastinno.otransos.shop.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRecord;
import com.eastinno.otransos.shop.promotions.domain.RushBuyRegular;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeRegular;
import com.eastinno.otransos.shop.promotions.domain.SweepstakeSystemConfig;
import com.eastinno.otransos.shop.promotions.domain.SweepstakesRecord;
import com.eastinno.otransos.shop.promotions.service.IIntegralChangeRuleService;
import com.eastinno.otransos.shop.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRecordService;
import com.eastinno.otransos.shop.promotions.service.IRushBuyRegularService;
import com.eastinno.otransos.shop.promotions.service.ISweepstakeRegularService;
import com.eastinno.otransos.shop.promotions.service.ISweepstakeSystemConfigService;
import com.eastinno.otransos.shop.promotions.service.ISweepstakesRecordService;
import com.eastinno.otransos.shop.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.shop.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.shop.usercenter.domain.ShopAddress;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopAddressService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.ShopUtil;
import com.eastinno.otransos.shop.util.TokenUtil;
import com.eastinno.otransos.shop.util.formatUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 
 * @author  dll
 */
@Action
public class WxShopPromotionsAction extends WxShopBaseAction{
    @Inject
    private ISweepstakeRegularService service;
    @Inject
    private ISweepstakesRecordService sweepstakesRecordService;
    @Inject
    private ICusUploadFileService cusUploadFileService;
    @Inject
    private ISweepstakeSystemConfigService sweepstakeSystemConfigService;
    @Inject
    private IShopAddressService addressService;
    @Inject
    private IShopMemberService shopMemberService;
    @Inject
    private IRushBuyRecordService recordService;
	@Inject
    private IRushBuyRegularService regularService;
	@Inject
	private IShopProductService productService;
	@Inject
	private IShopSpecService shopSpecService;
	@Inject
	private ShopUtil shopUtil;
	@Inject
	private IIntegralChangeRuleService integralChangeRuleService;
	@Inject
	private IIntegralHistoryService integralHistoryService;
	@Inject
	private IIntegralRechargeRecordService integralRechargeRecordService;
	@Inject
	private IShopDistributorService shopDistributorService;
	@Inject
	private IShopSystemConfigService shopSystemConfigService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IShopOrderdetailService shopOrderdetailService;
	
	 /**
     * 抽奖入口
     * 
     * @param form
     */
    public Page doTopage(WebForm form) {
    	SweepstakeSystemConfig sc=null;
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		sc = list.get(0);	
    	}
    	form.addResult("obj", sc);
    	if(sc.getStatus()==0){
    		return error(form,"当前未进行抽奖活动，敬请期待！");
    	}
    	QueryObject qo2 = new QueryObject();
    	qo2.addQuery("obj.status",Short.parseShort("0"), "!=");
    	qo.setPageSize(10);
    	qo.setOrderBy("createTime");
    	qo.setOrderType("desc");
    	List<SweepstakesRecord> list2 = this.sweepstakesRecordService.getSweepstakesRecordBy(qo2).getResult();
    	form.addResult("winner", list2);
    	
        return new Page("/bcd/wxshop/promotions/sweepstakes/sweepstake.html");
    }
    /**
     * 抽奖方法
     * 
     * @param form
     */
    public Page doSweepstake(WebForm form) {
    	Map map = new HashMap<>();
    	ShopMember member =  getShopMember(form);
    	Long aviala = member.getAvailableIntegral();
    	/**
    	 *查询抽奖全局配置 
    	 *
    	 */
    	int minbase=0;
    	SweepstakeSystemConfig sc=null;
    	QueryObject qo = new QueryObject();
    	List<SweepstakeSystemConfig> list = this.sweepstakeSystemConfigService.getSweepstakeSystemConfigBy(qo).getResult();
    	if(list != null && list.size() != 0){
    		sc = list.get(0);
    		minbase = sc.getMinbase();
    	}
    	int cost = sc.getCostInterval();
    	int limit = sc.getCount();
    	if(aviala < cost){
    		map.put("flag",1);
    		form.jsonResult(map);
    	}else{
    		/**
        	 * 检查抽奖次数
        	 */
        	Integer count = this.sweepstakesRecordService.getTodayCount(member);
    		if(count >= limit){
        		map.put("flag",3);
        		form.jsonResult(map);
        		return Page.JSONPage;
        	}else{
    			int position = this.service.checkSweepstake(minbase);
    	    	SweepstakeRegular regular = null;
    	    	QueryObject qo2 = new QueryObject();
    	    	qo.addQuery("obj.position",position,"=");
    	    	List<SweepstakeRegular> list2 = this.service.getSweepstakeRegularBy(qo).getResult();
    	    	if(list2 != null && list2.size() != 0 ){
    	    		regular = list2.get(0);
    		    	SweepstakesRecord sr = new SweepstakesRecord();
    		    	sr.setStatus(regular.getStatus());
    		    	sr.setUser(member);
    		    	sr.setCost(cost);
    		    	sr.setImgPaths(regular.getImgPaths());
    		    	if(regular.getStatus() == 1){
    		    		sr.setProductname(regular.getProductname());
    		    	}else if(regular.getStatus() == 3){
    		    		sr.setIntegal(regular.getIntegal());
    		    	}
    		    	
    		    	if(regular.getStatus() == 3){
    		    		member.setAvailableIntegral((long)(member.getAvailableIntegral()-cost + regular.getIntegal()));//抽一次
    		    		member.setTotalIntegral((long)(member.getTotalIntegral() + regular.getIntegal()));
    		    		sr.setIsDispatch(Short.parseShort("1"));
    		    	}else{
    		    		member.setAvailableIntegral((long)(member.getAvailableIntegral()-cost));//抽一次
    		    	}
    		    	this.sweepstakesRecordService.addSweepstakesRecord(sr);
    		    	this.shopMemberService.updateShopMember(member.getId(),member);
    		    	//需生成积分使用记录
    		    	IntegralHistory ih = new IntegralHistory();
    		    	ih.setType(5);
    		    	ih.setDescription("抽奖消费");
    		    	ih.setIntegral((long)-cost);
    		    	ih.setUser(member);
    		    	this.integralHistoryService.addIntegralHistory(ih);	
    		    	if(regular.getStatus() == 3){
    		    		IntegralHistory ih2 = new IntegralHistory();
    			    	ih2.setType(7);
    			    	ih2.setDescription("抽奖获得");
    			    	ih2.setIntegral((long)regular.getIntegal());
    			    	ih2.setUser(member);
    			    	this.integralHistoryService.addIntegralHistory(ih2);	
    		    	}
    		    	int angle = this.service.returnangle(position);
    		    	map.put("position", position);
    		    	map.put("angle", angle);
    		    	map.put("name", regular.getName());
    		    	map.put("productname", regular.getProductname());
    		    	map.put("type", regular.getStatus());
    		    	map.put("integal", regular.getIntegal());
    		    	form.jsonResult(map);
    	    	}
    		}
    	}
		return Page.JSONPage;
    }
	
	 /**
     * 客户端抽奖记录列表页面
     * 
     * @param form
     */
    public Page doCustomList(WebForm form) {
    	ShopMember member = getShopMember(form);
    	if(member == null){
    		return error(form,"操作超时或无法获取用户信息，请刷新后重试");
    	}
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.addQuery("obj.user.id",member.getId(), "=");
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        qo.setPageSize(-1);
        IPageList pl = this.sweepstakesRecordService.getSweepstakesRecordBy(qo);
        form.addResult("pl", pl);
        return new Page("/bcd/wxshop/promotions/sweepstakes/sweepstakeRecord.html");
    }
    
    /**
     * 选择收货地址列表页面
     * 
     * @param form
     */
    public Page doAddrList(WebForm form) {
    	String id= CommUtil.null2String(form.get("id"));
    	form.addResult("id", id);
    	ShopMember member = getShopMember(form);
    	if(member == null){
    		return error(form,"操作超时或无法获取用户信息，请刷新后重试");
    	}
		QueryObject qo = new QueryObject();		
		qo.addQuery("obj.user.id", member.getId(), "=");
		qo.setPageSize(-1);
		IPageList addressList = this.addressService.getShopAddressBy(qo);
        form.addResult("addressList", addressList.getResult());
        return new Page("/bcd/wxshop/promotions/sweepstakes/addressList.html");
    }
    /**
     * 选择收货地址列表页面
     * 
     * @param form
     */
    public Page doFinish(WebForm form) {
    	String id= CommUtil.null2String(form.get("id"));
    	String addrid= CommUtil.null2String(form.get("addrid"));
    	SweepstakesRecord sr = this.sweepstakesRecordService.getSweepstakesRecord(Long.parseLong(id));
    	ShopAddress sa = this.addressService.getShopAddress(Long.parseLong(addrid));
    	sr.setAddress(sa);
    	this.sweepstakesRecordService.updateSweepstakesRecord(sr.getId(), sr);
    	return go("customList");
    }
        
    /**
     * 秒杀活动列表页面
     * 
     * @param form
     */
    public Page doSecKillList(WebForm form) {
        IPageList pageList = this.regularService.getAllSecKillRegularForHome();
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/wxshop/promotions/seckill/seckillList.html");
    }
    
    /**
     * 
     * @param form
     */
    public Page doToSecKillDetail(WebForm form) {
    	String regularId=CommUtil.null2String(form.get("regularId"));
    	RushBuyRegular regular = this.regularService.getRushBuyRegular(Long.parseLong(regularId));
    	form.addResult("regular", regular);
    	
    	//向volecity模板中传入商品信息
    	this.addResultForSKDetail(form);
    	
        return new Page("/bcd/wxshop/promotions/seckill/seckillDetail.html");
    }
    
    
    
	/**
     * 秒杀-请求是否可以进入确认订单页
     * @param form
     * @return
     */
    public Page doRequestSecKillRight(WebForm form) {		
		ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}		
		Long regularId = Long.parseLong(form.get("regularId").toString());
		RushBuyRegular regular = this.regularService.getRushBuyRegular(regularId);
		String secKillListUrl = "<br /><a style=\"color:red;\" href=\"/wxShopPromotions.java?cmd=secKillList\">查看其他秒杀活动</a>";
		//活动状态
		if(!regular.getState().equals("start")){
			return this.error(form, "请求的活动还没有开始！"+secKillListUrl);
		}
		//库存数量
		if(regular.getPro().getInventory().compareTo(0) <= 0){
			return this.error(form, "该商品已经售罄！"+secKillListUrl);
		}
		//抢购数量
		IPageList list = this.recordService.getAllAvailableSecKillRecordByRegular(regular);
		if(list.getRowCount() > regular.getBuyNum()*2){
			return this.error(form, "目前系统中抢购的人数较多，您可能没有机会了！"+secKillListUrl);
		}
		//有秒杀资格添加秒杀资格记录
		if(this.setRecord(member, regular, form) == null){
			return this.error(form, "您本次抢购失败！"+secKillListUrl);
		}		
		//有秒杀资格，进入下单页
		String path = this.getActivityProUrl(regular, form);
		
		form.set("proId", regular.getPro().getId());
		return this.beforeCreateOrder(form);
//		this.forwardTo(path);
//		return this.go("beforeCreateOrder&proId="+regular.getPro().getId()+"&payProNum="+form.get("payProNum"), "wxShopTrade");
//		return Page.nullPage;
	}
    
    /**
     * 限时抢购活动列表页面
     * 
     * @param form
     */
    public Page doTimeLimitList(WebForm form) {
        IPageList pageList = this.regularService.getAllTimeLimitRegularForHome();
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/wxshop/promotions/timelimit/timelimitList.html");
    }
    
    /**
     * 
     * @param form
     */
    public Page doToTimeLimitDetail(WebForm form) {
    	String regularId=CommUtil.null2String(form.get("regularId"));
    	RushBuyRegular regular = this.regularService.getRushBuyRegular(Long.parseLong(regularId));
    	form.addResult("regular", regular);
    	
    	//向volecity模板中传入商品信息
    	this.addResultForSKDetail(form);
    	
        return new Page("/bcd/wxshop/promotions/timelimit/timelimitDetail.html");
    }
    
    
    
	/**
     * 限时抢购-请求是否可以进入确认订单页
     * @param form
     * @return
     */
    public Page doRequestTimeLimitRight(WebForm form) {		
		ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}		
		Long regularId = Long.parseLong(form.get("regularId").toString());
		RushBuyRegular regular = this.regularService.getRushBuyRegular(regularId);
		String timeLimitListUrl = "<br /><a style=\"color:red;\" href=\"/wxShopPromotions.java?cmd=timeLimitList\">查看其他限时抢购活动</a>";
		//活动状态
		if(!regular.getState().equals("start")){
			return this.error(form, "请求的活动还没有开始！"+timeLimitListUrl);
		}
		//库存数量
		if(regular.getPro().getInventory().compareTo(0) <= 0){
			return this.error(form, "该商品已经售罄！"+timeLimitListUrl);
		}
		//抢购数量
		IPageList list = this.recordService.getAllAvailableTimeLimitRecordByRegular(regular);
		if(list.getRowCount() > regular.getBuyNum()*2){
			return this.error(form, "目前系统中抢购的人数较多，您可能没有机会了！"+timeLimitListUrl);
		}
		//有限时抢购资格添加限时抢购资格记录
		if(this.setRecord(member, regular, form) == null){
			return this.error(form, "您本次抢购失败！"+timeLimitListUrl);
		}
		
		//有秒杀资格，进入下单页
		String path = this.getActivityProUrl(regular, form);
		form.set("proId", regular.getPro().getId());
		return this.beforeCreateOrder(form);
//		this.forwardTo(path);
//		return this.go("beforeCreateOrder&proId="+regular.getPro().getId()+"&payProNum="+form.get("payProNum"), "wxShopTrade");
//		return Page.nullPage;				
	}
    
    public Page beforeCreateOrder(WebForm form){
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
		ShopProduct pro = this.productService.getShopProduct(Long.parseLong(pid));
		/**----------------cl----------------**/
		String dep=pro.getProductType().getDePath();
		String ptId=dep.split("@")[1];
		ProductType pType=this.productTypeService.getProductTypeByCode(ptId);
		if(pType.getIsSpecialProType()){
			if(user.getDisType()==0){
				QueryObject qo = new QueryObject();
				qo.addQuery("obj.orderInfo.user", user, "=");
				qo.addQuery("obj.pro.productType.dePath", pType.getDePath()+"%", "like");
				qo.addQuery("obj.status > 0");
				List<?> list=this.shopOrderdetailService.getShopOrderdetailBy(qo).getResult();
				if(list!=null){
					return error(form, "你已经买过此类商品，不需要再次购买");
				}
			}else{
				return error(form, "您是店铺，无需购买此类产品");
			}
		}
		/**----------------cl----------------**/
		if(!"".equals(specid)){
			ShopSpec spec = this.shopSpecService.getShopSpec(Long.parseLong(specid));
			form.addResult("spec",spec);
		}
		if(!"".equals(addrid)){
			ShopAddress addr = this.addressService.getShopAddress(Long.parseLong(addrid));
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
		IPageList pl = this.addressService.getShopAddressBy(qo);
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
		
		return new Page("/bcd/wxshop/trading/payorder.html");
	}
    
    /**
     * 服务器端秒杀商品下单页
     * @param regular
     * @param form
     * @return
     */
    public String getActivityProUrl(RushBuyRegular regular, WebForm form){
    	String result = "";
    	result += "wxShopTrade.java?cmd=beforeCreateOrder&proId="+regular.getPro().getId()+"&payProNum="+form.get("payProNum");
    	return result;
    }
    
    /**
     * 服务器端跳转
     * @param path
     * @return
     */
    private void forwardTo(String path){    	
    	HttpServletRequest request = ActionContext.getContext().getRequest();
    	HttpServletResponse response = ActionContext.getContext().getResponse();
    	try {
			request.getRequestDispatcher(path).include(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
     * 创建进入秒杀 和 限时抢购 系统的资格记录
     * @param member
     * @param regular
     * @param form
     * @return
     */
    private RushBuyRecord setRecord(ShopMember member, RushBuyRegular regular, WebForm form){
    	RushBuyRecord record = null;
    	if(regular.getActivityType() == 0){
    		QueryObject qo = new QueryObject();
    		qo.addQuery("obj.regular.activityType", 0, "=");
    		qo.addQuery("obj.regular.id", regular.getId(), "=");
    		qo.addQuery("obj.member.id", member.getId(), "=");
    		IPageList pageList = this.recordService.getAllSecKillRecordByQO(qo);
    		if(pageList!=null && pageList.getRowCount()>0){
    			record = (RushBuyRecord)pageList.getResult().get(0);
    		}
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
     * 积分充值选择页面
     * @param member
     * @return
     */
    public Page doChooseType(WebForm form){
    	ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}
		if(member.getDisType() != 0){
			form.addResult("commission", member.getMyDistributor().getDisCommission());
		}	
		Long totalIntegral = member.getTotalIntegral();
		Long availableIntegral = member.getAvailableIntegral();
		Long ruler = this.integralChangeRuleService.getIntegralCashRate();
		Double money = member.getRemainderAmt();
		form.addResult("totalIntegral", totalIntegral);
		form.addResult("availableIntegral", availableIntegral);
		form.addResult("money", money);
		form.addResult("ruler", ruler);
    	return new Page("/bcd/wxshop/promotions/integral/integralChooseType.html");
    }
    /**
     * 积分充值
     * @param member
     * @return
     */
    public Page doChargeIntegral(WebForm form){
    	ShopMember member = this.getShopMember(form);
		if(member == null){
			return this.error(form, "当前用户登录超时，请关闭浏览窗口，重新进入系统！");
		}	
		String integral=CommUtil.null2String(form.get("integral"));		
		String type = CommUtil.null2String(form.get("type"));

		
		Long ruler = this.integralChangeRuleService.getIntegralCashRate();
		Long money = ruler*(Long.parseLong(integral));
		
		IntegralRechargeRecord irr = new IntegralRechargeRecord();
		irr.setCode(new Date() +"");
		irr.setGross_price((double)money);
		irr.setIntegral(Long.parseLong(integral));
		irr.setType((short)(Short.parseShort(type)-1));
		irr.setMember(member);
		if("1".equals(type)){
			
			//后台判断用户余额
			Double remaind = member.getRemainderAmt();
			if(remaind < money){
				return new Page("/bcd/wxshop/error.html");
			}
			member.setRemainderAmt(member.getRemainderAmt()-money);
			member.setTotalIntegral(member.getTotalIntegral()+Long.parseLong(integral));
			member.setAvailableIntegral(member.getAvailableIntegral()+ Long.parseLong(integral));			
			this.shopMemberService.updateShopMember(member.getId(), member);
			IntegralHistory ih = new IntegralHistory();
			ih.setIntegral(money);
			ih.setType(2);
			ih.setUser(member);
			this.integralHistoryService.addIntegralHistory(ih);
			irr.setStatus(Short.parseShort("1"));
			this.integralRechargeRecordService.addIntegralRechargeRecord(irr);
		}else if("2".equals(type)){
			this.integralRechargeRecordService.addIntegralRechargeRecord(irr);
			form.addResult("record", irr);
			form.addResult("member", member);
	    	return new Page("/bcd/wxshop/promotions/integral/payTypeConfirm.html");
		}
		form.addResult("order", irr);
		return new Page("/trade/paySuccess.html");
    }
	public ISweepstakeRegularService getService() {
		return service;
	}
	public void setService(ISweepstakeRegularService service) {
		this.service = service;
	}
	public ISweepstakesRecordService getSweepstakesRecordService() {
		return sweepstakesRecordService;
	}
	public void setSweepstakesRecordService(
			ISweepstakesRecordService sweepstakesRecordService) {
		this.sweepstakesRecordService = sweepstakesRecordService;
	}
	public ICusUploadFileService getCusUploadFileService() {
		return cusUploadFileService;
	}
	public void setCusUploadFileService(ICusUploadFileService cusUploadFileService) {
		this.cusUploadFileService = cusUploadFileService;
	}
	public ISweepstakeSystemConfigService getSweepstakeSystemConfigService() {
		return sweepstakeSystemConfigService;
	}
	public void setSweepstakeSystemConfigService(ISweepstakeSystemConfigService sweepstakeSystemConfigService) {
		this.sweepstakeSystemConfigService = sweepstakeSystemConfigService;
	}
	public IShopAddressService getAddressService() {
		return addressService;
	}
	public void setAddressService(IShopAddressService addressService) {
		this.addressService = addressService;
	}
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
    	
    public IShopSpecService getShopSpecService() {
		return shopSpecService;
	}

	public void setShopSpecService(IShopSpecService shopSpecService) {
		this.shopSpecService = shopSpecService;
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

	public IRushBuyRecordService getRecordService() {
		return recordService;
	}

	public void setRecordService(IRushBuyRecordService recordService) {
		this.recordService = recordService;
	}

	public IRushBuyRegularService getRegularService() {
		return regularService;
	}

	public void setRegularService(IRushBuyRegularService regularService) {
		this.regularService = regularService;
	}
	public void setIntegralChangeRuleService(
			IIntegralChangeRuleService integralChangeRuleService) {
		this.integralChangeRuleService = integralChangeRuleService;
	}
	public void setIntegralHistoryService(
			IIntegralHistoryService integralHistoryService) {
		this.integralHistoryService = integralHistoryService;
	}
	public IIntegralRechargeRecordService getIntegralRechargeRecordService() {
		return integralRechargeRecordService;
	}
	public void setIntegralRechargeRecordService(
			IIntegralRechargeRecordService integralRechargeRecordService) {
		this.integralRechargeRecordService = integralRechargeRecordService;
	}
	public IShopDistributorService getShopDistributorService() {
		return shopDistributorService;
	}
	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}
	public IIntegralChangeRuleService getIntegralChangeRuleService() {
		return integralChangeRuleService;
	}
	public IIntegralHistoryService getIntegralHistoryService() {
		return integralHistoryService;
	}
	public IShopSystemConfigService getShopSystemConfigService() {
		return shopSystemConfigService;
	}
	public void setShopSystemConfigService(IShopSystemConfigService shopSystemConfigService) {
		this.shopSystemConfigService = shopSystemConfigService;
	}
	public IProductTypeService getProductTypeService() {
		return productTypeService;
	}
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}
	public IShopOrderdetailService getShopOrderdetailService() {
		return shopOrderdetailService;
	}
	public void setShopOrderdetailService(IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}
	
}
