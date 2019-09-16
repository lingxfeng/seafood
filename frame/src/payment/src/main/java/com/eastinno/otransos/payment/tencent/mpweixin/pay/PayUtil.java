package com.eastinno.otransos.payment.tencent.mpweixin.pay;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.payment.tencent.mpweixin.utils.GetWxOrderno;
import com.eastinno.otransos.payment.tencent.mpweixin.utils.RequestHandler;
import com.eastinno.otransos.payment.tencent.mpweixin.utils.Sha1Util;
import com.eastinno.otransos.payment.tencent.mpweixin.utils.TenpayUtil;


/**
 * @author ex_yangxiaoyi
 * 
 */
public class PayUtil {
	//微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	private static String appid = "wx67e5479ae239e9b2";
	private static String appsecret = "2f52588e2bff415dcd0309292baa75d0";
	private static String partner = "1261539901";
	//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private static String partnerkey = "yufeiweixinpayforyilianyibang999";
	//openId 是微信用户针对公众号的标识，授权的部分这里不解释
	private static String openId = "";
	//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	private static String notifyurl = PaymentUtil.getDomain()+"wx_apijspay_notify.otr";// Key
	private static String perPath="";
	

	public static void setPerPath(String perPath) {
		PayUtil.perPath = perPath;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//微信支付jsApi
		WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setOpenId(openId);
		tpWxPay.setBody("商品信息");
		tpWxPay.setOrderId(getNonceStr());
		tpWxPay.setSpbillCreateIp("127.0.0.1");
		tpWxPay.setTotalFee("0.01");
	    //getPackage(tpWxPay);
	    
	    //扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setBody("商品信息");
	    tpWxPay1.setOrderId(getNonceStr());
	    tpWxPay1.setSpbillCreateIp("127.0.0.1");
	    tpWxPay1.setTotalFee("0.01");
		//getCodeurl(tpWxPay1);
		
		//给普通粉丝转钱
		WxPayDto tpWxPay2 = new WxPayDto();
		tpWxPay2.setOpenId(openId);
		tpWxPay2.setOrderId(getNonceStr());
		tpWxPay2.setSpbillCreateIp("127.0.0.1");
		tpWxPay2.setTotalFee("0.01");
		//PayUtil.tranWeixin(tpWxPay2);
	}
	
