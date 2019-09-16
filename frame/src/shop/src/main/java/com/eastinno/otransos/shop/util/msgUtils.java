package com.eastinno.otransos.shop.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class msgUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(msgUtils.class);

    // 短信网关参数配置
	private static final String url = "http://222.73.117.158/msg/HttpBatchSendSM";
    private static final String account = "rcts888"; // 接口帐号
    private static final String pswd = "Rcts123456"; // 接口密钥
    private static final boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
	private static final String product = null;// 产品ID
	private static final String extno = null;// 扩展码

    /**
     * 发送短信。
     * 
     * @param mobiles 目标手机号，多个手机号使用逗号分隔。
     * @param message 短信内容。
     * @return 网关回执。
     */
    public static String sendMessage(String mobiles, String msg) {
    	String result="Success";
    	String returnString;
		try {
			returnString = HttpSender.batchSend(url, account, pswd, mobiles, msg, needstatus, product, extno);
			String rs=returnString.replaceAll("\n", ",");
			String [] s=rs.split(",");
			if(!"0".equals(s[1])){
				result="Failure";  LOGGER.warn("短信发送失败，返回状态码：{}", returnString);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

    public static void main(String[] args) {
        String msg = sendMessage("13391634215", "【百春达】测试验证码：123456");
        System.out.println(msg);
    }
}
