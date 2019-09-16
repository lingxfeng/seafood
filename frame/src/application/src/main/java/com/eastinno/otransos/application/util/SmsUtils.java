package com.eastinno.otransos.application.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.shcm.bean.SendResultBean;
import com.shcm.send.OpenApi;

/**
 * @intro 短信工具类
 * @version v_0.1
 * @author majiang
 * @since 2014年5月19日 下午5:55:43
 */
public class SmsUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsUtils.class);

    // 短信网关参数配置
    private static final String account = "1001@500764290001"; // 接口帐号
    private static final String authkey = "0BF8C61BF208CDE76608E67A51FBB659"; // 接口密钥
    private static final int cgid = 658; // 通道组编号
    private static final int csid = 2217; // 默认使用的签名编号(未指定签名编号时传此值到服务器)
    private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";

    /**
     * 发送短信。
     * 
     * @param mobiles 目标手机号，多个手机号使用逗号分隔。
     * @param message 短信内容。
     * @return 网关回执。
     * 2017年2月8号做了变更
     * OpenApi.sendOnce(StringUtils.tokenizeToStringArray(mobiles, ","), message, 0, 0, null);的返回值变成了list
     */
    public static String sendMessage(String mobiles, String message) {
        OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
        List<SendResultBean> responseCode = OpenApi.sendOnce(StringUtils.tokenizeToStringArray(mobiles, ","), message, 0, 0, null);
        String result = "";
        if (responseCode.size() > 0) {
            LOGGER.debug("短信发送成功!");
            result = "Success";
        } else {
            LOGGER.warn("短信发送失败，返回状态码：{}", responseCode);
            result = "Failure";
        }
        return result;
    }

    public static void main(String[] args) {
        String msg = sendMessage("13396291152", "麦豆会员注册验证码：" + 123456);
        System.out.println(msg);
    }
}
