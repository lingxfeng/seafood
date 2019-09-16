package com.eastinno.otransos.shop.promotions.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.shop.core.action.WxShopBaseAction;
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.service.ICustomCouponService;
import com.eastinno.otransos.shop.spokesman.service.ISubsidyService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.util.formatUtil;

/**
 * CustomCouponAction
 * @author 
 */
@Action
public class CustomCouponAction extends WxShopBaseAction {
    @Inject
    private ICustomCouponService service;
    @Inject
    private IShopOrderInfoService shopOrderInfoService;
    @Inject
    private IDeliveryRuleService deliveryRuleService;
    @Inject
    private ISubsidyService subsidyService;
    /**
     * 客户持有优惠券列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	ShopMember member = this.getShopMember(form);
    	if(member==null){
    		return error(form,"操作超时或无法获取用户信息");
    	}
    	QueryObject qo = new QueryObject();
    	qo.addQuery("obj.status", Short.parseShort("3"), "!=");
    	qo.addQuery("obj.shopMember", member, "=");
    	if(member.getDisType()!= 0){
    		qo.addQuery("obj.coupon.useType",Short.parseShort("0"), "=");
    	}
    	qo.setPageSize(-1);
    	qo.setOrderBy("status");
        List<CustomCoupon> cclist =this.service.getCustomCouponBy(qo).getResult();
        form.addResult("pl", cclist);
        return new Page("/bcd/wxshop/promotions/coupons/coupon_sure.html");
    }
    /**
     * 客户已使用优惠券列表页面
     * 
     * @param form
     */
    public Page doUsedList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getCustomCouponBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    /**
     * 检查是否有可用优惠券
     * 
     * @param form
     */
    public Page doCheckCoupon(WebForm form) {
    	String orderid = CommUtil.null2String(form.get("orderid"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderid));
    	/**
    	 * 获取用户的有效优惠券
    	 */
    	ShopMember member = this.getShopMember(form);
    	if(member==null){
    		return error(form,"操作超时或无法获取用户信息");
    	}
    	int flag = 0;
    	List<CustomCoupon> list = this.service.judgeCustomCoupon(member,order);
    	if(list != null && list.size() != 0){
    		flag = list.size();
    	}
    	Map map = new HashMap();
    	map.put("flag", flag);
    	form.jsonResult(map);
        return Page.JSONPage;
    }
    /**
     * 跳转可选择优惠券列表
     * 
     * @param form
     */
    public Page doChooseCoupon(WebForm form) {
    	String orderid = CommUtil.null2String(form.get("orderid"));
    	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderid)); 
    	/**
    	 * 获取用户的有效优惠券
    	 */
    	ShopMember member = this.getShopMember(form);
    	if(member==null){
    		return error(form,"操作超时或无法获取用户信息");
    	}
    	int flag = 0;
    	List<CustomCoupon> list = this.service.judgeCustomCoupon(member,order);
    	form.addResult("pl", list);
    	form.addResult("orderid", orderid);
        return new Page("/bcd/wxshop/promotions/coupons/validcoupons.html");
    }
    
    /**
     * 选择优惠券
     * 
     * @param form
     */
    public Page doValidCouponList(WebForm form) {
        Long couponid = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("couponid"))));
        String orderid = CommUtil.null2String(form.get("orderid"));
        ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderid));
        ShopMember member = order.getUser();
        member = this.shopMemberService.getShopMember(member.getId());
        CustomCoupon coupon = this.service.getCustomCoupon(couponid);
        Map<Long, Double> costMap = this.deliveryRuleService.getDeliveryCostMap(order);
        form.addResult("costMap", costMap);
        form.addResult("coupon", coupon);
        form.addResult("order", order);
        form.addResult("rAmt", member.getRemainderAmt());
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/wxshop/trading/orderDetails.html");
    } 
    
    /**
     * 使用优惠券之后，更改订单价格
     * 
     * @param form
     */
    public Page doUpdateOrder(WebForm form) {
    	String c = CommUtil.null2String(form.get("c"));//是否佣金支付
    	String couponid = CommUtil.null2String(form.get("couponid"));
        String orderid = CommUtil.null2String(form.get("orderid"));
        Map<String,String> map = new HashMap<String,String>();
        //
        ShopMember member = (ShopMember)ActionContext.getContext().getSession().getAttribute("SHOPMEMBER");
        if(member==null){
    		map.put("success", "false");
    		map.put("msg", "登录超时请重新登陆");
    		form.jsonResult(map);
            return Page.JSONPage;
    	}
        member = this.shopMemberService.getShopMember(member.getId());
        if(!"".equals(orderid)){
        	ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(Long.parseLong(orderid));
        	//
        	if(order.getUser().getId()!=member.getId()){
        		map.put("success", "false");
        		map.put("msg", "登录超时请重新登陆");
        		form.jsonResult(map);
                return Page.JSONPage;
        	}
        	if(!"".equals(couponid)){
        		CustomCoupon coupon = this.service.getCustomCoupon(Long.parseLong(couponid));
        		if(order.getMyCoupon() == null){
        			order.setCoupon_price(coupon.getCoupon().getValue());
        			BigDecimal gross_price = new BigDecimal(Double.toString(order.getGross_price()));
    				BigDecimal couponValue = new BigDecimal(Double.toString(coupon.getCoupon().getValue()));
            		order.setGross_price(gross_price.subtract(couponValue).doubleValue());
            		order.setMyCoupon(coupon);
            		coupon.setStatus(Short.parseShort("1"));
            		this.service.updateCustomCoupon(coupon.getId(), coupon);
            		this.shopOrderInfoService.updateShopOrderInfo(order.getId(), order);
            		map.put("flag", "已根据优惠券修改订单！");
        		}	
        	}
        	//
        	if("true".equals(c) && member.getRemainderAmt()>0){
        		Double rAmt = member.getRemainderAmt();//账户金额
        		Double gross_price=order.getGross_price();//订单总价格
        		if(rAmt>=gross_price){
        			order.setBalancePay(gross_price);
        			map.put("success", "true");
        			map.put("status", "0");//直接走虚拟支付
        			PayReturnObj pro = new PayReturnObj(order.getUuid(), order.getCode(), new Date().getTime()+"");
        			this.updOrder(order, pro);
        		}else{
        			map.put("success", "true");
        			map.put("status", "1");
        		}
            }
        }
        
        form.jsonResult(map);
        return Page.JSONPage;
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
    
    
    public void setService(ICustomCouponService service) {
        this.service = service;
    }
	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	public ICustomCouponService getService() {
		return service;
	}
	public IDeliveryRuleService getDeliveryRuleService() {
		return deliveryRuleService;
	}
	public void setDeliveryRuleService(IDeliveryRuleService deliveryRuleService) {
		this.deliveryRuleService = deliveryRuleService;
	}
	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}
	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}
    
}