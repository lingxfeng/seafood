package com.eastinno.otransos.payment.union.app.conf;

public class UnionConfig {

    // 版本号
    public static String VERSION = "1.0.0";

    // 编码方式
    public static String CHARSET = "UTF-8";

    // 交易网址
    public static String TRADE_URL = "https://mgate.unionpay.com/gateway/merchant/trade";

    // 查询网址
    public static String QUERY_URL = "https://mgate.unionpay.com/gateway/merchant/query";

    // 商户代码
    public static String MER_ID = "898111148990094";

    // 商城密匙，需要和银联商户网站上配置的一样
    public static String SECURITY_KEY = "ZwSIdznOZcl3t8Qtl1HyJWBMDzIsV21t";
    // 通知URL
    public static String MER_BACK_END_URL = "";

    // 前台通知URL
    public static String MER_FRONT_END_URL;

    // 返回URL
    public static String MER_FRONT_RETURN_URL = "";

    // 加密方式
    public static String SIGN_TYPE = "MD5";


    // 成功应答码
    public static final String RESPONSE_CODE_SUCCESS = "00";

    // 签名
    public final static String SIGNATURE = "signature";

    // 签名方法
    public final static String SIGN_METHOD = "signMethod";

    // 应答码
    public final static String RESPONSE_CODE = "respCode";

    // 应答信息
    public final static String RESPONSE_MSG = "respMsg";
    public final static String return_url = "unicon_app_return.otr";
    public final static String notiry_url = "unicon_app_notify.otr";
}