	/**
	 * 获取微信扫码支付二维码连接
	 */
	public static String getCodeurl(PayParamsObj payParams){
		PaymentConfig config = payParams.getPayConfig();
		if(config!=null){
			appid = config.getBargainorId();
			appsecret = config.getBargainorKey();
			partner = config.getSeller_email();
			partnerkey = config.getPartnerkey();
		}
		// 1 参数
		// 订单号
		String orderId = payParams.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(payParams.getTotal_fee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = PaymentUtil.getIP();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = payParams.getOrderName();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		
		code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------"+code_url);
		return code_url;
	}
	
	
	/**
	 * 获取请求预支付id报文
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getPackage(PayParamsObj payParams) {
		PaymentConfig config = payParams.getPayConfig();
		if(config!=null){
			appid = config.getBargainorId();
			appsecret = config.getBargainorKey();
			partner = config.getSeller_email();
			partnerkey = config.getPartnerkey();
		}
		String openId = payParams.getUserCode();
		// 1 参数
		// 订单号
		String orderId = payParams.getOrderId();
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(payParams.getTotal_fee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = PaymentUtil.getIP();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "JSAPI";

		// ---必须参数
		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = payParams.getOrderName();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" 
				+ "<mch_id>"+ mch_id + "</mch_id>" 
				+ "<nonce_str>" + nonce_str+ "</nonce_str>" 
				+ "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no+ "</out_trade_no>" 
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip+ "</spbill_create_ip>" 
				+ "<notify_url>" + notify_url+ "</notify_url>" 
				+ "<trade_type>" + trade_type+ "</trade_type>" 
				+ "<openid>" + openId + "</openid>"
				+ "</xml>";
		String prepay_id = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		System.out.println("支付请求参数"+xml);
		prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);

		System.out.println("获取到的预支付ID：" + prepay_id);
		
		
		//获取prepay_id后，拼接最后请求支付所需要的package
		
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", appid);  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		
		String finaPackage = "\"appId\":\"" + appid + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\"";

		System.out.println("V3 jsApi package:"+finaPackage);
		return finaPackage;
	}
	/**
	 * 微信转账
	 * @param tpWxPayDto
	 * @return
	 */
	public static Map<String,Object> tranWeixin(PayParamsObj payParams){
		PaymentConfig config = payParams.getPayConfig();
		if(config!=null){
			appid = config.getBargainorId();
			appsecret = config.getBargainorKey();
			partner = config.getSeller_email();
			partnerkey = config.getPartnerkey();
			perPath = config.getPerPath();
		}
				// 订单号
				String orderId = payParams.getOrderId();
				// 总金额以分为单位，不带小数点
				String totalFee = getMoney(payParams.getTotal_fee());
				
				// 订单生成的机器 IP
				String spbill_create_ip = PaymentUtil.getIP();
				// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。

				// 商户号
				String mch_id = partner;
				// 随机字符串
				String nonce_str = getNonceStr();

				// 商品描述根据情况修改
				String body = payParams.getOrderName();
				// 商户订单号
				String out_trade_no = orderId;
				//用户微信号
				String openid = payParams.getUserCode();
				SortedMap<String, String> packageParams = new TreeMap<String, String>();
				packageParams.put("mch_appid", appid);
				packageParams.put("mchid", mch_id);
				packageParams.put("nonce_str", nonce_str);
				packageParams.put("desc", body);
				packageParams.put("partner_trade_no", out_trade_no);
				packageParams.put("openid", openid);
				packageParams.put("check_name", "NO_CHECK");//不进行实名校验

				// 这里写的金额为1 分到时修改
				packageParams.put("amount", totalFee);
				packageParams.put("spbill_create_ip", spbill_create_ip);


				RequestHandler reqHandler = new RequestHandler(null, null);
				reqHandler.init(appid, appsecret, partnerkey);

				String sign = reqHandler.createSign(packageParams);
				String xml = "<xml>" + "<mch_appid>" + appid + "</mch_appid>"
						+ "<mchid>"+ mch_id + "</mchid>"
						+ "<nonce_str>" + nonce_str+ "</nonce_str>"
						+ "<sign>" + sign + "</sign>"
						+ "<desc><![CDATA[" + body + "]]></desc>"
						+ "<partner_trade_no>" + out_trade_no+ "</partner_trade_no>"
						+"<openid>"+openid+"</openid>"
						+"<check_name>NO_CHECK</check_name>"
						+ "<amount>" + totalFee + "</amount>"
						+ "<spbill_create_ip>" + spbill_create_ip+ "</spbill_create_ip>"
						+  "</xml>";
				String createOrderURL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
				
				String returnxml =post(getHttpClient(perPath, partner), createOrderURL, xml);
				System.out.println("退款返回xml结果："+returnxml);
				Map<String,Object> map = new HashMap<String,Object>();
				try {
					map = new GetWxOrderno().doXMLParse(returnxml);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return map;
	}
	/**
	 * 退钱接口
	 * @param tpWxPayDto
	 * @return
	 */
	public static Map<String,Object>  returnPay(PayParamsObj payParams){
				PaymentConfig config = payParams.getPayConfig();
				if(config!=null){
					appid = config.getBargainorId();
					appsecret = config.getBargainorKey();
					partner = config.getSeller_email();
					partnerkey = config.getPartnerkey();
					perPath = config.getPerPath();
				}
		// 1 参数
				// 订单号
				String orderId = payParams.getOrderId();				//微信订单号
				String transaction_id = payParams.getTransactionId();
				//商户退款订单号：
				String out_refund_no = payParams.getUnOrderId();
				// 商户号
				String mch_id = partner;
				// 总金额以分为单位，不带小数点
				String total_fee = getMoney(payParams.getTotal_fee());
				//退款金额
				String refund_fee = total_fee;
				String op_user_id = partner;
				// 随机字符串
				String nonce_str = getNonceStr();
				// 商户订单号
				String out_trade_no = orderId;
				SortedMap<String, String> packageParams = new TreeMap<String, String>();
				packageParams.put("appid", appid);
				packageParams.put("mch_id", mch_id);
				packageParams.put("nonce_str", nonce_str);
				
				packageParams.put("transaction_id", transaction_id);
				packageParams.put("out_trade_no", out_trade_no);
				packageParams.put("out_refund_no", out_refund_no);
				packageParams.put("total_fee", total_fee);
				packageParams.put("refund_fee", refund_fee);
				
				packageParams.put("op_user_id", op_user_id);



				RequestHandler reqHandler = new RequestHandler(null, null);
				reqHandler.init(appid, appsecret, partnerkey);

				String sign = reqHandler.createSign(packageParams);
				String xml = "<xml>" + "<appid>" + appid + "</appid>"
						+ "<mch_id>"+ mch_id + "</mch_id>"
						+ "<nonce_str>" + nonce_str+ "</nonce_str>"
						+ "<sign>" + sign + "</sign>"
						+ "<transaction_id>" + transaction_id+ "</transaction_id>"
						+"<out_trade_no>"+out_trade_no+"</out_trade_no>"
						+ "<out_refund_no>" + out_refund_no + "</out_refund_no>"
						+ "<total_fee>" + total_fee+ "</total_fee>"
						+ "<refund_fee>" + refund_fee+ "</refund_fee>"
						+ "<op_user_id>" + op_user_id+ "</op_user_id>"
						+  "</xml>";
				String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
				String returnxml =post(getHttpClient(perPath, partner), createOrderURL, xml);
				Map<String,Object> map = new HashMap<String,Object>();
				try {
					map = new GetWxOrderno().doXMLParse(returnxml);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return map;
	}
	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}

	/**
	 * 元转换成分
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}

	public static String getAppid() {
		return appid;
	}

	public static void setAppid(String appid) {
		PayUtil.appid = appid;
	}

	public static String getAppsecret() {
		return appsecret;
	}

	public static void setAppsecret(String appsecret) {
		PayUtil.appsecret = appsecret;
	}

	public static String getPartner() {
		return partner;
	}

	public static void setPartner(String partner) {
		PayUtil.partner = partner;
	}

	public static String getPartnerkey() {
		return partnerkey;
	}

	public static void setPartnerkey(String partnerkey) {
		PayUtil.partnerkey = partnerkey;
	}

	public static String getOpenId() {
		return openId;
	}

	public static void setOpenId(String openId) {
		PayUtil.openId = openId;
	}

	public static String getNotifyurl() {
		return notifyurl;
	}

	public static void setNotifyurl(String notifyurl) {
		PayUtil.notifyurl = notifyurl;
	}
	private static HttpClient getHttpClient(String sertPath,String sertPw){
		HttpClient httpClient=null;
		try {

			KeyStore keyStore = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File(sertPath));//加载本地的证书进行https加密传输
	        try {
	            keyStore.load(instream, sertPw.toCharArray());//设置证书密码
	        } catch (CertificateException e) {
	            e.printStackTrace();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } finally {
	            instream.close();
	        }

	        SSLContext sslcontext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, sertPw.toCharArray())
	                .build();
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[]{"TLSv1"},
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

	        httpClient= HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpClient;
	}
	public static String post(HttpClient httpClient,String url,String xmlStr){
		String result = null;
        HttpPost httpPost = new HttpPost(url);
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(xmlStr, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        //设置请求器的配置
        httpPost.setConfig(RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000).build());
        try {
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");

        }catch (Exception e) {

        } finally {
            httpPost.abort();
        }
        return result;
	}
}

