package com.eastinno.otransos.payment.common.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eastinno.otransos.payment.common.service.IPayCallOrderService;

/**
 * 回调基础公共servlet
 */
public class BasePayCallSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected IPayCallOrderService payCallOrderServices;
	@Override
    public void init() throws ServletException {
		System.out.println("==================defail payback innit");
        ServletContext sc = this.getServletContext();
        ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(sc);
        if(this.payCallOrderServices==null){
        	try {
        		payCallOrderServices = (IPayCallOrderService) ac2.getBean("payCallOrderServices");
			} catch (Exception e) {
				payCallOrderServices = (IPayCallOrderService)ac2.getBean("defaultPayCallService");
			}
        }
        System.out.println("============默认回调初始化==============");
        super.init();
    }
}
