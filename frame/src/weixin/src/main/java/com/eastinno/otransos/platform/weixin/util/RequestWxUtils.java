package com.eastinno.otransos.platform.weixin.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.http.util.HttpHelper;
import com.eastinno.otransos.platform.weixin.bean.AccessToken;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.service.IWxplatService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;

public class RequestWxUtils {
	public static String errorCodeOld="40001";
	/**
	 * 获取用户列表
	 * @param next_openId
	 * @return
	 */
	public static Map<String,Object> getWxUser(Account account,String next_openId){
		Map<String,Object> mapjson=null;
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
		AccessToken sccessToken = getAccessToken(account);
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
		if(next_openId != null){
			requestUrl = requestUrl+"&next_openid="+next_openId;
		}
		try {
			String contentstr = HttpHelper.get(requestUrl).getContent();
			mapjson = JSONObject.parseObject(contentstr);
			if(mapjson.get("errcode")!=null && errorCodeOld.equals(mapjson.get("errcode").toString())){
				sccessToken = getAccessToken(account);
				requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
				contentstr = HttpHelper.get(requestUrl).getContent();
				mapjson = JSONObject.parseObject(contentstr);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return mapjson;
	}
	/**
	 * 获取jsapi_ticket
	 * 
	 * @param account
	 * @return
	 */
	public static String getJsapiTicket(Account account) {
		String jsApiToken = null;
		Date nowTime = new Date();
		if (account.getJsapi_ticket() == null
				|| "".equals(account.getJsapi_ticket())
				|| (nowTime.getTime() - account.getJsapi_ticket_time()) >= 7000 * 1000) {
			AccessToken sccessToken = getAccessToken(account);
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
			String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
			String contentstr;
			try {
				contentstr = HttpHelper.get(requestUrl).getContent();
				Map<String, Object> mapjson = JSONObject
						.parseObject(contentstr);
				if(errorCodeOld.equals(mapjson.get("errcode").toString())){
					sccessToken = getAccessTokenNew(account);
					requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
					contentstr = HttpHelper.get(requestUrl).getContent();
					mapjson = JSONObject.parseObject(contentstr);
				}
				if ("0".equals(mapjson.get("errcode") + "")) {
					jsApiToken = mapjson.get("ticket") + "";
					account.setJsapi_ticket(jsApiToken);
					account.setJsapi_ticket_time(new Date().getTime());
					ServletContext sc = ActionContext.getContext()
							.getServletContext();
					ApplicationContext ac2 = WebApplicationContextUtils
							.getWebApplicationContext(sc);
					IWxplatService wxplatService = ac2
							.getBean(IWxplatService.class);
					wxplatService.updateAccount(account.getId(), account);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			jsApiToken = account.getJsapi_ticket();
		}
		return jsApiToken;
	}
	/**
	 * 多媒体下载
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @param savePath
	 * @return
	 */
	public static String downloadMedia(String accessToken, String mediaId,
			String savePath) {
		String filePath = null;
		// 拼接请求地址
		String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
				"MEDIA_ID", mediaId);
		System.out.println(requestUrl);
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");

			if (!savePath.endsWith("/")) {
				savePath += "/";
			}
			// 根据内容类型获取扩展名
			String fileExt = WeixinUtils.getFileExt(conn.getHeaderField("Content-Type"));
			// 将mediaId作为文件名
			filePath = savePath + new Date().getTime() + fileExt;

			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(
					Globals.APP_BASE_DIR + filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);
			fos.close();
			bis.close();

			conn.disconnect();
			String info = String.format("下载媒体文件成功，filePath=" + filePath);
			System.out.println(info);
		} catch (Exception e) {
			filePath = null;
			String error = String.format("下载媒体文件失败：%s", e);
			System.out.println(error);
		}
		return filePath;
	}

	public static String downloadMedia(Account account, String mediaId,
			String savePath) {
		AccessToken sccessToken = getAccessToken(account);
		return downloadMedia(sccessToken.getToken(), mediaId, savePath);
	}
	/**
	 * 多媒体上传
	 * 
	 * @param accessToken
	 * @param type
	 * @param mediaFileUrl
	 * @return
	 */
	public static Map<String, Object> uploadMedia(String accessToken,
			String type, String mediaFileUrl) {
		Map<String, Object> mapjson = null;
		// 拼装请求地址
		String uploadMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken)
				.replace("TYPE", type);

		// 定义数据分隔符
		String boundary = "------------7da2e536604c8";
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl
					.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();

			URL mediaUrl = new URL(mediaFileUrl);
			HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl
					.openConnection();
			meidaConn.setDoOutput(true);
			meidaConn.setRequestMethod("GET");

			// 从请求头中获取内容类型
			String contentType = meidaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = mediaFileUrl.substring(mediaFileUrl
					.lastIndexOf("."));
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream
					.write(String
							.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n",
									fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n",
					contentType).getBytes());

			// 获取媒体文件的输入流（读取文件）
			BufferedInputStream bis = new BufferedInputStream(
					meidaConn.getInputStream());
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			meidaConn.disconnect();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();

			// 使用JSON-lib解析返回结果
			mapjson = JSONObject.parseObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapjson;
	}

