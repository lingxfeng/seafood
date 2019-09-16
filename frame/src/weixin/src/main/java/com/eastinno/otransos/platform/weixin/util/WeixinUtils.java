package com.eastinno.otransos.platform.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.platform.weixin.bean.Article;
import com.eastinno.otransos.platform.weixin.bean.Image;
import com.eastinno.otransos.platform.weixin.bean.Music;
import com.eastinno.otransos.platform.weixin.bean.MyX509TrustManager;
import com.eastinno.otransos.platform.weixin.bean.ReqMessage;
import com.eastinno.otransos.platform.weixin.bean.RespImgMessage;
import com.eastinno.otransos.platform.weixin.bean.RespMessage;
import com.eastinno.otransos.platform.weixin.bean.RespMusicMessage;
import com.eastinno.otransos.platform.weixin.bean.RespNewsMessage;
import com.eastinno.otransos.platform.weixin.bean.RespTextMessage;
import com.eastinno.otransos.platform.weixin.bean.RespVideoMessage;
import com.eastinno.otransos.platform.weixin.bean.RespVoiceMessage;
import com.eastinno.otransos.platform.weixin.bean.Video;
import com.eastinno.otransos.platform.weixin.bean.Voice;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Menu;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.web.ActionContext;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 微信相关工具类
 * 
 * @author maowei
 * @createDate 2013-11-27下午3:36:22
 */
