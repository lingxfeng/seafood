package com.eastinno.otransos.payment.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.union.pc.util.UnionpcUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;

/**
 * 支付系统核心工具类
 * 
 * @author nsz
 */
public class PaymentUtil {
    /**
     * 根据payType 判断调用哪种支付方式，获取返回结果
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public static String paystr(PayParamsObj payParams) throws Exception {
        String paystr = "支付失败";
        String orderId = payParams.getOrderId();
        String orderName = payParams.getOrderName();
        PaymentConfig config = payParams.getPayConfig();
        PayTypeE payType = config==null?PayTypeE.ALIPAYPCWEB:config.getType();
        String total_fee = payParams.getTotal_fee();
        if ("".equals(orderId) || "".equals(orderName) || "".equals(payType) || "".equals(total_fee)) {
            paystr = "{status:'error',msg:'请求参数错误'}";
        } else {
            if (payType==PayTypeE.ALIPAYMWEB) {
                /**
                 * 阿里手机网页版支付
                 */
                paystr = com.eastinno.otransos.payment.alipay.mobile.web.util.AlipaySubmit.getHtmlStr(payParams);
            } else if (payType==PayTypeE.ALIPAYNET) {
                /**
                 * 阿里网银支付
                 */
                paystr = com.eastinno.otransos.payment.alipay.web.net.util.AlipaySubmit.getHtmlStr(payParams);
            } else if (payType==PayTypeE.YLAPP) {
                /**
                 * 银联app支付
                 */
                paystr = com.eastinno.otransos.payment.union.app.util.SubmitUtil.toSubmit(payParams);
            } else if (payType==PayTypeE.AliPAYSDK) {
                /**
                 * 支付宝快捷支付
                 */
                paystr = com.eastinno.otransos.payment.alipay.mobile.app.util.AlipayCore.getHtmlStr(payParams);
            } else if (payType==PayTypeE.TENPAY) {
                /**
                 * 微信开放平台支付
                 */
                paystr = com.eastinno.otransos.payment.tencent.weixin.util.SubmitUtil.toSubmit(payParams);
            } else if (payType==PayTypeE.YLPC) {
                /**
                 * 银联pc支付
                 */
                paystr = UnionpcUtil.toSubmit(payParams);
            } else if (payType==PayTypeE.ALIPAYPCWEB) {
                /**
                 * 阿里pc网页版支付
                 */
                paystr = com.eastinno.otransos.payment.alipay.pc.web.util.AlipaySubmit.getHtmlStr(payParams);
            }else if(payType == PayTypeE.WEIXINMPAPI){
            	/**
            	 * 微信公众号api支付
            	 */
            	paystr = com.eastinno.otransos.payment.tencent.mpweixin.pay.PayUtil.getPackage(payParams);
            }else if(payType == PayTypeE.WEIXINMPSM){
            	/**
            	 * 微信公众号扫码支付
            	 */
            	paystr = com.eastinno.otransos.payment.tencent.mpweixin.pay.PayUtil.getCodeurl(payParams);
            }else if(payType == PayTypeE.WEIXINTURNLQ){
            	/**
            	 * 转钱到客户微信零钱中
            	 */
            	//paystr = com.eastinno.otransos.payment.tencent.mpweixin.pay.PayUtil.tranWeixin(payParams);
            }
        }
        return paystr;
    }

    /**
     * 调用支付系统完成支付，返回Page
     * 
     * @return
     * @throws Exception
     */
    public static Page paysubmit(PayParamsObj payParams) {
        String msg = "error";
        try {
            msg = paystr(payParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sentmsg(msg);
        return Page.nullPage;
    }

    /**
     * 调用支付系统完成支付支付，返回字符串
     * 
     * @param params
     * @return
     */
    public static String paysubmitStr(PayParamsObj payParams) {
        String msg = "error";
        try {
            msg = paystr(payParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 将信息打印到前台
     * 
     * @param msg
     * @return
     */
    public static Page sentmsg(String msg) {
        HttpServletResponse res = ActionContext.getContext().getResponse();
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = res.getWriter();
            out.print(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Page.nullPage;
    }

    /**
     * 获取域名
     * 
     * @return
     */
    public static String getDomain() {
        HttpServletRequest req = getReq();
        StringBuffer url = req.getRequestURL();
        String tempContextUrl = url.delete(url.length() - req.getRequestURI().length(), url.length()).append("/").toString();
        if (!tempContextUrl.endsWith("/")) {
            tempContextUrl = tempContextUrl + "/";
        }
//        System.out.println("========================域名为：" + tempContextUrl);
        return tempContextUrl;
    }

    public static String getIP() {
        HttpServletRequest req = ActionContext.getContext().getRequest();
        String ip = req.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        String returnIp =  ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
        int index=returnIp.indexOf(",");
        if(index!=-1){
        	returnIp = returnIp.substring(0, index);
        }
        return returnIp;
    }

    /**
     * 获取request
     * 
     * @return
     */
    public static HttpServletRequest getReq() {
        return ActionContext.getContext().getRequest();
    }

    /**
     * 获取response
     * 
     * @return
     */
    public static HttpServletResponse getRes() {
        return ActionContext.getContext().getResponse();
    }
}
