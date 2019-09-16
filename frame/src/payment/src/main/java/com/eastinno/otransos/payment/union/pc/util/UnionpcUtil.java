package com.eastinno.otransos.payment.union.pc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastinno.otransos.core.util.ClientIPUtil;
import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.util.PaymentUtil;

/**
 * 银联支付pc版
 */

public class UnionpcUtil {
    /**
     * 构造参数长度
     * 
     * @param str
     * @param length
     * @return
     */
    private static String setlengthStr(String str, int length) {
        if (str.length() >= length) {
            str = str.substring(str.length() - length);
        } else {
            for (int i = 0, c = length - str.length(); i < c; i++) {
                str = "0" + str;
            }
        }
        return str;
    }

    public static String toSubmit(PayParamsObj payParams) {
    	PaymentConfig config = payParams.getPayConfig();
		if(config!=null){
			QuickPayConf.merCode=config.getBargainorId();
			QuickPayConf.securityKey = config.getBargainorKey();
		}
        String msg = "";
        String[] valueVo = new String[] {QuickPayConf.version,// 协议版本
                QuickPayConf.charset,// 字符编码
                "01",// 交易类型
                "",// 原始交易流水号
                QuickPayConf.merCode,// 商户代码
                QuickPayConf.merName,// 商户简称
                "",// 收单机构代码（仅收单机构接入需要填写）
                "",// 商户类别（收单机构接入需要填写）
                "",// 商品URL
                payParams.getOrderName(),// 商品名称
                ((int) (Double.parseDouble(payParams.getTotal_fee()) * 100)) + "",// 商品单价 单位：分
                "1",// 商品数量
                "0",// 折扣 单位：分
                "0",// 运费 单位：分
                setlengthStr(payParams.getOrderId(), 16),// 订单号（需要商户自己生成）
                ((int) (Double.parseDouble(payParams.getTotal_fee()) * 100)) + "",// 交易金额 单位：分
                "156",// 交易币种
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),// 交易时间
                ClientIPUtil.getIP(null),// 用户IP
                payParams.getOrderName(),// 用户真实姓名
                "",// 默认支付方式
                "",// 默认银行编号
                "30000000",// 交易超时时间
                PaymentUtil.getDomain() + QuickPayConf.merFrontEndUrl,// 前台回调商户URL
                PaymentUtil.getDomain() + QuickPayConf.merBackEndUrl,// 后台回调商户URL
                ""// 商户保留域
        };
        msg = new QuickPayUtils().createPayHtml(valueVo, QuickPayConf.signType);// 跳转到银联页面支付
        System.out.println("支付已经开始，请求参数：" + msg);

        return msg;
    }

}