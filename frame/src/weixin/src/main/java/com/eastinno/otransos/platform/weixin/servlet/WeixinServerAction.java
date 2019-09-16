package com.eastinno.otransos.platform.weixin.servlet;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IWeiXinHandlerService;
import com.eastinno.otransos.platform.weixin.util.WeixinUtils;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * 微信消息推送服务
 * 
 * @author maowei
 * @createDate 2013-9-5下午1:31:05
 */
@Action
public class WeixinServerAction extends AbstractPageCmdAction{
	@Inject
	private IAccountService accountService;
	@Inject
	private IWeiXinHandlerService weiXinHandlerService;
	
	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	public void setWeiXinHandlerService(IWeiXinHandlerService weiXinHandlerService) {
		this.weiXinHandlerService = weiXinHandlerService;
	}


	@Override
	public Page doInit(WebForm form, Module module) {
		HttpServletRequest req = ActionContext.getContext().getRequest();
		HttpServletResponse res = ActionContext.getContext().getResponse();
		try {
			req.setCharacterEncoding("UTF-8");
			res.setCharacterEncoding("UTF-8");
			String method = req.getMethod();
			if("GET".equalsIgnoreCase(method)){
				 String signature = CommUtil.null2String(form.get("signature"));// 微信加密签名
			        String timestamp = CommUtil.null2String(form.get("timestamp"));// 时间戳
			        String nonce = CommUtil.null2String(form.get("nonce"));// 随机数
			        String echostr = CommUtil.null2String(form.get("echostr"));
			        String appId = CommUtil.null2String(form.get("appId"));
			        boolean ischeck = false;
			        QueryObject qo = new QueryObject();
			        qo.addQuery("obj.appid",appId,"=");
			        qo.setPageSize(1);
			        List<Account> accounts = this.accountService.getAccountBy(qo).getResult();
			        if(accounts!=null && accounts.size()>0){
			        	Account account = accounts.get(0);
			        	if (WeixinUtils.checkSignature(signature, timestamp, nonce, account.getToken())) {
			                ischeck = true;
			            }
			        }
			        if (ischeck) {
			        	res.getWriter().write(echostr);
			        }
			}else{
				String respMessage = weiXinHandlerService.parseHandler(req);
	            PrintWriter out = res.getWriter();
	            out.print(respMessage);
	            out.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return Page.nullPage;
	}
	
}