public class WeixinUtils {
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";
	/**
	 * 事件类型：VIEW(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_VIEW = "VIEW";
	// 图文消息详情地址
	public static final String NEWSITEMURL = "/weixinBase.otr?cmd=showNewsItem";
	// 文章详情地址
	public static final String NEWDOCURL = "/weixinBase.otr?cmd=showNewsDoc";

	public static void setMsgInfo(RespMessage oms, ReqMessage msg) throws Exception {
		// 设置发送信息
		Class<?> outMsg = oms.getClass().getSuperclass();
		Field CreateTime = outMsg.getDeclaredField("CreateTime");
		Field ToUserName = outMsg.getDeclaredField("ToUserName");
		Field FromUserName = outMsg.getDeclaredField("FromUserName");

		ToUserName.setAccessible(true);
		CreateTime.setAccessible(true);
		FromUserName.setAccessible(true);

		CreateTime.set(oms, new Date().getTime());
		ToUserName.set(oms, msg.getFromUserName());
		FromUserName.set(oms, msg.getToUserName());
	}

	/**
	 * 输入流转XML文件
	 * 
	 * @param ips
	 * @return
	 */
	public static final String ips2Str(InputStream ips) {
		if (ips == null)
			return "";

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = ips.read(b)) != -1;) {
				out.append(new String(b, 0, n, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(RespTextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	public static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * 1.将token、timestamp、nonce三个参数进行字典序排序<br/>
	 * 2.将三个参数字符串拼接成一个字符串进行sha11加密<br/>
	 * 3.开发者获得加密后的字符串可与signature对比，标识该请求来源于微信。
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param token
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce, String token) {
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	// 将字节数组转换为十六进制字符串
	private static String byteToStr(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexStr(bytearray[i]);
		}
		return strDigest;
	}

	// 将字节转换为十六进制字符串
	private static String byteToHexStr(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];

		String s = new String(ob);
		return s;
	}

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
		reader.setFeature(FEATURE, true);
		FEATURE = "http://xml.org/sax/features/external-general-entities";
		reader.setFeature(FEATURE, false);
		FEATURE = "http://xml.org/sax/features/external-parameter-entities";
		reader.setFeature(FEATURE, false);
		// Disable external DTDs as well
		FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
		reader.setFeature(FEATURE, false);
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 解析微信发来的请求（ReqMessage）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static ReqMessage parseXmlToReqM(HttpServletRequest request) throws Exception {
		// 将解析结果存储在ReqMessage中
		ReqMessage reqMessage = new ReqMessage();
		Class<ReqMessage> clazzRM = ReqMessage.class;
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			Field f = clazzRM.getDeclaredField(e.getName());
			f.setAccessible(true);
			f.set(reqMessage, e.getText());
		}
		// 释放资源
		inputStream.close();
		inputStream = null;
		return reqMessage;
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(RespNewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 图片消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图片消息对象
	 * @return xml
	 */
	public static String imgMessageToXml(RespImgMessage imgMessage) {
		xstream.alias("xml", imgMessage.getClass());
		xstream.alias("Image", new Image().getClass());
		return xstream.toXML(imgMessage);
	}

	/**
	 * 语音消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            语音消息对象
	 * @return xml
	 */
	public static String voiceMessageToXml(RespVoiceMessage voiceMessage) {
		xstream.alias("xml", voiceMessage.getClass());
		xstream.alias("Voice", new Voice().getClass());
		return xstream.toXML(voiceMessage);
	}

	/**
	 * 视频消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            视频消息对象
	 * @return xml
	 */
	public static String videoMessageToXml(RespVideoMessage videoMessage) {
		xstream.alias("xml", videoMessage.getClass());
		xstream.alias("Video", new Video().getClass());
		return xstream.toXML(videoMessage);
	}

	public static String musicMessageToXml(RespMusicMessage respMusic) {
		xstream.alias("xml", respMusic.getClass());
		xstream.alias("Music", new Music().getClass());
		return xstream.toXML(respMusic);
	}

	/**
	 * 获取url
	 * 
	 * @param url
	 * @param appId
	 * @param menuKey
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getViewButionUrl(String url, String appId, String menuKey) {
		url = URLEncoder.encode(url);
		return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + url
				+ "&response_type=code&scope=snsapi_userinfo&state=" + menuKey + "#wechat_redirect";
	}

	/**
	 * 根据经纬度计算距离
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double EARTH_RADIUS = 6378137;
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static String getFileExt(String contentType) {
		String fileExt = "";
		if ("image/jpeg".equals(contentType)) {
			fileExt = ".jpg";
		} else if ("audio/mpeg".equals(contentType)) {
			fileExt = ".mp3";
		} else if ("audio/amr".equals(contentType)) {
			fileExt = ".amr";
		} else if ("video/mp4".equals(contentType)) {
			fileExt = ".mp4";
		} else if ("video/mpeg4".equals(contentType)) {
			fileExt = ".mp4";
		}
		return fileExt;
	}

	/**
	 * 获取jsapi签名
	 * 
	 * @param timestamp
	 * @param jsapiTicket
	 * @param accountToken
	 * @return
	 */
	public static String jsnonceStr(Map<String, String> map) {
		Set<String> set = map.keySet();
		String[] arr = set.toArray(new String[] {});
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i] + "=" + map.get(arr[i]) + "&");
		}
		String contentstr = content.toString().substring(0, content.toString().length() - 1);
		System.out.println(contentstr);
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(contentstr.getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		return tmpStr;
	}

	/**
	 * 获取jsapi签名
	 * 
	 * @param account
	 * @return
	 */
	public static Map<String, String> jsnonceStr(Account account, String pageUrl) {
		String timestamp = new Date().getTime() + "";
		timestamp = timestamp.substring(timestamp.length() - 10, timestamp.length());
		String noncestr = randomString(10);
		String jsApiToken = RequestWxUtils.getJsapiTicket(account);
		Map<String, String> map = new HashMap<String, String>();
		map.put("timestamp", timestamp);
		map.put("noncestr", noncestr);
		map.put("jsapi_ticket", jsApiToken);
		map.put("url", pageUrl);
		String signature = jsnonceStr(map);
		map.put("appid", account.getAppid());
		map.put("signature", signature);
		return map;
	}

	/**
	 * 随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
					.toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 获取完整路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String url = "";
		url = request.getContextPath();
		url = url + request.getServletPath();
		java.util.Enumeration names = request.getParameterNames();
		if (!"".equals(request.getQueryString()) || request.getQueryString() != null) {
			url = url + "?" + request.getQueryString();
		}
		return url;
	}

	public static String getUrlAll() {
		HttpServletRequest req = ActionContext.getContext().getRequest();
		return getRequestURL(req);
	}

	/**
	 * 提取字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getStringZf(String str) {
		String regEx = "[a-zA-Z0-9\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		StringBuffer strb = new StringBuffer();
		while (m.find()) {
			strb.append(m.group());
		}
		return strb.toString();
	}

	/**
	 * 发送请求
	 * 
	 * @param reqUrl
	 * @param reqMethod
	 * @param ops
	 * @return
	 */
	public static JSONObject httpRequest(String reqUrl, String reqMethod, String ops) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(reqUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(reqMethod);
			if ("GET".equalsIgnoreCase(reqMethod)) {
				httpUrlConn.connect();
			}
			if (ops != null) {
				System.out.println(ops);
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(ops.getBytes("UTF-8"));
				outputStream.close();
			}
			InputStream ips = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(ips, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			ips.close();
			ips = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			System.out.println("Weixin server connection timed out.");
		} catch (Exception e) {
			System.out.println("https request error:{}");
		}
		return jsonObject;
	}

	/**
	 * 将菜单转化为map
	 * 
	 * @param menus
	 * @param map
	 * @param isParent
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> menuToMap(List<Menu> menus, Map<String, Object> map, Account account,
			boolean isParent) {
		String domain = ShiroUtils.getTenant().getUrl();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Menu m : menus) {
			Map<String, Object> mapmenu = new HashMap<String, Object>();
			mapmenu.put("name", m.getName());
			List<Menu> chilren = m.getChildren();
			if (chilren != null && chilren.size() > 0) {
				menuToMap(chilren, mapmenu, account, false);
			} else {
				mapmenu.put("type", m.getType());
				mapmenu.put("key", m.getMenuKey());
				if ("viewin".equals(m.getType())) {
					mapmenu.put("type", "view");
					String url = WeixinUtils.getViewButionUrl(
							domain + "/" + m.getUrl() + "&accountId=" + account.getId(), account.getAppid(),
							m.getMenuKey());
					mapmenu.put("url", url);
				} else if ("viewout".equals(m.getType())) {
					mapmenu.put("type", "view");
					mapmenu.put("url", m.getUrl());
				} else {
					mapmenu.put("key", m.getMenuKey());
				}
			}
			list.add(mapmenu);
		}
		if (isParent) {
			map.put("button", list);
		} else {
			map.put("sub_button", list);
		}
		return map;
	}
}
