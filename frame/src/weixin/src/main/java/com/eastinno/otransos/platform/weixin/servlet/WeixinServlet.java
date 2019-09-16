package com.eastinno.otransos.platform.weixin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IWeiXinHandlerService;
import com.eastinno.otransos.platform.weixin.service.impl.WeiXinHandlerServiceImpl;
import com.eastinno.otransos.platform.weixin.util.WeixinUtils;

/**
 * 微信消息推送服务
 * 
 * @author maowei
 * @createDate 2013-9-5下午1:31:05
 */
@WebServlet(urlPatterns = "/weixinServlet.otr")
public class WeixinServlet extends HttpServlet {
    private IAccountService accountService;
    private ApplicationContext ac;

    private static final long serialVersionUID = 1L;
    private final Logger logger = Logger.getLogger(WeixinServlet.class);

    @Override
    public void init() throws ServletException {
    	if(this.ac==null){
    		ServletContext sc = this.getServletContext();
            this.ac = WebApplicationContextUtils.getWebApplicationContext(sc);
    	}
        if (this.accountService == null) {
            this.accountService = ac.getBean(IAccountService.class);
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String signature = req.getParameter("signature");// 微信加密签名
        String timestamp = req.getParameter("timestamp");// 时间戳
        String nonce = req.getParameter("nonce");// 随机数
        String echostr = req.getParameter("echostr");
        String appId = req.getParameter("appId");
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
            resp.getWriter().write(echostr);
        } else {
            logger.error("数据库中没有对应的token！！！");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String appId = req.getParameter("appId");
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.appid",appId,"=");
        qo.setPageSize(1);
        List<Account> accounts = this.accountService.getAccountBy(qo).getResult();
        if(accounts!=null && accounts.size()>0){
        	Account account = accounts.get(0);
        	String handlerName = account.getHandlerName();
        	IWeiXinHandlerService weiXinHandlerService;
        	if(handlerName!=null && !"".equals(handlerName)){
        		weiXinHandlerService = (IWeiXinHandlerService) ac.getBean(handlerName);
        	}else{
        		weiXinHandlerService = (IWeiXinHandlerService) ac.getBean("weiXinHandlerServiceImpl");
        	}
        	String respMessage = weiXinHandlerService.parseHandler(req);
            PrintWriter out = resp.getWriter();
            out.print(respMessage);
            out.close();
        }
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

}
