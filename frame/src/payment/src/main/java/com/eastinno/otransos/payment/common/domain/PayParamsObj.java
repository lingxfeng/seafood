package com.eastinno.otransos.payment.common.domain;


/**
 * 支付参数封装
 * @author nsz
 */
public class PayParamsObj {
	private PaymentConfig payConfig;
	private String orderId;//订单号
	private String orderName;//订单名称
	private String total_fee;//订单总金额
	private String orderDesc;//订单描述
	private String defaultbank;//默认银行
	private String ip;//客户端ip
	private String userCode;//客户标识支付宝中标识支付宝帐号，微信中标识openId
	/**
	 * 退款所需字段
	 */
	private String unOrderId;//退款订单号
	private String TransactionId;//交易流水号
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getDefaultbank() {
		return defaultbank;
	}
	public void setDefaultbank(String defaultbank) {
		this.defaultbank = defaultbank;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public PaymentConfig getPayConfig() {
		return payConfig;
	}
	public void setPayConfig(PaymentConfig payConfig) {
		this.payConfig = payConfig;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUnOrderId() {
		return unOrderId;
	}
	public void setUnOrderId(String unOrderId) {
		this.unOrderId = unOrderId;
	}
	public String getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}
}
