package com.eastinno.otransos.seafood.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.eastinno.otransos.web.ActionContext;

/**
 * 表单令牌工具类
 * @author Wb
 *
 */
public class TokenUtil {
	public static String SESSION_TOKEN_KEY = "FORM_TOKENS";
	
	/**
	 * 创建存储在session中的token数组
	 * 1.生成token字符串
	 * 2.并将其放入session中
	 * 3.键名为SESSION_TOKEN_KEY
	 * 4.键值类型为数组类型
	 * @return
	 */
	public static String createSessionToken(){
		String token = TokenUtil.createToken();
		while(TokenUtil.existsSessionToken(token)){
			token = TokenUtil.createToken(); 
		}
		TokenUtil.putTokenIntoSession(token);
		return token;
	}
	
	/**
	 * 校验token是否存在于session的token数组
	 * 1.检查是否存在token，存在返回true，不存在返回false
	 * 2.如果存在token，则校验后删除token
	 * @param token
	 * @return
	 */
	public static boolean existsSessionTokenThenDelete(String token){		
		boolean result = false;
		result = TokenUtil.existsSessionToken(token);
		if(result){
			TokenUtil.deleteSessionToken(token);
		}
		return result;
	}
	
	/**
	 * 根据当前时间戳和随机数，生成token字符串
	 * @return
	 */
	private static String createToken(){
		String result = "";
		result += System.currentTimeMillis()+""+(int)(Math.random()*10000);
		return result;
	}
	
	/**
	 * 将token字符串存入SESSION_TOKEN_KEY对应的session数组中
	 * @param token
	 * @return
	 */
	private static boolean putTokenIntoSession(String token){
		HttpSession session = ActionContext.getContext().getSession();
		List tokens = (List)session.getAttribute(TokenUtil.SESSION_TOKEN_KEY);
		
		//检查token是否为空
		if(StringUtils.isEmpty(token)){
			return false;
		}
		
		if(tokens == null){
			tokens = new ArrayList<String>();
			session.setAttribute(TokenUtil.SESSION_TOKEN_KEY, tokens);
		}
		if(!TokenUtil.existsSessionToken(token)){
			tokens.add(token);
		}				
		return true;
	}
	
	/**
	 * 判断SESSION_TOKEN_KEY所对应的session数组中是否存在token值
	 * @param token
	 * @return
	 */
	private static boolean existsSessionToken(String token){
		boolean result = false;
		HttpSession session = ActionContext.getContext().getSession();
		List tokens = (List)session.getAttribute(TokenUtil.SESSION_TOKEN_KEY);
		if(tokens != null){
			for(int i=0; i<tokens.size(); ++i){
				if(tokens.get(i).equals(token)){
					result = true;
					break;
				}
			}	
		}		
		return result;
	}
	
	/**
	 * 删除SESSION_TOKEN_KEY所对应的session数组中存在token值
	 * @param token
	 * @return
	 */
	private static void deleteSessionToken(String token){		
		HttpSession session = ActionContext.getContext().getSession();
		List tokens = (List)session.getAttribute(TokenUtil.SESSION_TOKEN_KEY);
		int equalIndex = -1;
		if(!StringUtils.isEmpty(token) && tokens!=null){
			for(int i=0; i<tokens.size(); ++i){
				if(tokens.get(i).equals(token)){
					equalIndex = i;
					break;
				}
			}	
		}
		if(equalIndex != -1){
			tokens.remove(equalIndex);
		}
		session.setAttribute(TokenUtil.SESSION_TOKEN_KEY, tokens);
	}
	
	private static void printSessionToken(){
		HttpSession session = ActionContext.getContext().getSession();
		List tokens = (List)session.getAttribute(TokenUtil.SESSION_TOKEN_KEY);
		System.out.println("==============FORMTOKEN LIST INFO--BEGIN==============");
		if(tokens!=null){
			for(int i=0; i<tokens.size(); ++i){
				System.out.println("FORMTOKEN."+i+":"+tokens.get(i));
			}	
		}
		System.out.println("==============FORMTOKEN LIST INFO--END==============");
	}
	
	public static void main(String args[]){
		System.out.println(TokenUtil.createToken());
//		System.out.println(TokenUtil.createSessionToken());
	}
}