	public static Map<String, Object> uploadMedia(Account account,
			String mediaUploadType, String mediaFileUrl,ServletContext sc) {
		AccessToken sccessToken = getAccessToken(account,sc);
		Map<String, Object> mapjson = uploadMedia(sccessToken.getToken(), mediaUploadType,mediaFileUrl);
		if(mapjson.get("errcode")!=null && errorCodeOld.equals(mapjson.get("errcode").toString())){
			sccessToken = getAccessTokenNew(account,sc);
			mapjson = uploadMedia(sccessToken.getToken(), mediaUploadType,mediaFileUrl);
		}
		return mapjson;
	}
	public static Map<String, Object> uploadMedia(Account account,
			String mediaUploadType, String mediaFileUrl) {
		return uploadMedia(account,mediaUploadType, mediaFileUrl,null);
	}
	/**
	 * 获取用户基本信息
	 * 
	 * @param follower
	 * @param account
	 */
	public static void getUserInfo(Follower follower, Account account,ServletContext sc) {
		AccessToken sccessToken = getAccessToken(account,sc);
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("OPENID", follower.getWeixinOpenId());
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
		try {
			String contreturn = HttpHelper.get(requestUrl).getContent();
			Map<String, Object> mapjson = JSONObject.parseObject(contreturn);
			if(mapjson.get("errcode")!=null && errorCodeOld.equals(mapjson.get("errcode").toString())){
				sccessToken = getAccessTokenNew(account,sc);
				requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
				contreturn = HttpHelper.get(requestUrl).getContent();
				mapjson = JSONObject.parseObject(contreturn);
			}
			follower.setNickname(WeixinUtils.getStringZf(mapjson.get("nickname") + ""));
			follower.setSex(mapjson.get("sex") + "");
			follower.setCitystr(mapjson.get("city") + "");
			follower.setProvincestr(mapjson.get("province") + "");
			follower.setCountrystr(mapjson.get("country") + "");
			follower.setHeadimgurl(mapjson.get("headimgurl") + "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public static void getUserInfo(Follower follower, Account account){
		getUserInfo(follower, account, null);
	}
	/**
	 * 根据code获取粉丝openid
	 * @param code
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static String getOpenIdByCode(Account account,String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ account.getAppid() + "&secret=" + account.getAppsecret() + "&code=" + code
				+ "&grant_type=authorization_code";
		String openid = "";
		try {
			String resstr = HttpHelper.get(url, "UTF-8").getContent();
			Map<String, Object> mapjson = JSONObject.parseObject(resstr);
			openid = mapjson.get("openid") + "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return openid;
	}
	/**
	 * 根据code获取粉丝openid
	 * @param code
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static Map<String,Object> getUserInfoByCode(Account account,String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ account.getAppid() + "&secret=" + account.getAppsecret() + "&code=" + code
				+ "&grant_type=authorization_code";
		Map<String,Object> mapjson=null;
		try {
			String resstr = HttpHelper.get(url, "UTF-8").getContent();
			mapjson = JSONObject.parseObject(resstr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return mapjson;
	}
	public static AccessToken getAccessToken(String appid, String appsecret) {
        AccessToken accessToken = null;
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = WeixinUtils.httpRequest(requestUrl, "GET", null);
        if (jsonObject != null) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
            } catch (JSONException e) {
                accessToken = null;
                System.out.println("获取token失败 errcode:{"+jsonObject.getIntValue("errcode")+"} errmsg:{"+jsonObject.getString("errmsg")+"}");
            }
        }
        return accessToken;
    }

    public static AccessToken getAccessToken(Account account) {
        return getAccessToken(account, null);
    }
    public static AccessToken getAccessToken(Account account,ServletContext sc) {
        AccessToken accessToken = new AccessToken();
        Date nowTime = new Date();
        if (account.getAccess_token() == null || "".equals(account.getAccess_token())
                || (nowTime.getTime() - account.getAccess_token_time()) >= account.getExpires_in() * 1000) {
            System.out.println("============重新获取accessToken===============");
            accessToken = getAccessTokenNew(account,sc);
        } else {
            accessToken.setExpiresIn(account.getExpires_in());
            accessToken.setToken(account.getAccess_token());
        }
        return accessToken;
    }
    public static AccessToken getAccessToken2(Account account,IWxplatService wxplatService) {
    	 AccessToken accessToken = new AccessToken();
         Date nowTime = new Date();
         if (account.getAccess_token() == null || "".equals(account.getAccess_token())
                 || (nowTime.getTime() - account.getAccess_token_time()) >= account.getExpires_in() * 1000) {
             System.out.println("============重新获取accessToken===============");
             accessToken = getAccessTokenNew2(account,wxplatService);
         } else {
             accessToken.setExpiresIn(account.getExpires_in());
             accessToken.setToken(account.getAccess_token());
         }
         return accessToken;
    }
    /**
     * 重新获取accessToken
     * @param account
     * @return
     */
    public static AccessToken getAccessTokenNew(Account account){
    	return getAccessTokenNew(account, null);
    }
    public static AccessToken getAccessTokenNew(Account account,ServletContext sc){
    	AccessToken accessToken = new AccessToken();
    	accessToken = getAccessToken(account.getAppid(), account.getAppsecret());
    	if(sc==null){
    		sc = ActionContext.getContext().getServletContext();
    	}
        ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(sc);
        IWxplatService wxplatService = ac2.getBean(IWxplatService.class);
        account.setAccess_token(accessToken.getToken());
        account.setExpires_in(accessToken.getExpiresIn());
        account.setAccess_token_time(new Date().getTime());
        wxplatService.updateAccount(account.getId(), account);
    	return accessToken;
    }
    public static AccessToken getAccessTokenNew2(Account account,IWxplatService wxplatService){
    	AccessToken accessToken = new AccessToken();
    	accessToken = getAccessToken(account.getAppid(), account.getAppsecret());
        account.setAccess_token(accessToken.getToken());
        account.setExpires_in(accessToken.getExpiresIn());
        account.setAccess_token_time(new Date().getTime());
        wxplatService.updateAccount(account.getId(), account);
    	return accessToken;
    }
    /**
     * 创建菜单
     * @param jsonMenu
     * @param accessToken
     * @return
     */
    public static JSONObject createMenu(Account account,String jsonStr) {
    	AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        if(errorCodeOld.equals(jSONObject.getShort("errcode"))){
        	sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        }
        return jSONObject;
    }
    /**
     * 消息群发
     * @param account
     * @return
     */
	public static JSONObject sendMsgToAll(Account account,String jsonStr) {
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
        String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        if(errorCodeOld.equals(jSONObject.getShort("errcode"))){
        	sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        }
        return jSONObject;
	}
	/**
     * 消息群发
     * @param account
     * @return
     */
	public static JSONObject sendMsgToAll(Account account,String jsonStr,IWxplatService wxplatService) {
		AccessToken sccessToken = RequestWxUtils.getAccessToken2(account,wxplatService);
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
        String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        if(errorCodeOld.equals(jSONObject.getShort("errcode"))){
        	sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        }
        return jSONObject;
	}
	/**
	 * 发送客服消息
	 * @param account
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject sendMsgToKF(Account account, String jsonStr) {
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        if(errorCodeOld.equals(jSONObject.getShort("errcode"))){
        	sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        }
        return jSONObject;
	}
	/**
	 * 生成临时二维码
	 * @param account
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject createInterimQrcode(Account account, String jsonStr) {
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
        String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        if(errorCodeOld.equals(jSONObject.getShort("errcode"))){
        	sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        }
        return jSONObject;
	}
	/**
	 * 生成永久二维码
	 * @param account
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject createForeverQrcode(Account account, String jsonStr) {
		AccessToken sccessToken = RequestWxUtils.getAccessToken(account);
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
        String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
        JSONObject jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        if(errorCodeOld.equals(jSONObject.getShort("errcode"))){
        	sccessToken = RequestWxUtils.getAccessTokenNew(account);
        	requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
            jSONObject = WeixinUtils.httpRequest(requestUrl, "POST", jsonStr);
        }
        return jSONObject;
	}
	/**
	 * 网页授权获取用户信息
	 * @param follower
	 * @param account
	 * @param access_token
	 * @param sc
	 */
	public static void getUserInfo(Follower follower, Account account,String access_token,ServletContext sc) {
		AccessToken sccessToken = getAccessToken(account,sc);
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("OPENID", follower.getWeixinOpenId());
		url = url.replace("ACCESS_TOKEN", access_token);
		String requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
		try {
			String contreturn = HttpHelper.get(requestUrl).getContent();
			Map<String, Object> mapjson = JSONObject.parseObject(contreturn);
			if(mapjson.get("errcode")!=null && errorCodeOld.equals(mapjson.get("errcode").toString())){
				sccessToken = getAccessTokenNew(account,sc);
				requestUrl = url.replace("ACCESS_TOKEN", sccessToken.getToken());
				contreturn = HttpHelper.get(requestUrl).getContent();
				mapjson = JSONObject.parseObject(contreturn);
			}
			if(mapjson.get("errcode")!=null){
				return;
			}
			follower.setNickname(WeixinUtils.getStringZf(mapjson.get("nickname") + ""));
			follower.setSex(mapjson.get("sex") + "");
			follower.setCitystr(mapjson.get("city") + "");
			follower.setProvincestr(mapjson.get("province") + "");
			follower.setCountrystr(mapjson.get("country") + "");
			follower.setHeadimgurl(mapjson.get("headimgurl") + "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
