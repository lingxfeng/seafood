package com.eastinno.otransos.payment.common.domain;
/**
 * 支付返回实体类
 * @author nsz
 */
public class PayReturnObj {
	private String outTradeNo;//订单号
	private String transactionId;//第三封交易流水号
	private String timeEnd;//服务器返回的交易时间
	public PayReturnObj(String outTradeNo,String transactionId,String timeEnd){
		this.outTradeNo = outTradeNo;
		this.transactionId = transactionId;
		this.timeEnd = timeEnd;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	
}
