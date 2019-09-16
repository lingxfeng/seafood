package com.eastinno.otransos.shop.core.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.util.StringUtil;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.utils.NewsUtil;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.mvc.WeixinBaseAction;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shop.core.service.IShopSystemConfigService;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.droduct.domain.Brand;
import com.eastinno.otransos.shop.droduct.domain.ProductType;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.droduct.service.ISpecialProTypeService;
import com.eastinno.otransos.shop.promotions.domain.Coupon;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.service.ICouponService;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IMyCouponService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DateUtil;
import com.eastinno.otransos.shop.util.ShopUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
/**
 * 微电商入口
 * @author nsz
 */

public class WxShopBaseAction extends WeixinBaseAction{
	@Inject
	private ISpecialProTypeService specialProTypeService;
	@Inject
    private IShopProductService shopProductService;	
	@Inject
    private IShopSystemConfigService shopSystemConfigService;
	@Inject
    private NewsUtil newsUtil;
	@Inject
	private ShopUtil shopUtil;
	@Inject
	protected IShopMemberService shopMemberService;
	@Inject
	private IMyCouponService myCouponService;
	@Inject
	private ICouponService couponService;
	@Inject
	private ICustomCouponService customCouponService;
	
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
	public IMyCouponService getMyCouponService() {
		return myCouponService;
	}
	public void setMyCouponService(IMyCouponService myCouponService) {
		this.myCouponService = myCouponService;
	}
	public void setShopSystemConfigService(
			IShopSystemConfigService shopSystemConfigService) {
		this.shopSystemConfigService = shopSystemConfigService;
	}
	public void setNewsUtil(NewsUtil newsUtil) {
		this.newsUtil = newsUtil;
	}
	public void setShopUtil(ShopUtil shopUtil) {
		this.shopUtil = shopUtil;
	}
	
