package com.eastinno.otransos.seafood.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;

public class DiscoShopUtil {
	public static void setValueToField(Field f,Object obj,String value) throws Exception{
		Class<?> clazz = f.getType();
		if(clazz==Integer.class){
			f.set(obj, Integer.parseInt(value));
		}else if(clazz==Long.class){
			f.set(obj, Long.parseLong(value));
		}else if(clazz==Short.class){
			f.set(obj, Short.parseShort(value));
		}else if(clazz==Boolean.class){
			f.set(obj, value.equals("1"));
		}else{
			f.set(obj, value);
		}
	}
	public static String getMerchantCode(){
		return "core";
	}
	/**
	 * 删除文件
	 * @param fileNames
	 */
	public static void deleteFile(String... fileNames){
		for(String fileName:fileNames){
			File file = new File(Globals.APP_BASE_DIR + fileName);
            if (file.exists()) {
                file.delete();
            }
		}
	}
	public static Page goPage(String url){
		try {
			ActionContext.getContext().getResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Page.nullPage;
	}
	/**
	 * 获取cookie
	 * @param key
	 * @return
	 */
	public static String getCookie(String key){
		String value="";
		HttpServletRequest req = ActionContext.getContext().getRequest();
		Cookie[] css = req.getCookies();
		if(css!=null){
			for(Cookie cs:css){
				if(cs.getName().equals(key)){
					value = cs.getValue();
					break;
				}
			}
		}
		return value;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Cookie getCookieC(String key){
		HttpServletRequest req = ActionContext.getContext().getRequest();
		Cookie[] css = req.getCookies();
		if(css!=null){
			for(Cookie cs:css){
				if(cs.getName().equals(key)){
					return cs;
				}
			}
		}
		return null;
	}
	/**
	 * add proId tocookie
	 * @param id
	 */
	public static String setCookie(String cookieName,String id){
		String cookieStr="";
		try {
			id = URLEncoder.encode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie c = getCookieC(cookieName);
		HttpServletResponse res = ActionContext.getContext().getResponse();
		if(c==null){
			c= new Cookie(cookieName, id);
			cookieStr=id;
		}else{
			String idstr = c.getValue();
			try {
				idstr = URLDecoder.decode(idstr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String[] ids = idstr.split(",");
			boolean exit=false;
			for(String ide:ids){
				if(ide.equals(id)){
					exit=true;
					break;
				}
			}
			if(!exit){
				idstr +=","+id;
			}
			c.setValue(idstr);
			cookieStr=idstr;
		}
		c.setPath("/");
		c.setMaxAge(7*24*60*60);
		res.addCookie(c);
		return cookieStr;
	}
	public static boolean setCookieByVal(String cookieName,String value){
		if(value!=null){
			try {
				value=URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Cookie c= new Cookie(cookieName, value);
		c.setMaxAge(7*24*60*60);
		HttpServletResponse res = ActionContext.getContext().getResponse();
		res.addCookie(c);
		return true;
	}
	/**
	 * 删除cookie中某个值
	 * @param cookieName
	 * @param id
	 * @return
	 */
	public static String removeCookie(String cookieName,String id){
		String cookieStr="";
		try {
			id = URLEncoder.encode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie c = getCookieC(cookieName);
		if(c!=null){
			String value =  URLDecoder.decode(c.getValue());
			if(c!=null && !value.equals("")){
				String valuen = "";
				String[] values = value.split(",");
				for(String val:values){
					if(!val.equals(id)){
						valuen+=val+",";
					}
				}
				if(!"".equals(valuen)){
					valuen = valuen.substring(0, valuen.length()-1);
					cookieStr=valuen;
					c.setValue(valuen);
					c.setPath("/");
				}else{
					c.setMaxAge(0);
				}
				HttpServletResponse res = ActionContext.getContext().getResponse();
				res.addCookie(c);
			}
		}
		return cookieStr;
	}
	public static User getUser(){
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MEMBER");
		return user;
	}
	public static User getManageUser(){
		User user = (User) ActionContext.getContext().getSession().getAttribute("DISCO_MANAGEMEMBER");
		return user;
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
        System.out.println("========================域名为：" + tempContextUrl);
        return tempContextUrl;
    }
}
