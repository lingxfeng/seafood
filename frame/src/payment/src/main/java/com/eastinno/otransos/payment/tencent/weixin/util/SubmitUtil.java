package com.eastinno.otransos.payment.tencent.weixin.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.payment.tencent.weixin.AccessTokenRequestHandler;
import com.eastinno.otransos.payment.tencent.weixin.ClientRequestHandler;
import com.eastinno.otransos.payment.tencent.weixin.PackageRequestHandler;
import com.eastinno.otransos.payment.tencent.weixin.PrepayIdRequestHandler;

public class SubmitUtil {
	public static String toSubmit(PayParamsObj payParams) throws Exception {
		String msg = "";
		HttpServletRequest req = PaymentUtil.getReq();
		HttpServletResponse res = PaymentUtil.getRes();
		// 接收财付通通知的URL
		String domain=PaymentUtil.getDomain();
		String notify_url = domain+ConstantUtil.NotifyUrl;
		System.out.println("回调为"+notify_url);
		PackageRequestHandler packageReqHandler = new PackageRequestHandler(
				req, res);// 生成package的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(
				req, res);// 获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler(req, res);// 返回客户端支付参数的请求类
		packageReqHandler.setKey(ConstantUtil.PARTNER_KEY);
		// 获取token值
		String token = AccessTokenRequestHandler.getAccessToken();
		JSONObject map = new JSONObject();
		if (!"".equals(token)) {
			// 设置package订单参数
			packageReqHandler.setParameter("bank_type", "WX");// 银行渠道
			packageReqHandler.setParameter("body", payParams.getOrderName()); // 商品描述
			packageReqHandler.setParameter("notify_url", notify_url); // 接收财付通通知的URL
			packageReqHandler.setParameter("partner", ConstantUtil.PARTNER); // 商户号
			packageReqHandler.setParameter("out_trade_no", payParams.getOrderId()); // 商家订单号
			packageReqHandler.setParameter("total_fee",payParams.getTotal_fee()); // 商品金额,以分为单位
			packageReqHandler.setParameter("spbill_create_ip",
					req.getRemoteAddr()); // 订单生成的机器IP，指用户浏览器端IP
			packageReqHandler.setParameter("fee_type", "1"); // 币种，1人民币 66
			packageReqHandler.setParameter("input_charset", "UTF-8"); // 字符编码

			// 获取package包
			String packageValue = packageReqHandler.getRequestURL();

			String noncestr = WXUtil.getNonceStr();
			String timestamp = WXUtil.getTimeStamp();
			String traceid = "";
			// //设置获取prepayid支付参数
			prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
			prepayReqHandler.setParameter("appkey", ConstantUtil.APP_KEY);
			prepayReqHandler.setParameter("noncestr", noncestr);
			prepayReqHandler.setParameter("package", packageValue);
			prepayReqHandler.setParameter("timestamp", timestamp);
			prepayReqHandler.setParameter("traceid", traceid);

			// 生成获取预支付签名
			String sign = prepayReqHandler.createSHA1Sign();
			// 增加非参与签名的额外参数
			prepayReqHandler.setParameter("app_signature", sign);
			prepayReqHandler.setParameter("sign_method",
					ConstantUtil.SIGN_METHOD);
			String gateUrl = ConstantUtil.GATEURL + token;
			prepayReqHandler.setGateUrl(gateUrl);

			// 获取prepayId
			String prepayid = prepayReqHandler.sendPrepay();
			// 吐回给客户端的参数
			if (null != prepayid && !"".equals(prepayid)) {
				// 输出参数列表
				clientHandler.setParameter("appid", ConstantUtil.APP_ID);
				clientHandler.setParameter("appkey", ConstantUtil.APP_KEY);
				clientHandler.setParameter("noncestr", noncestr);
				// clientHandler.setParameter("package", "Sign=" +
				// packageValue);
				clientHandler.setParameter("package", "Sign=WXPay");
				clientHandler.setParameter("partnerid", ConstantUtil.PARTNER);
				clientHandler.setParameter("prepayid", prepayid);
				clientHandler.setParameter("timestamp", timestamp);
				// 生成签名
				sign = clientHandler.createSHA1Sign();
				clientHandler.setParameter("sign", sign);

				map.put("retcode", "0");
				map.put("retmsg", "OK");
				map.put("appid", ConstantUtil.APP_ID);
				map.put("appkey", ConstantUtil.APP_KEY);
				map.put("noncestr", noncestr);
				map.put("package", "Sign=WXPay");
				map.put("partnerid", ConstantUtil.PARTNER);
				map.put("prepayid", prepayid);
				map.put("timestamp", timestamp);
				map.put("sign", sign);
			} else {
				map.put("retcode", "-2");
				map.put("retmsg", "错误：获取prepayId失败");
			}
		} else {
			map.put("retcode", "-1");
			map.put("retmsg", "错误：获取不到Token");
		}
		msg = map.toJSONString();
		System.out.println("支付已经开始，请求参数："+msg);
		return msg;
	}
}
