package com.eastinno.otransos.shop.trade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPayCallOrderService;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.payment.tencent.mpweixin.pay.PayUtil;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.shop.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.shop.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.shop.spokesman.service.ISubsidyService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.shop.trade.service.IShopPayMentService;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
import com.eastinno.otransos.shop.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.shop.usercenter.service.IShopMemberService;
import com.eastinno.otransos.web.ActionContext;
@Component("defaultPayCallService")
public class ShopPayMentServiceImpl implements IShopPayMentService,IPayCallOrderService{
	@Autowired
    private IShopOrderInfoService shopOrderInfoService;
	@Autowired
	private IShopMemberService shopMemberService;
	@Autowired
	private IPaymentConfigService paymentConfigService;	
	@Autowired
	private ISubsidyService subsidyService;
	@Autowired
	private IIntegralRechargeRecordService integralRechargeRecordService;
	@Autowired
	private IRemainderAmtHistoryService remainderAmtHistoryService;
	@Autowired
	private IIntegralHistoryService integralHistoryService;
	
	@Override
	public String paySubmit(ShopOrderInfo orderInfo, PaymentConfig payconfig,
			String defaultbank) {
		orderInfo.setUuid(new Date().getTime() + "");
		orderInfo.setPayConfig(payconfig);
		this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderId(orderInfo.getUuid());
        payParams.setOrderName(orderInfo.getCode());
        payParams.setOrderDesc(orderInfo.getCode());
        payParams.setTotal_fee(orderInfo.getGross_price()+"");
        payParams.setPayConfig(payconfig);
        payParams.setUserCode(orderInfo.getUser().getFollower().getWeixinOpenId());
        if (!"".equals(defaultbank)) {
            payParams.setDefaultbank(defaultbank);
        }
        return PaymentUtil.paysubmitStr(payParams);
	}
	@Override
	public void updateOrder(PayReturnObj payreturn) {
		String uuid = payreturn.getOutTradeNo();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.uuid", uuid, "=");
        qo.setPageSize(1);
        List<ShopOrderInfo> list = this.shopOrderInfoService.getShopOrderInfoBy(qo).getResult();
        if (list != null && list.size() > 0) {
        	ShopOrderInfo order = list.get(0);
            if (order != null && order.getStatus() != 1) {
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
        }else{
			updateRecord(payreturn);
		}
	}
	@Override
	public String getReturnUrl() {
		return "/goShop.java";
	}
	/**
	 * 提现
	 * @param member
	 * @param amt
	 * @return
	 */
	public boolean withdrawcash(CommissionWithdraw withdraw){
		String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();  
        path=path.replace("file:", ""); //去掉file:  
        path=path.replace("classes/", ""); //去掉class\
        path = path+"apiclient_cert.p12";
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.type",PayTypeE.WEIXINTURNLQ,"=");
        Tenant t = ShiroUtils.getTenant();
        qo.addQuery("obj.tenant",t,"=");
        PaymentConfig config = null;
        List<PaymentConfig> configList = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
        if(configList!=null && configList.size()>0){
        	config=configList.get(0);
        }
        if(config==null){
        	return false;
        }
        String uuid = System.currentTimeMillis()+"";
        withdraw.setUuid(uuid);
        PayParamsObj payParams = new PayParamsObj();
        payParams.setPayConfig(config);
        payParams.setOrderId(withdraw.getUuid());
        payParams.setTotal_fee(withdraw.getCommission()+"");
        ShopMember member = withdraw.getUser();
        payParams.setOrderName(member.getNickname()+"体现记录");
        payParams.setUserCode(member.getFollower().getWeixinOpenId());
        PayUtil.setPerPath(path);
        Map<String,Object> returnMap = PayUtil.tranWeixin(payParams);
        if("SUCCESS".equals(returnMap.get("result_code"))){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean withdrawcash(ApplyWithdrawCash applyWithdrawCash) {
		String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();  
        path=path.replace("file:", ""); //去掉file:  
        path=path.replace("classes/", ""); //去掉class\
        path = path+"apiclient_cert.p12";
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.type",PayTypeE.WEIXINTURNLQ,"=");
        Tenant t = ShiroUtils.getTenant();
        qo.addQuery("obj.tenant",t,"=");
        PaymentConfig config = null;
        List<PaymentConfig> configList = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
        if(configList!=null && configList.size()>0){
        	config=configList.get(0);
        }
        if(config==null){
        	return false;
        }
        config.setPerPath(path);
        PayParamsObj payParams = new PayParamsObj();
        payParams.setPayConfig(config);
        payParams.setOrderId(applyWithdrawCash.getUuid());
        payParams.setTotal_fee(applyWithdrawCash.getSums()+"");
        ShopMember member = applyWithdrawCash.getShopMember();
        payParams.setOrderName(member.getNickname()+"提现记录");
        payParams.setUserCode(member.getFollower().getWeixinOpenId());
        
        PayUtil.setPerPath(path);
        Map<String,Object> returnMap = PayUtil.tranWeixin(payParams);
        if("SUCCESS".equals(returnMap.get("result_code"))){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 退款
	 * @param orderInfo
	 * @return
	 */
	public boolean refund(ShopOrderInfo orderInfo){
		String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();  
        path=path.replace("file:", ""); //去掉file:  
        path=path.replace("classes/", ""); //去掉class\
        path = path+"apiclient_cert.p12";
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.type",PayTypeE.WEIXINTURNLQ,"=");
        Tenant t = ShiroUtils.getTenant();
        qo.addQuery("obj.tenant",t,"=");
        PaymentConfig config = null;
        List<PaymentConfig> configList = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
        if(configList!=null && configList.size()>0){
        	config=configList.get(0);
        }
        if(config==null){
        	return false;
        }
        String unUuid = System.currentTimeMillis()+"";
        orderInfo.setUnuuid(unUuid);
        PayParamsObj payParams = new PayParamsObj();
        payParams.setPayConfig(config);
        payParams.setOrderId(orderInfo.getUuid());
        payParams.setUnOrderId(orderInfo.getUnuuid());
        payParams.setTransactionId(orderInfo.getTradeCode());
        payParams.setTotal_fee(orderInfo.getGross_price()+"");
        ShopMember member = orderInfo.getUser();
        payParams.setOrderName(member.getNickname()+"体现记录");
        payParams.setUserCode(member.getFollower().getWeixinOpenId());
        PayUtil.setPerPath(path);
        Map<String,Object> returnMap = PayUtil.returnPay(payParams);
		String return_code =returnMap.get("return_code")+"";
		String result_code = returnMap.get("result_code")+"";
		if("SUCCESS".equals(return_code)&& "SUCCESS".equals(result_code)){
			String refund_id = returnMap.get("refund_id")+"";
			String payment_time = new Date().getTime()+"";
			orderInfo.setUntradeCode(refund_id);
			orderInfo.setUntradeTime(payment_time);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 使用优惠券支付
	 */
	@Override
	public String paySubmit2(ShopOrderInfo orderInfo, PaymentConfig payconfig,String defaultbank,CustomCoupon customCoupon) {
		orderInfo.setUuid(new Date().getTime() + "");
		orderInfo.setPayConfig(payconfig);
		if(customCoupon!=null){
			Double cValue = customCoupon.getCoupon().getValue();
			orderInfo.setMyCoupon(customCoupon);
			orderInfo.setCoupon_price(cValue);
			orderInfo.setGross_price(orderInfo.getGross_price()-cValue);
		}
		this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderId(orderInfo.getUuid());
        payParams.setOrderName(orderInfo.getCode());
        payParams.setOrderDesc(orderInfo.getCode());
        payParams.setTotal_fee(orderInfo.getGross_price()+"");
        payParams.setPayConfig(payconfig);
        if(orderInfo.getUser().getFollower()!=null){
        	payParams.setUserCode(orderInfo.getUser().getFollower().getWeixinOpenId());
        }
        if (!"".equals(defaultbank)) {
            payParams.setDefaultbank(defaultbank);
        }
        return PaymentUtil.paysubmitStr(payParams);
	}
	
	@Override
	public String paySubmitByAmt(ShopOrderInfo orderInfo,PaymentConfig payconfig, String defaultbank,CustomCoupon customCoupon, ShopMember member) {
		orderInfo.setUuid(new Date().getTime() + "");
		orderInfo.setPayConfig(payconfig);
		if(customCoupon!=null){
			Double cValue = customCoupon.getCoupon().getValue();
			orderInfo.setMyCoupon(customCoupon);
			orderInfo.setCoupon_price(cValue);
			orderInfo.setGross_price(orderInfo.getGross_price()-cValue);
		}
		
		String total_fee="";
		double rAmt = member.getRemainderAmt();//余额
		double gPri = orderInfo.getGross_price();//订单价格
		if(rAmt>=gPri){
			orderInfo.setBalancePay(gPri);
			this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
			/*BigDecimal amt = new BigDecimal(Double.toString(member.getRemainderAmt()));
	        BigDecimal price = new BigDecimal(Double.toString(orderInfo.getGross_price()));
			member.setRemainderAmt(amt.subtract(price).doubleValue());
			this.shopMemberService.updateShopMember(member.getId(), member);*/
			//this.remainderAmtHistoryService.addRemainderAmtHistory(member, 1, orderInfo.getGross_price(), member.getNickname()+"购物消费余额");
			return "0";
		}else{
			if(member.getRemainderAmt()<0){
				return "1";
			}
			BigDecimal amt = new BigDecimal(Double.toString(rAmt));
	        BigDecimal price = new BigDecimal(Double.toString(gPri));
			Double sjzf=price.subtract(amt).doubleValue();
			total_fee=sjzf.toString();
			//this.remainderAmtHistoryService.addRemainderAmtHistory(member, 1, member.getRemainderAmt(), member.getNickname()+"购物消费余额");
			orderInfo.setBalancePay(rAmt);
			this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
			//member.setRemainderAmt(Double.valueOf("0"));
			//this.shopMemberService.updateShopMember(member.getId(), member);
		}
		
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderId(orderInfo.getUuid());
        payParams.setOrderName(orderInfo.getCode());
        payParams.setOrderDesc(orderInfo.getCode());
        payParams.setTotal_fee(total_fee);
        payParams.setPayConfig(payconfig);
        if(orderInfo.getUser().getFollower()!=null){
        	payParams.setUserCode(orderInfo.getUser().getFollower().getWeixinOpenId());
        }
        if (!"".equals(defaultbank)) {
            payParams.setDefaultbank(defaultbank);
        }
        return PaymentUtil.paysubmitStr(payParams);
	}
	
	/**
	 * 手机端支付
	 * @param orderInfo
	 * @param payconfig
	 * @param defaultbank
	 * @param customCoupon
	 * @param member
	 * @return
	 */
	@Override
	public String paySubmitByAmt2(ShopOrderInfo orderInfo,PaymentConfig payconfig, String defaultbank,ShopMember member) {
		orderInfo.setUuid(new Date().getTime() + "");
		orderInfo.setPayConfig(payconfig);
		String total_fee="";
		Double rAmt = member.getRemainderAmt();//余额
		Double gPri = orderInfo.getGross_price();//订单价格
		if(rAmt<=0){
			return "-1";
		}
		if(rAmt>=gPri){
			orderInfo.setBalancePay(gPri);
			this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
			return "0";
		}else{
			BigDecimal amt = new BigDecimal(Double.toString(rAmt));
	        BigDecimal price = new BigDecimal(Double.toString(gPri));
			Double sjzf=price.subtract(amt).doubleValue();
			total_fee=sjzf.toString();
			orderInfo.setBalancePay(rAmt);
			this.shopOrderInfoService.updateShopOrderInfo(orderInfo.getId(), orderInfo);
		}
		
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderId(orderInfo.getUuid());
        payParams.setOrderName(orderInfo.getCode());
        payParams.setOrderDesc(orderInfo.getCode());
        payParams.setTotal_fee(total_fee);
        payParams.setPayConfig(payconfig);
        if(orderInfo.getUser().getFollower()!=null){
        	payParams.setUserCode(orderInfo.getUser().getFollower().getWeixinOpenId());
        }
        if (!"".equals(defaultbank)) {
            payParams.setDefaultbank(defaultbank);
        }
        return PaymentUtil.paysubmitStr(payParams);
	}
	
	@Override
	public boolean withdrawcash(Double sums, ShopMember member, String uuid) {
		String path=Thread.currentThread().getContextClassLoader().getResource("/").toString();  
        path=path.replace("file:", ""); //去掉file:  
        path=path.replace("classes/", ""); //去掉class\
        path = path+"apiclient_cert.p12";
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.type",PayTypeE.WEIXINTURNLQ,"=");
        Tenant t = ShiroUtils.getTenant();
        qo.addQuery("obj.tenant",t,"=");
        PaymentConfig config = null;
        List<PaymentConfig> configList = this.paymentConfigService.getPaymentConfigBy(qo).getResult();
        if(configList!=null && configList.size()>0){
        	config=configList.get(0);
        }
        if(config==null){
        	return false;
        }
        PayParamsObj payParams = new PayParamsObj();
        payParams.setPayConfig(config);
        payParams.setOrderId(uuid);
        payParams.setTotal_fee(sums+"");
        payParams.setOrderName(member.getNickname()+"体现记录");
        payParams.setUserCode(member.getFollower().getWeixinOpenId());
        PayUtil.setPerPath(path);
        Map<String,Object> returnMap = PayUtil.tranWeixin(payParams);
        if("SUCCESS".equals(returnMap.get("result_code"))){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 积分充值
	 * @param member
	 * @param amt
	 * @return
	 */
	public String rechargePaySubmit(IntegralRechargeRecord rechargeRecord,PaymentConfig payconfig,String defaultbank){
		rechargeRecord.setUuid(new Date().getTime() + "");
		rechargeRecord.setPayConfig(payconfig);
		this.integralRechargeRecordService.updateIntegralRechargeRecord(rechargeRecord.getId(), rechargeRecord);
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderId(rechargeRecord.getUuid());
        payParams.setOrderName(rechargeRecord.getCode());
        payParams.setOrderDesc(rechargeRecord.getCode());
        payParams.setTotal_fee(rechargeRecord.getGross_price()+"");
        payParams.setPayConfig(payconfig);
        payParams.setUserCode(rechargeRecord.getMember().getFollower().getWeixinOpenId());
        if (!"".equals(defaultbank)) {
            payParams.setDefaultbank(defaultbank);
        }
        return PaymentUtil.paysubmitStr(payParams);		
	}
	
	@Override
	public void updateRecord(PayReturnObj payreturn) {
		String uuid = payreturn.getOutTradeNo();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.uuid", uuid, "=");
        qo.setPageSize(1);
        List<IntegralRechargeRecord> list = this.integralRechargeRecordService.getIntegralRechargeRecordBy(qo).getResult();
        if (list != null && list.size() > 0) {
        	IntegralRechargeRecord irr = list.get(0);
            if (irr != null && irr.getStatus() != 1) {
            	irr.setTradeCode(payreturn.getTransactionId());
            	irr.setTradeDate(new Date());
            	ShopMember member = irr.getMember();
            	if(member != null){
            		member.setAvailableIntegral(member.getAvailableIntegral() + irr.getIntegral());
            		member.setTotalIntegral(member.getTotalIntegral() + irr.getIntegral());
            		this.shopMemberService.updateShopMember(member.getId(),member);
            	}
                
                if(irr.getStatus()==0){
                	irr.setStatus(Short.parseShort("1"));
        			System.out.println("=======充值订单："+irr.getCode()+"更改了状态");
        		}
        		this.integralRechargeRecordService.updateIntegralRechargeRecord(irr.getId(), irr);
        		this.integralHistoryService.saveIntegralHistory(irr.getIntegral(), member, member.getNickname()+"充值获取积分", 2);//积分历史记录
            }
        }
	}
	
	@Override
	public String paySubmit3(IntegralRechargeRecord integralRechargeRecord,PaymentConfig payconfig, String defaultbank) {
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderId(integralRechargeRecord.getUuid());
        payParams.setOrderName(integralRechargeRecord.getCode());
        payParams.setOrderDesc(integralRechargeRecord.getCode());
        payParams.setTotal_fee(integralRechargeRecord.getGross_price()+"");
        payParams.setPayConfig(payconfig);
        if(integralRechargeRecord.getMember().getFollower()!=null){
        	payParams.setUserCode(integralRechargeRecord.getMember().getFollower().getWeixinOpenId());
        }
        if (!"".equals(defaultbank)) {
            payParams.setDefaultbank(defaultbank);
        }
        return PaymentUtil.paysubmitStr(payParams);
	}
	
	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}
	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
	public IPaymentConfigService getPaymentConfigService() {
		return paymentConfigService;
	}
	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}
	public ISubsidyService getSubsidyService() {
		return subsidyService;
	}
	public void setSubsidyService(ISubsidyService subsidyService) {
		this.subsidyService = subsidyService;
	}
	public IIntegralRechargeRecordService getIntegralRechargeRecordService() {
		return integralRechargeRecordService;
	}
	public void setIntegralRechargeRecordService(
			IIntegralRechargeRecordService integralRechargeRecordService) {
		this.integralRechargeRecordService = integralRechargeRecordService;
	}
}
