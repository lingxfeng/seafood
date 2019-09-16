package com.eastinno.otransos.payment.common.domain;
/**
 * 支付类型
 * @author nsz
 */
public enum PayTypeE {
	ALIPAYMWEB,// 支付宝支付手机网页版
	ALIPAYNET,// 支付宝网银支付
	ALIPAYPCWEB,// 支付宝支付pc网页版
	AliPAYSDK,// 支付宝快捷支付
	YLAPP,// 银联app支付
	YLPC,// 银联pc支付
	TENPAY,// 微信开放平台app支付
	WEIXINMPAPI,//微信支付mpapi支付
	WEIXINMPSM,//扫码支付
	WEIXINTURNLQ//转账到零钱
}
