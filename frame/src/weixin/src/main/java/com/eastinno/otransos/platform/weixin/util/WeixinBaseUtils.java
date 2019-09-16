package com.eastinno.otransos.platform.weixin.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;

public class WeixinBaseUtils {
	public static int firstfollowSto=20;//首次关注送积分
	public static int signatureSto=5;//签到获积分
	public static int shareSto=5;//分享获积分
	public static int cjjSto=10;
	/**
	 * 获取公众号
	 * 
	 * @param accountId
	 * @return
	 */
	public static Account getAccount(String accountId) {
		ServletContext sc = ActionContext.getContext().getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		IAccountService accountService = ac2.getBean(IAccountService.class);
		Account account = accountService.getAccount(Long.parseLong(accountId));
		return account;
	}

	/**
	 * 获取粉丝
	 * 
	 * @param code
	 * @param accountId
	 * @return
	 */
	public static Follower getFollower(String code, String accountId) {
		ServletContext sc = ActionContext.getContext().getServletContext();
		ApplicationContext ac2 = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		IFollowerService followerService = ac2.getBean(IFollowerService.class);
		Follower follower = null;
		Account account = getAccount(accountId);
		String weixinOpenId = RequestWxUtils.getOpenIdByCode(account,code);
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.weixinOpenId", weixinOpenId, "=");
		List<?> list = followerService.getFollowerBy(qo).getResult();
		if (list != null && list.size() > 0) {
			follower = (Follower) list.get(0);
		}
		return follower;
	}
	public static String getWxUrl(String url,Account account){
    	url = URLEncoder.encode(url);
    	String weixinurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+account.getAppid()+"&redirect_uri=" + url
    			+ "&response_type=code&scope=snsapi_userinfo&state=1"
    			+ "#wechat_redirect";
    	return weixinurl;
    }
	/**
	 * 设置微信调用
	 * @param form
	 * @param account
	 */
	public static void setWeixinjs(WebForm form,Account account){
		if(account!=null){
			String pageUrl = getDomain()+WeixinUtils.getUrlAll();
	        Map<String,String> mapjs = WeixinUtils.jsnonceStr(account,pageUrl);
	        form.addResult("jsmap", mapjs);
	        form.addResult("defaultImg", account.getImgPath());
	        form.addResult("title", account.getName());
		}
	}
	/**
	 * 设置微信调用
	 * @param form
	 * @param account
	 */
	public static void setWeixinjs(WebForm form,Account account,String url){
		String pageUrl = getDomain()+url;
        Map<String,String> mapjs = WeixinUtils.jsnonceStr(account,pageUrl);
        form.addResult("jsmap", mapjs);
        form.addResult("defaultImg", account.getImgPath());
        form.addResult("title", account.getName());
	}
	/**
	 * 转发url
	 * @param url
	 * @param account
	 * @return
	 */
	public static Page reSendWx(String url,Account account){
		url = URLEncoder.encode(url);
    	String weixinurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+account.getAppid()+"&redirect_uri=" + url
    			+ "&response_type=code&scope=snsapi_userinfo&state=1"
    			+ "#wechat_redirect";
    	HttpServletResponse res = ActionContext.getContext().getResponse();
    	try {
			res.sendRedirect(weixinurl);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return Page.nullPage;
	}
	/**
	 * 转发url
	 * @param url
	 * @param account
	 * @return
	 */
	public static Page reSendWxGetUser(String url,Account account){
		url = URLEncoder.encode(url);
    	String weixinurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+account.getAppid()+"&redirect_uri=" + url
    			+ "&response_type=code&scope=snsapi_userinfo&state=1"
    			+ "#wechat_redirect";
    	HttpServletResponse res = ActionContext.getContext().getResponse();
    	try {
			res.sendRedirect(weixinurl);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return Page.nullPage;
	}
	/**
	 * 获得域名
	 * @return
	 */
	public static String getDomain() {
        HttpServletRequest req = ActionContext.getContext().getRequest();
        StringBuffer url = req.getRequestURL();
        String tempContextUrl = url.delete(url.length() - req.getRequestURI().length(), url.length()).append("/").toString();
        if (tempContextUrl.endsWith("/")) {
            tempContextUrl = tempContextUrl.substring(0, tempContextUrl.length()-1);
        }
//        System.out.println("========================域名为：" + tempContextUrl);
        return tempContextUrl;
    }
	/**
	 * 给粉丝发送消息
	 * @param follower
	 * @param msg
	 * @return
	 */
	public static boolean sendMsgToFollower(Account account,Follower follower,String msg){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("touser", follower.getWeixinOpenId());
		jsonMap.put("msgtype", "text");
		Map<String, Object> mapdata = new HashMap<String, Object>();
		mapdata.put("content", msg);
		jsonMap.put("text", mapdata);
		JSONObject jo = new JSONObject(jsonMap);
        String jsonStr = jo.toJSONString();
        JSONObject jSONObject = RequestWxUtils.sendMsgToKF(account, jsonStr);
        if (jSONObject != null && "0".equals(jSONObject.get("errcode") + "")) {
        	return true;
        }
		return false;
	}
}
