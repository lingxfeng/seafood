package com.eastinno.otransos.payment.common.service;

import com.eastinno.otransos.payment.common.domain.PayReturnObj;
/**
 * 支付系统回调处理订单接口
 * @author Administrator
 *
 */
public interface IPayCallOrderService {
	/**
	 * 更新订单
	 * @param params
	 */
	void updateOrder(PayReturnObj payreurnObj);
	/**
	 * 返回路径
	 * @return
	 */
	String getReturnUrl();
	/**
	 * 更新充值订单
	 * @param params
	 */
	void updateRecord(PayReturnObj payreurnObj);
}
