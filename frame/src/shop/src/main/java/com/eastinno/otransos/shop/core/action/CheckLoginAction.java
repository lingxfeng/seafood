package com.eastinno.otransos.shop.core.action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
/**
 * 公共action
 * @author dll
 */
@Action
public class CheckLoginAction extends AbstractPageCmdAction{
	
	/**
	 * 判断是否登录，未登录则跳转登录页面
	 * @return 
	 */
	@Override
	public Object doBefore(WebForm form, Module module) {
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()){
			return new Page("/shopmanage/login.html");
		}
		return super.doBefore(form, module);
	}
	/**
	 * 跳转到指定页面
	 * @param form
	 * @return
	 */
	public Page doToPage(WebForm form){
		String toPage = CommUtil.null2String(form.get("topage"));
		if(!StringUtils.hasText(toPage)){
			toPage = "/index.html";
		}
		return new Page(toPage);
	}
	
}
