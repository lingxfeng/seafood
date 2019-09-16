package com.eastinno.otransos.payment.union.pc.util;

public class QuickPayConf {
    public final static String version = "1.0.0";
    // 编码方式
    public final static String charset = "UTF-8";
    private final static String UPOP_BASE_URL = "https://unionpaysecure.com/api/";
    private final static String UPOP_BSPAY_BASE_URL = "https://besvr.unionpaysecure.com/api/";
    private final static String UPOP_QUERY_BASE_URL = "http://58.246.226.99/UpopWeb/api/";
    // 支付网址
    public final static String gateWay = UPOP_BASE_URL + "Pay.action";
    // 后续交易网址
    public final static String backStagegateWay = UPOP_BSPAY_BASE_URL + "BSPay.action";
    // 查询网址
    public final static String queryUrl = UPOP_QUERY_BASE_URL + "Query.action";
    // 认证支付2.0网址
    public final static String authPayUrl = UPOP_BASE_URL + "AuthPay.action";
    // 发短信网址
    public final static String smsUrl = UPOP_BASE_URL + "Sms.action";
    // 商户代码
    public static String merCode = "898111148990003";
    // 商户名称
    public final static String merName = "梦坊在线";
    public final static String merFrontEndUrl = "union_pc_return.otr";
    public final static String merBackEndUrl = "union_pc_notify.otr";
    // 加密方式
    public final static String signType = "MD5";
    public final static String signType_SHA1withRSA = "SHA1withRSA";
    public static String securityKey = "BNFTYU50T6RL4LPUPIWFPOL5PLU";
    // 签名
    public final static String signature = "signature";
    public final static String signMethod = "signMethod";
    // 组装消费请求包
    public final static String[] reqVo = new String[] {"version", "charset", "transType", "origQid", "merId", "merAbbr", "acqCode",
            "merCode", "commodityUrl", "commodityName", "commodityUnitPrice", "commodityQuantity", "commodityDiscount", "transferFee",
            "orderNumber", "orderAmount", "orderCurrency", "orderTime", "customerIp", "customerName", "defaultPayType",
            "defaultBankNumber", "transTimeout", "frontEndUrl", "backEndUrl", "merReserved"};

    public final static String[] notifyVo = new String[] {"charset", "cupReserved", "exchangeDate", "exchangeRate", "merAbbr", "merId",
            "orderAmount", "orderCurrency", "orderNumber", "qid", "respCode", "respMsg", "respTime", "settleAmount", "settleCurrency",
            "settleDate", "traceNumber", "traceTime", "transType", "version"};

    public final static String[] queryVo = new String[] {"version", "charset", "transType", "merId", "orderNumber", "orderTime",
            "merReserved"};

    public final static String[] smsVo = new String[] {"version", "charset", "acqCode", "merId", "merAbbr", "orderNumber", "orderAmount",
            "orderCurrency", "merReserved"};
}
