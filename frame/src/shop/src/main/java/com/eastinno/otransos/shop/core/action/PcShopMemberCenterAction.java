package com.eastinno.otransos.shop.core.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.core.util.MD5;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.shop.content.domain.ShopDiscuss;
import com.eastinno.otransos.shop.content.service.IShopDiscussService;
import com.eastinno.otransos.shop.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.shop.distribu.domain.ShopDistributor;
import com.eastinno.otransos.shop.distribu.service.ICommissionWithdrawService;
import com.eastinno.otransos.shop.distribu.service.IShopDistributorService;
import com.eastinno.otransos.shop.droduct.domain.ApplyPro;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IApplyProService;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.droduct.service.IShopProductService;
import com.eastinno.otransos.shop.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.shop.promotions.domain.SweepstakesRecord;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.promotions.service.IIntegralChangeRuleService;
import com.eastinno.otransos.shop.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.shop.promotions.service.ISweepstakesRecordService;
import com.eastinno.otransos.shop.trade.domain.ApplyRefund;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IApplyRefundService;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.shop.trade.service.IShopPayMentService;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.shop.usercenter.domain.ShopAddress;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IApplyWithdrawCashService;
import com.eastinno.otransos.shop.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopAddressService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.shop.util.DiscoShopUtil;
import com.eastinno.otransos.shop.util.EntityToolUtil;
import com.eastinno.otransos.shop.util.Verification;
import com.eastinno.otransos.shop.util.formatUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class PcShopMemberCenterAction extends PcShopBaseAction{
	
	@Inject
	private IShopMemberService service;
	
	@Inject
	private IShopDistributorService shopDistributorService;
	
	@Inject
    private ISystemRegionService systemRegionService;
	
	@Inject
    private IShopAddressService shopAddressService;
	
	@Inject
	private IShopDiscussService shopDiscussService;
	
	@Inject
	private IShopOrderInfoService shopOrderInfoService;
	
	@Inject
	private IShopOrderdetailService shopOrderdetailService;
	
	@Inject
	private ICommissionWithdrawService commissionWithdrawService;
	
	@Inject
	private IApplyProService applyProService;
	
	@Inject
	private IIntegralHistoryService integralHistoryService;
	
	@Inject
	private EntityToolUtil entityToolUtil;
	
	@Inject
	private IShopProductService shopProductService;
	
	@Inject
	private ICustomCouponService customCouponService;
	
	@Inject
	private IApplyRefundService applyRefundService;
	
	@Inject
	private IDeliveryRuleService deliveryRuleService;
	
	@Inject
	private IApplyWithdrawCashService applyWithdrawCashService; //申请提现
	
	@Inject
	private IRemainderAmtHistoryService remainderAmtHistoryService;
	@Inject
	private ISweepstakesRecordService sweepstakesRecordService;
	
	
	@Inject
	private IShopPayMentService shopPayMentService;
	@Inject
    private IPaymentConfigService paymentConfigService;
	@Inject
	private IIntegralChangeRuleService integralChangeRuleService;
	@Inject
	private IIntegralRechargeRecordService integralRechargeRecordService;
	
	@Override
	public Object doBefore(WebForm form, Module module) {
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(member!=null){
			form.addResult("user", member);
		}
		return super.doBefore(form, module);
	}

	/**
	 * 登录
	 * @param form
	 * @return
	 */
	public Page doLogin(WebForm form){
		String name=CommUtil.null2String(form.get("name"));
		String password=CommUtil.null2String(form.get("password"));
		String code=CommUtil.null2String(form.get("code"));
		boolean b = this.service.MemberLogin(name, password, code);
		/**
		 * 登录成功后清除cookie
		 */
		this.shoppingCartService.liulanqitoCar();
		/*if(b){
			this.service.addMemberCookie(name, password);
		}*/
		String url=form.getUrl();
		if(!url.split("\\?")[1].split("\\=")[1].startsWith("toLogin")){
			form.jsonResult(url);
		}
		return pageForExtForm(form);
	}
	
	/**
	 * 手机登录
	 * @param form
	 * @return
	 */
	public Page doMobileLogin(WebForm form){
		String mobileTel=CommUtil.null2String(form.get("mobileTel"));
		String code=CommUtil.null2String(form.get("code"));
		this.service.MobileLogin(mobileTel, code);
		return pageForExtForm(form);
	}
	
	/**
	 * 注册
	 * @param form
	 * @return
	 */
	public Page doRegister(WebForm form){
		String name=CommUtil.null2String(form.get("name"));
		String password=CommUtil.null2String(form.get("password"));
		String email=CommUtil.null2String(form.get("email"));
		String mobileTel=CommUtil.null2String(form.get("mobileTel"));
		String pmemberId=CommUtil.null2String(form.get("pmemberId"));
		this.service.register(name, password,email,mobileTel,pmemberId);
		return pageForExtForm(form);
	}
	
	/**
	 * 跳转
	 * @param form
	 * @return
	 */
	public Page doToPage(WebForm form){
		String toHtml=CommUtil.null2String(form.get("toHtml"));
		if("register".equals(toHtml)){
			return new Page("");
		}else if("updateOldPwd".equals(toHtml)){
    		return new Page("/userCenter/account_password.html");
    	}else if("updateEmail".equals(toHtml)){
    		return new Page("/userCenter/account_email.html");
		}else if("updateMobile".equals(toHtml)){
    		return new Page("/userCenter/account_mobile.html");
		}else if("updateMobile".equals(toHtml)){
    		return new Page("/userCenter/account_mobile.html");
		}else if("updateMobile".equals(toHtml)){
    		return new Page("/userCenter/account_mobile.html");
		}else if("updatePic".equals(toHtml)){
    		return new Page("/userCenter/account_pic.html");
		}else if("myOrder".equals(toHtml)){
    		return new Page("/userCenter/myOrder.html");
		}else if("myViewRecords".equals(toHtml)){
			return new Page("/userCenter/myViewRecords.html");
		}else if("addAddress".equals(toHtml)){
			return new Page("/userCenter/addAddress.html");
		}else if("standard".equals(toHtml)){
			form.addResult("num", CommUtil.null2String(form.get("num")));
			return new Page("/shop/product/standard.html");
		}
		return new Page("");
		
	}
	
	/**
	 * 验证用户名
	 * @param form
	 * @return
	 */
	public Page doValidateName(WebForm form){
		String name=CommUtil.null2String(form.get("name"));
		String code=CommUtil.null2String(form.get("code"));
		String rand = (String)ActionContext.getContext().getSession().getAttribute("rand");
		if(!rand.equals(code)){
			this.addError("msg", "验证码不正确");
		}
		if(!hasErrors()){
			ShopMember member=this.service.getShopMemberByName("name", name);
			if(member==null){
				this.addError("msg", "没有此用户");
			}
		}
		ActionContext.getContext().getSession().setAttribute("shopMemberName", name);
		return pageForExtForm(form);
	}
	
	/**
	 * 邮箱发送验证码
	 * @param form
	 * @return
	 */
	public Page doSendCodeByEmail(WebForm form){
		String email=CommUtil.null2String(form.get("email"));
		this.service.SendCodeByEmail(email);
		return pageForExtForm(form);
	}
	
	/**
	 * 验证
	 * @param form
	 * @return
	 */
	public Page doValidateCode(WebForm form){
		String code=CommUtil.null2String(form.get("code"));
		String sixRand = (String)ActionContext.getContext().getSession().getAttribute("sixRand");
		if(!sixRand.equals(code)){
			this.addError("msg", "验证码不正确，请重新输入");
		}
		ActionContext.getContext().getSession().setAttribute("vfalg", true);;
		return pageForExtForm(form);
	}
	
	/**
	 * 修改密码
	 * @param form
	 * @return
	 */
	public Page doUpdatePwd(WebForm form){
		String name = (String)ActionContext.getContext().getSession().getAttribute("shopMemberName");
		boolean falg = (boolean)ActionContext.getContext().getSession().getAttribute("vfalg");
		String password = CommUtil.null2String(form.get("password"));
		if(!Verification.checkPassword(password)){
			this.addError("msg", "密码长度只能在6-20位字符之间");
			return pageForExtForm(form);
		}
		if(!falg){
			this.addError("msg", "请先验证邮箱，在进行修改密码");
		}
		if(!hasErrors()){
			ShopMember member = this.service.getShopMemberByName("name", name);
			member.setPassword(MD5.encode(password));
			this.service.updateShopMember(member.getId(), member);
			ActionContext.getContext().getSession().removeAttribute("vfalg");
		}
		return pageForExtForm(form);
	}
	
	/**
	 * 跳转登录
	 * @return
	 */
	public Page doToLogin(){
		return new Page("/userCenter/login.html");
	}
	
	/**
	 * 跳转注册
	 * @return
	 */
	public Page doToRegister(){
		return new Page("/userCenter/register.html");
	}
	
	/**
     * 跳转个人中心
     * 
     * @param form
     */
    public Page doToUserCenter(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember shopMember2=this.service.getShopMember(shopMember.getId());
    	shopMember2 = this.service.getUserInfo(shopMember2);
    	form.addResult("user", shopMember2);
    	List<?> myColls = shopMember2.getMyCollections();
    	form.addResult("myColls", myColls);
    	List<?> list=this.service.getOrderInfo(form, shopMember2);
    	form.addResult("orderList", list);
        return new Page("/userCenter/userIndex.html");
    }
	
    /**
     * 跳转个人中心
     * @param form
     * @return
     */
    public Page doToMyUserCenter(WebForm form){
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember shopMember = this.service.getShopMember(member.getId());
    	this.shopOrderInfoService.queryOrderCount(form, shopMember);//订单 代付款 已付款 完成 个数
    	List<ShopOrderInfo> list=this.shopOrderInfoService.queryMyOrder(shopMember); //我的订单
    	this.queryMyCouponInfo(form, shopMember);//查询积分数量
    	form.addResult("orderList", list);
    	List<?> myColls = shopMember.getMyCollections();//我的收藏
    	form.addResult("myColls", myColls);
    	form.addResult("user", shopMember);
    	form.addResult("fu", formatUtil.fu);
    	return new Page("/userCenter/userIndex.html");
    }
    
    /**
	 * 查询优惠券--数量
	 * @param form
	 */
	public void queryMyCouponInfo(WebForm form,ShopMember shopMember){
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.shopMember", shopMember, "=");
		if(shopMember.getDisType()>0){
			qo.addQuery("obj.coupon.useType", Short.valueOf("0"), "=");
		}
		int couponCount=this.customCouponService.getCustomCouponBy(qo).getRowCount();
		form.addResult("couponCount", couponCount);
	}
		
    
    
    /**
     * 我的收藏--
     * 
     * @param form
     */
    public Page doMyCollection(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	form.addResult("user", sMember);
        return new Page("/userCenter/myCollection.html");
    }
    
    /**
     * 跳转申请--我的微店、实体店 页面
     * 
     * @param form
     */
    public Page doToApplyMicroShop(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.lev", 1, "=");
    	qo.setLimit(-1);
    	List<SystemRegion> list=this.systemRegionService.querySystemRegion(qo).getResult();
    	form.addResult("rList", list);
    	form.addResult("menuId", "applyMicroShop");
    	ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(shopMember);
    	if(distributor!=null){
    		if(distributor.getExStatus()==1){
    			form.addResult("flag", "1");
    			form.addResult("msg", "你已经申请成为了实体店,不需要再开微店");
    		}else if(distributor.getStatus()==0){
				form.addResult("msg", "微店正在审核");
			}else if(distributor.getStatus()==1){
				form.addResult("status", 1);
				form.addResult("msg", "微店已经审核通过");
			}else if(distributor.getStatus()==2){
				form.addResult("msg", "你的申请不符合成为微店，请核对信息后重新申请");
				return new Page("/userCenter/editApplyShop.html");
			}
    	}
        return new Page("/userCenter/applyShop.html");
    }
    
    /**
     * 跳转申请--实体店 页面
     * 
     * @param form
     */
    public Page doToApplyEntityShop(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.lev", 1, "=");
    	qo.setLimit(-1);
    	List<SystemRegion> list=this.systemRegionService.querySystemRegion(qo).getResult();
    	form.addResult("rList", list);
    	form.addResult("menuId", "applyEntityShop");
    	ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(shopMember);
    	if(distributor!=null){
    		if(distributor.getExStatus()==-1){
    			if(distributor.getDisType()==2){
    				form.addResult("msg", "实体店正在审核");
    			}
			}else if(distributor.getExStatus()==1){
				form.addResult("status", 1);
				form.addResult("msg", "实体店已经审核通过");
			}else if(distributor.getExStatus()==2){
				form.addResult("msg", " 你的申请不符合成为实体店，请核对信息后重新申请");
				return new Page("/userCenter/editApplyShop.html");
			}
    	}
        return new Page("/userCenter/applyShop.html");
    }
    
    /**
     * 申请--微店、实体店
     * 
     * @param form
     */
    public Page doApplyShop(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	String area_id=CommUtil.null2String(form.get("area_id"));
    	String myShopName=CommUtil.null2String(form.get("myShopName"));
    	String mobile=CommUtil.null2String(form.get("mobile"));
    	Integer disType=CommUtil.null2Int(form.get("disType"));
    	SystemRegion area=this.systemRegionService.getSystemRegionBySn(area_id);
    	ShopDistributor distributor = new ShopDistributor();
    	distributor.setArea(area);
    	distributor.setMyShopName(myShopName);
    	distributor.setMobile(mobile);
    	distributor.setDisType(disType);
    	distributor.setMember(shopMember);
    	this.shopDistributorService.addShopDistributor(distributor);
        return new Page("/userCenter/applyShop.html");
    }
    
    /**
	 * 审核结果
	 * @return
	 */
	public Page auditResults(WebForm form,ShopDistributor distributor){
		if(distributor.getMember().getDisType()==2){
			if(distributor.getExStatus()==-1){
				form.addResult("msg", "实体店正在审核");
			}else if(distributor.getDisType()==1){
				form.addResult("msg", "实体店已经审核通过");
			}else if(distributor.getDisType()==2){
				form.addResult("msg", " 你的申请不符合成为实体店，请核对信息后重新申请");
				return new Page("/userCenter/editApplyShop.html");
			}
		}else if(distributor.getMember().getDisType()==1){
			if(distributor.getStatus()==0){
				form.addResult("msg", "微店正在审核");
			}else if(distributor.getDisType()==1){
				form.addResult("msg", "微店已经审核通过");
			}else if(distributor.getDisType()==2){
				form.addResult("msg", "你的申请不符合成为微店，请核对信息后重新申请");
				return new Page("/userCenter/editApplyShop.html");
			}
		}
		return new Page("/userCenter/auditResults.html");
	}
	
	/**
	 * 跳转--申请商品页面
	 * @return
	 */
	public Page doToApplyProduct(WebForm form){
		
		return new Page("/userCenter/applyProduct.html");
	}
	
	/**
	 * 申请商品
	 * @return
	 */
	public Page doApplyProduct(WebForm form){
		
		return new Page("/userCenter/applyProduct.html");
	}
	
	/**
	 * 跳转--申请商品列表页面
	 * @return
	 */
	public Page doToApplyProductList(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.distributor", distributor, "=");
		IPageList iPageList=this.applyProService.getApplyProBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		form.addResult("applyProList", iPageList.getResult());
		return new Page("/userCenter/applyProductList.html");
	}
	
	/**
     * 跳转到个人资料界面
     * 
     * @param form
     */
    public Page doToUserInfo(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember member=this.service.getShopMember(shopMember.getId());
    	QueryObject qo = new QueryObject();
        qo.addQuery("obj.lev", 1, "=");
        qo.setPageSize(-1);
        List<?> list = this.systemRegionService.querySystemRegion(qo).getResult();
        form.addResult("proviceList", list);
        form.addResult("user", member);
        return new Page("/userCenter/userInfo.html");
    }
    
    /**
     * 修改个人信息
     * 
     * @param form
     */
    public Page doUpdateUserInfo(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String nickname=CommUtil.null2String(form.get("nickname"));
    	String trueName=CommUtil.null2String(form.get("trueName"));
    	String sex=CommUtil.null2String(form.get("sex"));
    	String idCard=CommUtil.null2String(form.get("idCard"));
    	sMember.setNickname(nickname);
    	sMember.setTrueName(trueName);
    	sMember.setSex(sex);
    	sMember.setIdCard(idCard);
    	
    	//form.toPo(sMember);
    	String imgPath = FileUtil.uploadFile(form, "pic", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(imgPath)) {
        	sMember.setPic(imgPath);
        }
        boolean b=this.service.updateShopMember(sMember.getId(), sMember);
        if(b){
        	form.addResult("state", "0");
        }
        form.addResult("user", sMember);
        UserContext.setMember(sMember);
        return new Page("/userCenter/userInfo.html");
    }
    
    /**
     * 修改密码(个人资料修改)
     * 
     * @param form
     */
    public Page doUpdatePassword(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String oldPwd=CommUtil.null2String(form.get("oldPwd"));
    	String newPwd=CommUtil.null2String(form.get("newPwd"));
    	String confirmPwd=CommUtil.null2String(form.get("confirmPwd"));
    	this.service.updatePassword(oldPwd, newPwd, confirmPwd, sMember);
        return pageForExtForm(form);
    }
    
    /**
     * 修改email(个人资料)
     * 
     * @param form
     */
    public Page doUpdateEmail(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败，请重新登录");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String password=CommUtil.null2String(form.get("password"));
    	String email=CommUtil.null2String(form.get("email"));
    	this.service.updateEmail(password, email, sMember);
        return pageForExtForm(form);
    }
    
    /**
     * 修改手机(个人资料)
     * 
     * @param form
     */
    public Page doUpdateMobileTel(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "修改个人信息失败,请重新登录");
    		return pageForExtForm(form);
    	}
    	ShopMember sMember=this.service.getShopMember(shopMember.getId());
    	String mobileTel=CommUtil.null2String(form.get("mobileTel"));
    	String code=CommUtil.null2String(form.get("code"));
    	boolean b=this.service.updateMobileTel(mobileTel, code , sMember);
    	if(b){
    		ActionContext.getContext().getSession().removeAttribute("bcd_sixRand");
    	}
        return pageForExtForm(form);
    }
    
    /**
	 * 跳转添加收货地址
	 * @param form
	 * @return
	 */
	public Page doToSaveAddress(WebForm form){
		QueryObject qo = new QueryObject();
        qo.addQuery("obj.lev", 1, "=");
        qo.setPageSize(-1);
        List<?> list = this.systemRegionService.querySystemRegion(qo).getResult();
        form.addResult("proviceList", list);
		return new Page("/userCenter/addAddress.html");
	}
	
	/**
	 * 添加收货地址
	 * @param form
	 * @return
	 */
	public Page doSaveAddress(WebForm form){
		ShopMember shopMember = (ShopMember) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(shopMember==null){
			this.addError("msg", "修改个人信息失败,请重新登录");
    		return pageForExtForm(form);
		}
		ShopAddress obj = form.toPo(ShopAddress.class);
		if(!hasErrors()){
			obj.setUser(shopMember);
			//地区信息
			String sn=CommUtil.null2String(form.get("area_id"));
			SystemRegion region=this.systemRegionService.getSystemRegionBySn(sn);
			obj.setArea(region);
	        Long id = this.shopAddressService.addShopAddress(obj);
		}
		return go("pcShopMemberCenter.myDeliveryAddress");
	}
	
	 /**
     * 收货地址
     * 
     * @param form
     */
    public Page doMyDeliveryAddress(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = form.toPo(QueryObject.class);
    	qo.addQuery("obj.user.id", shopMember.getId(), "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	IPageList iPageList=this.shopAddressService.getShopAddressBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("addressList", iPageList.getResult());
        return new Page("/userCenter/myAddress.html");
    }
    
    /**
     * 跳转编译收货地址
     * 
     * @param form
     */
    public Page doToEditDeliveryAddress(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	ShopAddress shopAddress=(ShopAddress)this.shopAddressService.getShopAddress(Long.valueOf(id));
    	if(shopAddress.getArea()!=null){
    		if(shopAddress.getArea().getParent().getLev()==1){
    			form.addResult("proId", shopAddress.getArea().getParent().getId());
    		}else{
    			form.addResult("proId", shopAddress.getArea().getParent().getParent().getId());
    		}
    	} 
    	form.addResult("address", shopAddress);
    	QueryObject qo = new QueryObject();
        qo.addQuery("obj.lev", 1, "=");
        qo.setPageSize(-1);
        List<?> list = this.systemRegionService.querySystemRegion(qo).getResult();
        form.addResult("proviceList", list);
    	return new Page("/userCenter/updateAddress.html");
    }
    
    /**
     * 编译收货地址
     * 
     * @param form
     */
    public Page doEditDeliveryAddress(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	String code=CommUtil.null2String(form.get("area_id"));
    	ShopAddress shopAddress=(ShopAddress)this.shopAddressService.getShopAddress(Long.valueOf(id));
    	SystemRegion systemRegion=this.systemRegionService.getSystemRegionBySn(code);
    	form.toPo(shopAddress);
    	shopAddress.setArea(systemRegion);
    	this.shopAddressService.updateShopAddress(Long.valueOf(id), shopAddress);
        return go("myDeliveryAddress");
    }
    
    /**
	 * 我的咨询
	 * @param form
	 * @return
	 */
	public Page doGetMyAdvice(WebForm form){
		String reply=CommUtil.null2String(form.get("reply"));
		ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		QueryObject  qo= new QueryObject();
		qo.addQuery("obj.user.id", shopMember.getId(), "=");
		if(reply!=""){
			qo.addQuery("obj.isHf", Boolean.valueOf(reply), "=");
		}
		List<?> list=this.shopDiscussService.getShopDiscussBy(qo).getResult();
		form.addResult("list", list);
		return new Page("/userCenter/myAdvice.html");
	}
	
	/**
     * 删除收货地址
     * 
     * @param form
     */
    public Page doDelDeliveryAddress(WebForm form) {
    	String id=CommUtil.null2String(form.get("id"));
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.addr.id", Long.valueOf(id), "=");
    	Integer count=this.shopOrderInfoService.getShopOrderInfoBy(qo).getRowCount();
    	if(count>0){
    		this.addError("msg", "不能删除此地址");
    	}
    	this.shopAddressService.delShopAddress(Long.valueOf(id));
        return pageForExtForm(form);
    }
    
    /**
	 * 删除收藏
	 * @param form
	 * @return
	 */
	public Page doDelCollection(WebForm form){
		Long id=Long.valueOf(CommUtil.null2Int(form.get("id")));
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		member=this.service.getShopMember(member.getId());
		List<ShopProduct> list=member.getMyCollections();
		for (ShopProduct shopProduct : list) {
			if(shopProduct.getId().equals(id)){
				list.remove(shopProduct);
				shopProduct.setCollectNum(shopProduct.getCollectNum()-1);
				this.shopProductService.updateShopProduct(shopProduct.getId(), shopProduct);
				break;
			}
		}
		this.service.updateShopMember(member.getId(), member);
		ActionContext.getContext().getSession().setAttribute("DISCO_MEMBER", member);
		return go("myCollection&menuId=goodsCollection");
	}
	
	/**-------------店铺管理--------------------**/
	
	/**
	 * 微店团
	 * @param form
	 * @return
	 */
	public Page doGetMyWdGroup(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		qo.addQuery("obj.parent.id="+distributor.getId()+"union all obj.parent.parent.id="+distributor.getId());
		IPageList iPageList=this.shopDistributorService.getShopDistributorBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		form.addResult("distributorList", iPageList.getResult());
		return new Page("/userCenter/myWdGroup.html");
	}
	
	/**
	 * 我的会员信息--一级会员
	 * @param form
	 * @return
	 */
	public Page doGetMyMemberInfo(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		qo.addQuery("obj.distributor", distributor, "=");
		qo.addQuery("obj.disType",Integer.valueOf(0), "=");
		IPageList iPageList=this.service.getShopMemberBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		/*form.addResult("distributorList", iPageList.getResult());
		return new Page("/userCenter/myMemberOrder.html");*/
		form.addResult("shopMemberList", iPageList.getResult());
		return new Page("/userCenter/myMemberInfo.html");
	}
	
	/**
	 * 二维码
	 * @param form
	 * @return
	 */
	public Page doGetMyQRCode(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		form.addResult("distributor", distributor);
		return new Page("/userCenter/myQRCode.html");
	}
	
	/**
	 * 会员订单--利益相关的会员
	 * @param form
	 * @return
	 */
	public Page doGetMyMemberOrder(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		String status=CommUtil.null2String(form.get("status"));
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id ",member.getId() ,"!=");
		/*if(StringUtils.hasText(status)){
			qo.addQuery("obj.status", Integer.valueOf(status), "=");
		}*/
		if(member.getDisType()==2){
			qo.addQuery("obj.topDistributor", distributor, "=");
		}else if(member.getDisType()==1){
			qo.addQuery("obj.distributor", distributor, "=");
		}
		qo.addQuery("obj.status", 1, ">=");
		qo.setOrderBy("ceateDate");
		qo.setOrderType("desc");
		IPageList iPageList=this.shopOrderInfoService.getShopOrderInfoBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		form.addResult("shopOrderInfoList", iPageList.getResult());
		return new Page("/userCenter/myMemberOrder.html");
	}
	
	/**
	 * 我的微店伙伴订单
	 * @param form
	 * @return
	 */
	public Page doGetMyWdPartnerOrder(WebForm form){
		String status=CommUtil.null2String(form.get("status"));
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		QueryObject qo = new QueryObject();
		if(StringUtils.hasText(status)){
			qo.addQuery("obj.status", Integer.valueOf(status), "=");
		}
		qo.addQuery("obj.distributor.parent.id = "+distributor.getId()+"or obj.distributor.parent.parent.id = "+distributor.getId());
		IPageList iPageList=this.shopOrderInfoService.getShopOrderInfoBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		form.addResult("shopOrderInfoList", iPageList.getResult());
		return new Page("/userCenter/myWdPartnerOrder.html");
	}
	
	/**
	 * 体现申请--跳转
	 * @param form
	 * @return
	 */
	public Page doToApplyReflectRecord(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		form.addResult("distributor", distributor);
		return new Page("/userCenter/applyReflectRecord.html");
	}
	/**
	 * 提现申请
	 * @param form
	 * @return
	 */
	public Page doApplyReflectRecord(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		String commission=CommUtil.null2String(form.get("commission")); //金额
		String openAccountName=CommUtil.null2String(form.get("openAccountName")); //开户人姓名
		String bankCardNum=CommUtil.null2String(form.get("bankCardNum")); //银行卡号
		String openAccountType=CommUtil.null2String(form.get("openAccountType")); //开户行详细信息
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		distributor.setBankCardNum(bankCardNum);
		distributor.setOpenAccountName(openAccountName);
		distributor.setOpenAccountType(openAccountType);
		this.shopDistributorService.updateShopDistributor(distributor.getId(), distributor);
		CommissionWithdraw cw = new CommissionWithdraw();
		if(distributor.getDisCommission()>Double.valueOf(commission)){
			cw.setCommission(Double.valueOf(commission));
		}else{
			return error(form,"申请提取的佣金大于余额佣金，请重新申请"); 
		}
		cw.setDistributor(distributor);
		cw.setUser(member);
		this.commissionWithdrawService.addCommissionWithdraw(cw);
		form.addResult("distributor", "distributor");
		return new Page("/userCenter/applyReflectRecord.html");
	}
	/**
	 * 我的提现记录
	 * @param form
	 * @return
	 */
	public Page doGetMyReflectRecord(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.distributor", distributor, "=");
		IPageList iPageList=this.commissionWithdrawService.getCommissionWithdrawBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		List<?> list = iPageList.getResult();
		form.addResult("fu",formatUtil.fu);
		form.addResult("distributorList", list);
		return new Page("/userCenter/myReflectRecord.html");
	}
	
	/**
     * 查看订单
     * 
     * @param form
     */
    public Page doMyOrder(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	List<?> list=this.service.getOrderInfo(form, shopMember);
    	form.addResult("orderList", list);
        return new Page("/userCenter/myOrder.html");
    }
    
    /**
     * 获取我的订单--2
     * 
     * @param form
     */
    public Page doGetMyOrder(WebForm form) {
    	String status=CommUtil.null2String(form.get("status"));  //订单状态:0未支付 ，1已支付待发货，2商家已发货，3用户已收货，-1已取消订单,4申请退货，5已同意退货，6:不同意退货
    	String currentPage=CommUtil.null2String(form.get("currentPage")); 
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	//订单数量
    	this.shopOrderInfoService.queryOrderCount(form, shopMember);
    	
    	String orderId=CommUtil.null2String(form.get("order_id"));
    	QueryObject qo2 = new QueryObject();
    	qo2.addQuery("obj.user", shopMember, "=");
    	if(StringUtils.hasText(status)){
    		qo2.addQuery("obj.status", Integer.valueOf(status), "=");
    	}
    	if(StringUtils.hasText(orderId)){
    		qo2.addQuery("obj.code", orderId, "=");
    	}
    	if(StringUtils.hasText(currentPage)){
    		qo2.setCurrentPage(Integer.valueOf(currentPage));
    	}
    	qo2.setOrderBy("ceateDate");
    	qo2.setOrderType("desc");
    	IPageList iPageList=shopOrderInfoService.getShopOrderInfoBy(qo2);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("orderList", iPageList.getResult());
    	
    	Map<Long, Map> orders = new HashMap();
    	List<ShopOrderInfo> orderList = iPageList.getResult();
    	if(orderList!=null){
    		int listNum = orderList.size();
        	for(int i=0; i<listNum; ++i){
        		Map<Long , List<ShopOrderdetail>> shopOrderdetailMap=this.shopOrderInfoService.getOrderDetailsByBrand((ShopOrderInfo)orderList.get(i));
        		orders.put(orderList.get(i).getId(), shopOrderdetailMap);
        	}
    	}
    	form.addResult("user", shopMember);
    	form.addResult("orders", orders);
    	form.addResult("eu", this.entityToolUtil);
        return new Page("/userCenter/myOrder2.html");
        
    }
    
    /**
     * 查看订单
     * @param form
     * @return
     */
    public Page doGetShopOrder(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	Integer orderId=CommUtil.null2Int(form.get("orderId"));
    	ShopOrderInfo shopOrderInfo=this.shopOrderInfoService.getShopOrderInfo((long)orderId);
    	//普通会员商品总价格
    	Double amt=this.shopOrderInfoService.getShopOrderAmt(shopOrderInfo);
    	Double freight=this.deliveryRuleService.getDeliveryCost(shopOrderInfo);
    	form.addResult("user", shopMember);
    	form.addResult("shopOrderInfo", shopOrderInfo);
    	form.addResult("mAmt", amt);
    	form.addResult("allPrice", amt+freight);
        return new Page("/userCenter/myOrderDetails.html");
    }
    
    
    
    
    
    
    
    public Page doGetShopOrderdetailGroupingByBrand(WebForm form) {
    	Integer orderId=CommUtil.null2Int(form.get("orderId"));
    	ShopOrderInfo shopOrderInfo=this.shopOrderInfoService.getShopOrderInfo((long)orderId);
    	Map<Long , List<ShopOrderdetail>> shopOrderdetailMap=this.shopOrderInfoService.getOrderDetailsByBrand(shopOrderInfo);
    	form.addResult("shopOrderInfo", shopOrderInfo);
    	form.addResult("shopOrderdetailMap", shopOrderdetailMap);
        return new Page("/userCenter/myOrder2.html");
    }
	
	/**
	 * 优惠券
	 * @param form
	 * @return
	 */
	public Page doGetMyCoupons(WebForm form){
		ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		if(member==null){
			return new Page("/userCenter/login.html"); 
		}
		Integer disType = member.getDisType();
		QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
		qo.addQuery("obj.shopMember", member, "=");
		if(disType!=0){
			qo.addQuery("obj.coupon.useType", Short.valueOf("0"), "=");
		}
		qo.setOrderBy("createTime desc,coupon.currentStatus asc");
		IPageList iPageList=this.customCouponService.getCustomCouponBy(qo);
		CommUtil.saveIPageList2WebForm(iPageList, form);
		form.addResult("couponList", iPageList.getResult());
		form.addResult("fu", formatUtil.fu);
		return new Page("/userCenter/myCoupons2.html");
	}
	
	/**
     * 保存申请商品
     * 
     * @param form
     */
    public Page doSaveApplyPro(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	ApplyPro applyPro=form.toPo(ApplyPro.class);
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	if(!hasErrors()){
    		String proPic = FileUtil.uploadFile(form, "proPic", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
    	                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
	        if(!"".equals(proPic)){
	        	applyPro.setProPic(proPic);
	        }
	        String eqPic = FileUtil.uploadFile(form, "eqPic", UploadFileConstant.FILE_UPLOAD_PATH + "/" + DiscoShopUtil.getMerchantCode() + "/"
	                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
	        if(!"".equals(eqPic)){
	        	applyPro.setEqPic(eqPic);
	        }
	        ShopDistributor distributor = this.shopDistributorService.getShopDistributorByMember(shopMember);
	        applyPro.setDistributor(distributor);
    		this.applyProService.addApplyPro(applyPro);
    	}
    	List<?> list=this.service.getOrderInfo(form, shopMember);
        return new Page("/userCenter/applyProduct.html");
    }
    
    /**
     * 申请商品--列表
     * 
     * @param form
     */
    public Page doGetApplyPro(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(shopMember);
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.distributor", distributor, "=");
    	IPageList iPageList=this.applyProService.getApplyProBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("applyProList", iPageList.getResult());
        return new Page("/userCenter/applyProductList.html");
    }
    
    /**
     * 获取积分--列表
     * 
     * @param form
     */
    public Page doGetMyIntegralRecord(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	shopMember = this.service.getShopMember(shopMember.getId());
    	QueryObject qo=form.toPo(QueryObject.class);
    	qo.addQuery("obj.user", shopMember, "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	IPageList iPageList=this.integralHistoryService.getIntegralHistoryBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("integralHistoryList", iPageList.getResult());
    	form.addResult("user", shopMember);
        return new Page("/userCenter/myIntegralHistory.html");
    }
    
    /**
     * 跳转--积分充值
     * 
     * @param form  
     */
    public Page doToIntegralRecharge(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	Long ruler = this.integralChangeRuleService.getIntegralCashRate();
    	shopMember=this.service.getShopMember(shopMember.getId());
    	form.addResult("ruler", ruler);
    	form.addResult("user", shopMember);
    	form.addResult("fu", formatUtil.fu);
        return new Page("/userCenter/myIntegralRecharge.html");
    }
    
    /**
     * 积分充值
     * 
     * @param form  
     */
    public Page doIntegralRecharge(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	shopMember = this.service.getShopMember(shopMember.getId());
    	String type=CommUtil.null2String(form.get("type"));
    	long integral=(long)CommUtil.null2Int(form.get("integral"));
    	if(integral<=0){
    		this.error(form, "充值数量大于0");
    	}
    	IntegralRechargeRecord irr = new IntegralRechargeRecord();
    	Long ruler = this.integralChangeRuleService.getIntegralCashRate();
		Long money = ruler*integral;
    	irr.setCode(new Date().getTime()+"");
    	irr.setGross_price((double)money);
    	irr.setIntegral(integral);
    	irr.setMember(shopMember);
    	//irr.setUuid(UUID.randomUUID().toString());
    	irr.setUuid(new Date().getTime()+"");
    	if("0".equals(type)){
    		if(shopMember.getRemainderAmt()<=0 || shopMember.getRemainderAmt()<money){
    			return this.error(form, "充值余额不足");
    		}
    		shopMember.setRemainderAmt(shopMember.getRemainderAmt()-money);
    		shopMember.setTotalIntegral(shopMember.getTotalIntegral()+integral);
    		shopMember.setAvailableIntegral(shopMember.getAvailableIntegral()+ integral);		
			this.service.updateShopMember(shopMember.getId(), shopMember);
			this.integralHistoryService.saveIntegralHistory(integral, shopMember, shopMember.getNickname()+"充值获取积分", 2);//积分充值记录
			irr.setType(Short.valueOf("0"));
			irr.setStatus(Short.valueOf("1"));
			this.integralRechargeRecordService.addIntegralRechargeRecord(irr);//充值记录
			this.remainderAmtHistoryService.addRemainderAmtHistory(shopMember, 2, -Double.valueOf(String.valueOf(money)), shopMember.getNickname()+"购物消费余额");//余额变动记录
    	}else if("1".equals(type)){
    		//irr.setStatus(Short.valueOf(type));
    		irr.setType(Short.valueOf("1"));
    		this.integralRechargeRecordService.addIntegralRechargeRecord(irr); //
    		return DiscoShopUtil.goPage("/pcShopMemberCenter.java?cmd=toPay&id="+irr.getId());
    	}else{
    		return this.error(form, "没有查询到此充值类型，请与管理员联系！！！");
    	}
    	ActionContext.getContext().getSession().setAttribute("shopMember", shopMember);
        return DiscoShopUtil.goPage("/pcShopMemberCenter.java?cmd=payComplete&id="+irr.getId());
    }
    
    public Page doToPay(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	IntegralRechargeRecord irr = this.integralRechargeRecordService.getIntegralRechargeRecord(Long.parseLong(id));
    	PaymentConfig paymentConfig=this.paymentConfigService.getPaymentConfigByName("WEIXINMPSM");
		String imgUrl=this.shopPayMentService.paySubmit3(irr, paymentConfig, "");
		form.addResult("imgUrl", imgUrl);
    	form.addResult("irr", irr);
    	return new Page("/userCenter/myWXPayQRCode.html");
    }
    
    public Page doPayComplete(WebForm form){
    	String id = CommUtil.null2String(form.get("id"));
    	IntegralRechargeRecord obj = this.integralRechargeRecordService.getIntegralRechargeRecord(Long.parseLong(id));
    	form.addResult("irr", obj);
    	form.addResult("fu", formatUtil.fu);
    	return new Page("/userCenter/myIntegralRechargeComplete.html");
    }
    
    /**
     * 记录
     * @param form
     * @return
     */
    public Page doGetIntegralRecharge(WebForm form){
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	member = this.service.getShopMember(member.getId());
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.addQuery("obj.member.id", member.getId(), "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	IPageList iPageList = this.integralRechargeRecordService.getIntegralRechargeRecordBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("irrList", iPageList.getResult());
    	form.addResult("user", member);
    	return new Page("/userCenter/myIntegralRechargeList.html");
    }
    
    /**
     * 查询店铺下所有--微店
     * 
     * @param form
     */
    public Page doGetMyMicroShop(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopDistributor distributor=shopDistributorService.getShopDistributorByMember(shopMember);
    	if(distributor==null || distributor.getDisType()!=2 || distributor.getExStatus()!=1){
    		return error(form , "你不是体验店，或体验店申请没有通过");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.topDistributor", distributor, "=");
    	IPageList iPageList=this.shopDistributorService.getShopDistributorBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("wdList", iPageList.getResult());
        return new Page("/userCenter/myMicroShop.html");
    }
	
    /**
     * 重新--申请店
     * 
     * @param form
     */
    public Page doEditApplyShop(WebForm form) {
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopDistributor distributor=shopDistributorService.getShopDistributorByMember(member);
    	String myShopName=CommUtil.null2String(form.get("myShopName"));
    	String mobile=CommUtil.null2String(form.get("mobile"));
    	String area_id=CommUtil.null2String(form.get("area_id"));
    	Integer disType=CommUtil.null2Int(form.get("disType"));
    	SystemRegion area=this.systemRegionService.getSystemRegionBySn(area_id);
    	distributor.setMyShopName(myShopName);
    	distributor.setMobile(mobile);
    	distributor.setArea(area);
    	if(disType==2){
    		distributor.setExStatus(-1);
    	}else if(disType==1){
    		distributor.setStatus(0);
    	}else{
    		return error(form , "提交申请发生错误，请重新申请");
    	}
    	this.shopDistributorService.updateShopDistributor(distributor.getId(), distributor);
        return new Page("/userCenter/myIntegralHistory.html");
    }
    
    /**
     * 得到店铺所有订单那
     * @param form
     * @return
     */
    public Page doGetMyShopAllOrderInfo(WebForm form){
    	String order_id=CommUtil.null2String(form.get("order_id"));
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
    	if(distributor==null || member.getDisType()!=2){
    		return error(form, "没有获取微店信息，或登录超时");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.status>0");
    	if(StringUtils.hasText(order_id)){
    		qo.addQuery("obj.code", order_id, "=");
    	}
    	qo.addQuery("obj.topDistributor.id = "+distributor.getId()+"or obj.distributor.id="+distributor.getId());
    	qo.addQuery("obj.user", member, "!=");
    	IPageList iPageList=this.shopOrderInfoService.getShopOrderInfoBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	
    	Map<Long, Map> orders = new HashMap();
    	List<ShopOrderInfo> orderList = iPageList.getResult();
    	int listNum = orderList.size();
    	for(int i=0; i<listNum; ++i){
    		Map<Long , List<ShopOrderdetail>> shopOrderdetailMap=this.shopOrderInfoService.getOrderDetailsByBrand((ShopOrderInfo)orderList.get(i));
    		orders.put(orderList.get(i).getId(), shopOrderdetailMap);
    	}
    	form.addResult("orderList", orderList);
    	form.addResult("user", member);
    	form.addResult("orders", orders);
    	form.addResult("eu", this.entityToolUtil);
    	return new Page("/userCenter/myShopAllOrderInfo.html");
    }
    
    /**
     * 我所有会员信息
     * @param form
     * @return
     */
    public Page doGetMyAllMemberInfo(WebForm form){
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopDistributor distributor=this.shopDistributorService.getShopDistributorByMember(member);
    	if(distributor==null || member.getDisType()!=2){
    		return error(form, "没有获取微店信息，或登录超时");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.distributor.topDistributor.id = "+distributor.getId()+"or obj.distributor.id="+distributor.getId());
    	qo.addQuery("obj.disType=0");
    	IPageList iPageList=this.service.getShopMemberBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("shopMemberList", iPageList.getResult());
    	return new Page("/userCenter/myAllMemberInfo.html");
    }
    
    /**
     * 我的评论
     * @param form
     * @return
     */
    public Page doGetMyEvaluation(WebForm form){
    	ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.user", member, "=");
    	qo.addQuery("obj.type", 1, "=");
    	IPageList iPageList=this.shopDiscussService.getShopDiscussBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("plList", iPageList.getResult());
    	return new Page("/userCenter/myEvaluation.html");
    }
    
    /**
     * 评价
     * @param form
     * @return
     */
    public Page doSaveDiscuss(WebForm form){
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	ShopDiscuss entry = (ShopDiscuss)form.toPo(ShopDiscuss.class);
    	String content=null;
		try {
			content = new String(CommUtil.null2String(form.get("content")).getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	entry.setContent(content);
    	if(!hasErrors()){
    		Long proId=Long.valueOf(CommUtil.null2Int(form.get("proId")));
        	Long oDid=Long.valueOf(CommUtil.null2Int(form.get("oDid")));
        	ShopOrderdetail shopOrderdetail=this.shopOrderdetailService.getShopOrderdetail(oDid);
        	ShopProduct product=this.shopProductService.getShopProduct(proId);
        	if(!shopOrderdetail.getUser().getId().equals(shopMember.getId())){
        		return error(form , "请重新登陆");
        	}
        	if(shopOrderdetail.getShopDiscuss()!=null){
        		return error(form , "此订单以评论");
        	}
    		entry.setUser(shopMember);
    		entry.setTenant(TenantContext.getTenant());
    		entry.setShopOrderdetail(shopOrderdetail);
    		entry.setPro(product);
    		this.shopDiscussService.addShopDiscuss(entry);
    		shopOrderdetail.setShopDiscuss(entry);
    		this.shopOrderdetailService.updateShopOrderdetail(shopOrderdetail.getId(), shopOrderdetail);
    	}
    	return go("toEvaluation&type=2");
    }
    
    /**
     * 去评价页面
     */
    public Page doToEvaluation(WebForm form){
    	String type=CommUtil.null2String(form.get("type")); //1：未评价 2：已评价
    	ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(member==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
    	qo.addQuery("obj.user", member, "=");
    	if("1".equals(type)){
    		qo.addQuery("obj.shopDiscuss is null");
    		qo.addQuery("obj.orderInfo.status in (2,3)");
    	}else if("2".equals(type)){
    		qo.addQuery("obj.shopDiscuss is not null");
    	}
    	qo.setOrderBy("ceateDate");
    	qo.setOrderType("desc");
    	IPageList iPageList=this.shopOrderdetailService.getShopOrderdetailBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("plList", iPageList.getResult());
    	return new Page("/userCenter/proEvaluation.html");
    }
    
    /**
     * 订单--评价页面
     */
    public Page doToEvaluation2(WebForm form){
    	Long id=Long.valueOf(CommUtil.null2Int(form.get("id")));
    	ShopOrderdetail shopOrderdetail = this.shopOrderdetailService.getShopOrderdetail(id);
    	form.addResult("obj", shopOrderdetail);
    	return new Page("/userCenter/proEvaluation2.html");
    }
    
    
    /**
     * 退款/退货
     */
    public Page doToRefundReturn(WebForm form){
    	String orderId=CommUtil.null2String(form.get("orderId"));
    	form.addResult("orderId", orderId);
    	return new Page("/userCenter/refundReturn.html");
    }
    
    /**
     * 退款/退货
     * 
     * @param form
     */
    public Page doOrderRefund(WebForm form) {
    	ApplyRefund applyRefund=(ApplyRefund)form.toPo(ApplyRefund.class);
        String code=CommUtil.null2String(form.get("orderId"));
        ShopMember member=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
        if(member==null){
        	return new Page("/userCenter/login.html");
        }
        ShopOrderInfo order=this.shopOrderInfoService.getShopOrderByName("code", code);
        if(!order.getUser().getId().equals(member.getId())){
        	return error(form , "你没有权限修改此订单");
        }
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.order.id", order.getId(), "=");
        Integer aCount=this.applyRefundService.getApplyRefundBy(qo).getRowCount();
        if(aCount>=1){
        	return error(form , "已经申请过退款/退货");
        }
        applyRefund.setOrder(order);
        applyRefund.setShopMember(member);
        this.applyRefundService.addApplyRefund(applyRefund);
        order.setStatus(4);
        this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
        return go("getMyOrder");
    }
    
    /**
     * 退款/退货
     * 
     * @param form
     * @return 
     */
    public Page doGetMyRefundReturn(WebForm form) {
    	
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = new QueryObject();
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	qo.addQuery("obj.shopMember", shopMember, "=");
    	IPageList iPageList=this.applyRefundService.getApplyRefundBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("ApplyRefundList", iPageList.getResult());
        return new Page("/userCenter/myRefundReturn.html");
    }
    
    /**
     * 跳转申请账户提现
     * 
     * @param form
     * @return 
     */
    public Page doToApplyWithdrawingCash(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	shopMember = this.service.getShopMember(shopMember.getId());
    	form.addResult("fu", formatUtil.fu);
    	form.addResult("user", shopMember);
        return new Page("/userCenter/applyWithdrawingCash.html");
    }
    
    /**
     * 申请账户提现
     * 
     * @param form
     * @return 
     */
    public Page doApplyWithdrawingCash(WebForm form) {
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	shopMember = this.service.getShopMember(shopMember.getId());
    	ApplyWithdrawCash applyWithdrawCash=form.toPo(ApplyWithdrawCash.class);
    	if(applyWithdrawCash.getSums()>shopMember.getRemainderAmt() || applyWithdrawCash.getSums()<100){
    		return error(form, "申请提取金额需要满100或申请金额不能大于账户余额");
    	}
    	boolean b=this.judgeApplySums(shopMember,applyWithdrawCash.getSums());
    	if(!b){
    		return error(form, "申请提取的总金额不能大于账户总金额");
    	}
    	if(!hasErrors()){
    		applyWithdrawCash.setShopMember(shopMember);
    		if(StringUtils.hasText(applyWithdrawCash.getBankCardNum())){
    			applyWithdrawCash.setType(Short.valueOf("1"));
    		}
    		this.applyWithdrawCashService.addApplyWithdrawCash(applyWithdrawCash);
    	}else{
    		this.error(form, "填写的信息有误,请检查后重新输入");
    	}
        return go("getMyWithdrawingCash");
    }
    
    public boolean judgeApplySums(ShopMember shopMember,double sums){
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.status", Short.valueOf("0"), "=");
    	qo.addQuery("obj.shopMember.id", shopMember.getId(), "=");
    	qo.setLimit(-1);
    	List<ApplyWithdrawCash> list=this.applyWithdrawCashService.getApplyWithdrawCashBy(qo).getResult();
    	if(list!=null){
    		double applySums=0;
        	for (ApplyWithdrawCash applyWithdrawCash2 : list) {
        		applySums+=applyWithdrawCash2.getSums();
    		}
        	if(applySums+sums>shopMember.getRemainderAmt()){
        		return false;
        	}
    	}else{
    		if(shopMember.getRemainderAmt()<sums){
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * 账户提现记录
     * 
     * @param form
     * @return 
     */
    public Page doGetMyWithdrawingCash(WebForm form) {
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.shopMember", shopMember, "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	IPageList iPageList=this.applyWithdrawCashService.getApplyWithdrawCashBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("withdrawCashList", iPageList.getResult());
        return new Page("/userCenter/myWithdrawingCash.html");
    }
    
    /**
     * 余额变动记录
     * 
     * @param form
     * @return 
     */
    public Page doGetRemainderAmtHistory(WebForm form) {
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	shopMember = this.service.getShopMember(shopMember.getId());
    	QueryObject qo = form.toPo(QueryObject.class);
    	qo.addQuery("obj.user", shopMember, "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	IPageList iPageList=this.remainderAmtHistoryService.getRemainderAmtHistoryBy(qo);
    	CommUtil.saveIPageList2WebForm(iPageList, form);
    	form.addResult("remainderAmtHistoryList", iPageList.getResult());
    	form.addResult("fu", formatUtil.fu);
    	BigDecimal b = new BigDecimal(shopMember.getRemainderAmt());
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    	form.addResult("amt", f1);
        return new Page("/userCenter/remainderAmtHistory.html");
    }
    
    /**
     * 取消订单
     */
    public Page doCancelOrder(WebForm form){
    	ShopMember shopMember=(ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	String orderId=CommUtil.null2String(form.get("orderId"));
    	ShopOrderInfo shopOrderInfo = this.shopOrderInfoService.getShopOrderInfo(Long.valueOf(orderId));
    	if(!shopOrderInfo.getUser().getId().equals(shopMember.getId())){
    		return error(form,"你没有权限取消此订单");
    	}
    	shopOrderInfo.setStatus(-1);
    	this.shopOrderInfoService.updateShopOrderInfo(shopOrderInfo.getId(), shopOrderInfo);
    	List<ShopOrderdetail> orderDetails=shopOrderInfo.getOrderdetails();
    	this.shopOrderdetailService.cancelOrder(orderDetails);
    	return go("getMyOrder&menuId=myOrder");
    }
    /**
     * 我的抽奖记录页面
     */
    public Page doToSweepstakeRecord(WebForm form){
    	String currentPage=CommUtil.null2String(form.get("currentPage")); 
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	 QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
         qo.addQuery("obj.user.id",shopMember.getId(), "=");
         qo.setOrderBy("createTime");
         qo.setOrderType("desc");
         IPageList pl = this.sweepstakesRecordService.getSweepstakesRecordBy(qo);
         CommUtil.saveIPageList2WebForm(pl, form);
         form.addResult("pl", pl.getResult());
         form.addResult("menuId", "toSweepstakeRecord");
        return new Page("/userCenter/sweepstakeRecord.html");
    }
    /**
     * 选择抽奖收货地址跳转
     */
    public Page doToAddress(WebForm form){
    	String rid=CommUtil.null2String(form.get("rid"));
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		return new Page("/userCenter/login.html");
    	}
    	QueryObject qo = form.toPo(QueryObject.class);
    	qo.addQuery("obj.user.id", shopMember.getId(), "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	qo.setPageSize(-1);
    	IPageList iPageList=this.shopAddressService.getShopAddressBy(qo);
    	form.addResult("addressList", iPageList.getResult());
        form.addResult("menuId", "toSweepstakeRecord");
        form.addResult("rid", rid);
        return new Page("/userCenter/chooseAddress.html");
    }
    public Page doConfirmAddress(WebForm form){
    	String rid=CommUtil.null2String(form.get("rid"));
    	String aid = CommUtil.null2String(form.get("aid"));
    	SweepstakesRecord sr = this.sweepstakesRecordService.getSweepstakesRecord(Long.parseLong(rid));
    	ShopAddress sa = this.shopAddressService.getShopAddress(Long.parseLong(aid));
    	sr.setAddress(sa);
    	this.sweepstakesRecordService.updateSweepstakesRecord(sr.getId(), sr);
    	return go("toSweepstakeRecord");
    }
    
    /**
     * 确认收货
     * @param form
     * @return
     */
    public Page doConfirmReceipt(WebForm form){
    	ShopMember shopMember = (ShopMember)ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
    	if(shopMember==null){
    		this.addError("msg", "登录超市请重新登录");
    		return pageForExtForm(form);
    	}
    	Integer orderId=CommUtil.null2Int(form.get("orderId"));
    	ShopOrderInfo shopOrderInfo = this.shopOrderInfoService.getShopOrderInfo((long)orderId);
    	if(!shopMember.getId().equals(shopOrderInfo.getUser().getId())){
    		this.addError("msg", "你没有权限修改此订单");
    		return pageForExtForm(form);
    	}
    	shopOrderInfo.setStatus(3);
    	this.shopOrderInfoService.updateShopOrderInfo(shopOrderInfo.getId(), shopOrderInfo);
    	return pageForExtForm(form);
    }
    
	/**
	 * 跳转错误页面
	 * @param form
	 * @return
	 */
	public Page error(WebForm form,String msg){
		form.addResult("msg", msg);
		return new Page("/userCenter/error.html");
	}
	
	public void setApplyRefundService(IApplyRefundService applyRefundService) {
		this.applyRefundService = applyRefundService;
	}

	public void setShopOrderdetailService(
			IShopOrderdetailService shopOrderdetailService) {
		this.shopOrderdetailService = shopOrderdetailService;
	}

	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}

	public void setIntegralHistoryService(
			IIntegralHistoryService integralHistoryService) {
		this.integralHistoryService = integralHistoryService;
	}

	public void setApplyProService(IApplyProService applyProService) {
		this.applyProService = applyProService;
	}

	public void setCommissionWithdrawService(
			ICommissionWithdrawService commissionWithdrawService) {
		this.commissionWithdrawService = commissionWithdrawService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public void setShopDiscussService(IShopDiscussService shopDiscussService) {
		this.shopDiscussService = shopDiscussService;
	}

	public void setShopAddressService(IShopAddressService shopAddressService) {
		this.shopAddressService = shopAddressService;
	}

	public void setSystemRegionService(ISystemRegionService systemRegionService) {
		this.systemRegionService = systemRegionService;
	}

	public void setShopDistributorService(
			IShopDistributorService shopDistributorService) {
		this.shopDistributorService = shopDistributorService;
	}

	public void setService(IShopMemberService service) {
		this.service = service;
	}
	
	public void setCustomCouponService(ICustomCouponService customCouponService) {
		this.customCouponService = customCouponService;
	}

	public void setEntityToolUtil(EntityToolUtil entityToolUtil) {
		this.entityToolUtil = entityToolUtil;
	}

	public void setDeliveryRuleService(IDeliveryRuleService deliveryRuleService) {
		this.deliveryRuleService = deliveryRuleService;
	}

	public void setApplyWithdrawCashService(
			IApplyWithdrawCashService applyWithdrawCashService) {
		this.applyWithdrawCashService = applyWithdrawCashService;
	}

	public void setRemainderAmtHistoryService(
			IRemainderAmtHistoryService remainderAmtHistoryService) {
		this.remainderAmtHistoryService = remainderAmtHistoryService;
	}

	public ISweepstakesRecordService getSweepstakesRecordService() {
		return sweepstakesRecordService;
	}

	public void setSweepstakesRecordService(
			ISweepstakesRecordService sweepstakesRecordService) {
		this.sweepstakesRecordService = sweepstakesRecordService;
	}

	public void setIntegralChangeRuleService(
			IIntegralChangeRuleService integralChangeRuleService) {
		this.integralChangeRuleService = integralChangeRuleService;
	}

	public void setIntegralRechargeRecordService(
			IIntegralRechargeRecordService integralRechargeRecordService) {
		this.integralRechargeRecordService = integralRechargeRecordService;
	}

	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}

	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}
	
}