	public ShopUtil getShopUtil() {
		return shopUtil;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	@Override
	public Object doBefore(WebForm form, Module module) {
		form.addResult("nu", this.newsUtil);
		form.addResult("su", this.shopUtil);
		form.addResult("config", this.shopSystemConfigService);
		form.addResult("dateu", new DateUtil());
		ShopMember member = getShopMember(form);
		if(member!=null){
			//member=this.shopMemberService.getShopMember(member.getId());
			form.addResult("member", member);
			//判断是否绑定用户名，如果没有绑定,跳转到绑定页面
			
			if(member.getPassword()==null){
				String bdmc=CommUtil.null2String(form.get("bdmc"));
				if(!"1".equals(bdmc)){
					return new Page("/bcd/wxshop/member/bindingPcName.html");
				}
			}
		}
		ShopDistributor distributor = getDistributor(form);
		if(distributor!=null){
			form.addResult("distributor", distributor);
		}
		Account account = getAccount(form);
		if(account!=null && member!=null){
			WeixinBaseUtils.setWeixinjs(form, getAccount(form));
			String url = WeixinBaseUtils.getDomain()+"/wxShopBase.java?cmd=init&pmemberId="+member.getId()+"&accountId="+account.getId();
			String weixinUrl = WeixinBaseUtils.getWxUrl(url, account);
			form.addResult("indexUrl", weixinUrl);
		}
		
		//如果该用户绑定成错误的老用户，则强制其进入重新绑定页进行绑定
		//如有疑问，请联系WB
/*		String pageUrl = WeixinBaseUtils.getDomain()+WeixinUtils.getUrlAll();
		int index = pageUrl.indexOf("toOldRebind");
		if(member.getOldUserStatus().equals("-1") && index==-1){
			String url = WeixinBaseUtils.getDomain()+"/wxShopMemberCenter.java?cmd=toOldRebind&pmemberId="+member.getId()+"&accountId="+account.getId();
			return WeixinBaseUtils.reSendWx(url, account);
		}*/
		
		return super.doBefore(form, module);
	}
	/**
	 * 从session中获取分销商
	 * @param form
	 * @return
	 */
	public ShopDistributor getDistributor(WebForm form) {
		HttpSession session = ActionContext.getContext().getSession();
		ShopDistributor distributor = (ShopDistributor) session.getAttribute("DISTRIBUTOR");
		return distributor;
	}
	
	private void doSearchList(WebForm form){
		String name=CommUtil.null2String(form.get("searchtext"));
        //Tenant tenant=ShiroUtils.getTenant();
        QueryObject qoGoods = new QueryObject();
        //qoGoods.addQuery("obj.tenant", tenant, "=");
        qoGoods.addQuery("obj.status", (short)1, "=");
        if(StringUtils.hasText(name)){
        	qoGoods.addQuery("obj.name like '%"+name+"%'");
        }
        IPageList iPageList=this.shopProductService.getShopProductBy(qoGoods);
        CommUtil.saveIPageList2WebForm(iPageList, form);
        form.addResult("goodsList", iPageList.getResult());        
	}
	
	/**
	 * 跳转到指定页面
	 * @param form
	 * @return
	 */
	public Page doToPage(WebForm form){
		String toPage = CommUtil.null2String(form.get("topage"));
		String searchtext = CommUtil.null2String(form.get("searchtext"));
		String flag = CommUtil.null2String(form.get("flag"));
		if(!StringUtils.hasText(toPage)){
			toPage = "/index.html";
		}
		if(StringUtils.hasText(searchtext)){
			this.doSearchList(form);
		}
		if(StringUtils.hasText(flag)){
			this.doCouponSearchList(form);
		}
		return new Page(toPage);
	}
	/**
	 * 由优惠券进行查询
	 * @param form
	 */
	private void doCouponSearchList(WebForm form){
		String id=CommUtil.null2String(form.get("cid"));
        QueryObject qoGoods = new QueryObject();
        qoGoods.addQuery("obj.status", (short)1, "=");
        if(StringUtils.hasText(id)){
        	CustomCoupon cc = this.customCouponService.getCustomCoupon(Long.parseLong(id));
        	Coupon c = cc.getCoupon();
        	Short type = c.getType();
        	if(type == 1){
        		List <Brand> blist = c.getBrandlist();
        		String str = "obj.brand.name not like'%%'";
        		for(Brand brand:blist){
        			str=str + "or obj.brand.name like'%"+brand.getName()+"%'";
        		}
        		qoGoods.addQuery(str);
        		qoGoods.setPageSize(-1);
        	}else if(type == 2){
        		List<ShopProduct> plist = c.getProductlist();
        		String str = "obj.name not like'%%'";
        		for(ShopProduct sp:plist){
        			str = str+"or obj.name like'%"+sp.getName()+"%'";
        		}
        	    qoGoods.addQuery(str);
        	}
        }
        IPageList iPageList=this.shopProductService.getShopProductBy(qoGoods);
        CommUtil.saveIPageList2WebForm(iPageList, form);
        form.addResult("goodsList", iPageList.getResult());        
	}
	/**
	 * 获取用户s
	 * @param form
	 * @return
	 */
	public ShopMember getShopMember(WebForm form){
		ShopMember member = getMemberBySession();
		if(member==null){
			member = getMemberByCode(form);
		}
		return member;
	}

	/**
	 * 微商前台入口
	 */
	@Override
	public Page doInit(WebForm form, Module module) {
		HttpSession session = ActionContext.getContext().getSession();
		session.removeAttribute("DISTRIBUTOR");
		form.addResult("distributor", null);
		return new Page("/bcd/wxshop/product/index.html");
	}
	/**
	 * 跳转到首页
	 * @param form
	 * @return
	 */
	public Page doToIntit(WebForm form){
		Account account = getAccount(form);
		if(account==null){
			return error(form, "登录超时");
		}
		String pmemberId = CommUtil.null2String(form.get("pmemberId"));
		String toProId = CommUtil.null2String(form.get("toProId"));
		String url = WeixinBaseUtils.getDomain()+"/wxShopBase.java?cmd=init&accountId="+account.getId()+"&pmemberId="+pmemberId;
		if(!"".equals(toProId)){
			url+="&toProId="+toProId;
		}
		return WeixinBaseUtils.reSendWx(url, account);
	}
	/**
	 * 错误页面
	 * @return
	 */
	public Page error(WebForm form,String msg){
		form.addResult("msg", msg);
		return new Page("/bcd/wxshop/error.html");
	}
	
	public void setSpecialProTypeService(
			ISpecialProTypeService specialProTypeService) {
		this.specialProTypeService = specialProTypeService;
	}
	
	public ShopMember getMemberBySession(){
		HttpSession session = ActionContext.getContext().getSession();
		ShopMember member = (ShopMember) session.getAttribute("SHOPMEMBER");
		if(member!=null){			
			ShopMember member2 = getShopMemberService().getShopMember(member.getId());
			if(member2!=null){
				session.setAttribute("SHOPMEMBER",member2);
				member = member2;
			}
		}
		return member;
	}
	public ShopMember getMemberByCode(WebForm form){
		ShopMember member = null;
		Follower f = getFollowerBySession();
		if(f==null){
			f = getFollowerByCode(form);
		}
		if(f==null){
			return null;
		}
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.follower.id",f.getId(),"=");
		qo.setPageSize(1);
		List<ShopMember> members = this.shopMemberService.getShopMemberBy(qo).getResult();
		if(members!=null && members.size()>0){
			member=members.get(0);
		}else{
			Tenant t = null;
			try {
				t = TenantContext.getTenant();
			} catch (Exception e) {
			}
			member = form.toPo(ShopMember.class);
			member.setCode(System.currentTimeMillis()+"");
			member.setTrueName(null);
			member.setNickname(f.getNickname());
			member.setSex(f.getSex());
			member.setPic(f.getHeadimgurl());
			member.setName(member.getCode());
			member.setTenant(t);
			member.setFollower(f);
			if(member.getPmember()!=null){
				ShopDistributor distributor = this.shopUtil.getDistributor(member.getPmember());
				member.setDistributor(distributor);
			}else{
				member.setDistributor(null);
			}
			this.shopMemberService.addShopMember(member);
		}
		HttpSession session = ActionContext.getContext().getSession();
		session.setAttribute("SHOPMEMBER",member);
		return member;
	}

	/**
	 * 判断登陆身份购买特殊商品
	 * @param form
	 * @return
	 */
	public Page doJudgingIdentity(WebForm form){
		Integer tId=CommUtil.null2Int(form.get("tId"));
		ProductType pType=this.shopUtil.getProductTypeService().getProductType((long)tId);
		ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
		if(pType.getIsSpecialProType()==true){
			if(member==null){
				this.addError("msg", "没有获取用户信息，请重新登陆微信");
				return pageForExtForm(form);
			}
			if(member.getDisType()!=0){
				this.addError("msg", "你已经买个是微店或者店不需要买此商品");
				return pageForExtForm(form);
			}
			QueryObject qo = new QueryObject();
			qo.addQuery("obj.member", member, "=");
			qo.addQuery("obj.", pType, "=");
			List<?> list=this.specialProTypeService.getSpecialProTypeBy(qo).getResult();
			/*if(list==null){
				SpecialProType spt = new SpecialProType();
				spt.setIsFirstLogin(true);
				spt.setMember(member);
				spt.setpType(pType);
				this.specialProTypeService.addSpecialProType(spt);
			}*/
			if(list==null){
				this.addError("status", "0"); //首次登陆输入密码
			}
		}
		return pageForExtForm(form);
	}
	public ICouponService getCouponService() {
		return couponService;
	}
	public void setCouponService(ICouponService couponService) {
		this.couponService = couponService;
	}
	public ISpecialProTypeService getSpecialProTypeService() {
		return specialProTypeService;
	}
	public IShopProductService getShopProductService() {
		return shopProductService;
	}
	public IShopSystemConfigService getShopSystemConfigService() {
		return shopSystemConfigService;
	}
	public NewsUtil getNewsUtil() {
		return newsUtil;
	}
	public ICustomCouponService getCustomCouponService() {
		return customCouponService;
	}
	public void setCustomCouponService(ICustomCouponService customCouponService) {
		this.customCouponService = customCouponService;
	}
	
}
