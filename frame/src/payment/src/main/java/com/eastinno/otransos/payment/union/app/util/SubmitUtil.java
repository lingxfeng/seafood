package com.eastinno.otransos.payment.union.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.payment.union.app.conf.UnionConfig;
import com.eastinno.otransos.payment.union.app.service.UnionService;


public class SubmitUtil {
    public static String toSubmit(PayParamsObj payParams) {
    	PaymentConfig config = payParams.getPayConfig();
		if(config!=null){
			UnionConfig.MER_ID=config.getBargainorId();
			UnionConfig.SECURITY_KEY = config.getBargainorKey();
		}
        Map<String, String> req = new HashMap<String, String>();
        req.put("version", UnionConfig.VERSION);// 版本号
        req.put("charset", UnionConfig.CHARSET);// 字符编码
        req.put("transType", "01");// 交易类型
        req.put("merId", UnionConfig.MER_ID);// 商户代码
        String domain = PaymentUtil.getDomain();
        req.put("backEndUrl", domain + UnionConfig.notiry_url);// 通知URL
        req.put("frontEndUrl", domain + UnionConfig.return_url);// 前台通知URL(可选)
        if (payParams.getOrderDesc() != null) {
            req.put("orderDescription", payParams.getOrderDesc());// 订单描述(可选)
        }
        req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 交易开始日期时间yyyyMMddHHmmss
        // req.put("orderTimeout", "");// 订单超时时间yyyyMMddHHmmss(可选)
        // req.put("orderNumber", params.get("orderId"));//订单号(商户根据自己需要生成订单号)
        req.put("orderNumber", setlengthStr(payParams.getOrderId(), 16));// 订单号(商户根据自己需要生成订单号)
        req.put("orderAmount", ((int) (Double.parseDouble(payParams.getTotal_fee()) * 100)) + "");// 订单金额
        req.put("orderCurrency", "156");// 交易币种(可选)
        // req.put("reqReserved", "透传信息");// 请求方保留域(可选，用于透传商户信息)

        // 保留域填充方法
        // Map<String, String> merReservedMap = new HashMap<String, String>();
        // merReservedMap.put("test", "test");
        // req.put("merReserved", UpmpService.buildReserved(merReservedMap));//
        // 商户保留域(可选)

        Map<String, String> resp = new HashMap<String, String>();
        System.out.println("支付已经开始，请求参数：" + req);
        boolean validResp = UnionService.trade(req, resp);

        // 商户的业务逻辑
        if (validResp) {
            return JSONObject.toJSON(resp).toString();
        }
        else {
            // 服务器应答签名验证失败
            return JSONObject.toJSON(resp).toString();
        }
    }


    private static String setlengthStr(String str, int length) {
        if (str.length() >= length) {
            str = str.substring(str.length() - length);
        }
        else {
            for (int i = 0, c = length - str.length(); i < c; i++) {
                str = "0" + str;
            }
        }
        return str;
    }
}
