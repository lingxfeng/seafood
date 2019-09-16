package com.eastinno.otransos.shop.trade.service;

import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.shop.distribu.domain.CommissionWithdraw;
import com.eastinno.otransos.shop.promotions.domain.CustomCoupon;
import com.eastinno.otransos.shop.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.ApplyWithdrawCash;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;
/**
 * 支付接口
 * @author nsz
 *
 */
public interface IShopPayMentService {
	String paySubmit(ShopOrderInfo orderInfo,PaymentConfig payconfig,String defaultBland);
	/**
	 * 提现
	 * @param member
	 * @param amt
	 * @return
	 */
	boolean withdrawcash(CommissionWithdraw withdraw);
	/**
	 * 退款
	 * @param orderInfo
	 * @return
	 */
	boolean refund(ShopOrderInfo orderInfo);
	
	/**
	 * 
	 * @param orderInfo 订单
	 * @param payconfig 支付方式配置(支付宝、银联、微信)
	 * @param defaultbank 
	 * @param customCoupon
	 * @return
	 */
	public String paySubmit2(ShopOrderInfo orderInfo, PaymentConfig payconfig,String defaultbank,CustomCoupon customCoupon);
	
	/**
	 * 余额支付订单
	 * @param orderInfo 订单
	 * @param payconfig 支付方式配置(支付宝、银联、微信)
	 * @param defaultbank 
	 * @param customCoupon
	 * @return
	 */
	public String paySubmitByAmt(ShopOrderInfo orderInfo, PaymentConfig payconfig,String defaultbank,CustomCoupon customCoupon,ShopMember member);
	
	/**
	 * 手机端余额支付订单
	 * @param orderInfo 订单
	 * @param payconfig 支付方式配置(支付宝、银联、微信)
	 * @param defaultbank 
	 * @param customCoupon
	 * @return
	 */
	public String paySubmitByAmt2(ShopOrderInfo orderInfo, PaymentConfig payconfig,String defaultbank,ShopMember member);
	
	/**
	 * 提现
	 * @param member
	 * @param amt
	 * @return
	 */
	boolean withdrawcash(Double sums,ShopMember member,String uuid);
	
		/**
	 * 积分充值
	 * @param member
	 * @param amt
	 * @return
	 */
	String rechargePaySubmit(IntegralRechargeRecord rechargeRecord,PaymentConfig payconfig,String defaultBland);
		void updateRecord(PayReturnObj payreturn);
	
	/**
	 * 提现
	 * @param member
	 * @param amt
	 * @return
	 */
	boolean withdrawcash(ApplyWithdrawCash applyWithdrawCash);
	
	/**
	 * 
	 * @param orderInfo 订单
	 * @param payconfig 支付方式配置(支付宝、银联、微信)
	 * @param defaultbank 
	 * @param customCoupon
	 * @return
	 */
	public String paySubmit3(IntegralRechargeRecord integralRechargeRecord, PaymentConfig payconfig,String defaultbank);
}
