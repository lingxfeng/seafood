package com.eastinno.otransos.payment.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.payment.alipay.mobile.web.util.AlipayNotify;
import com.eastinno.otransos.payment.common.domain.PayReturnObj;

/**
 * 支付宝手机网页版同步通知
 */
@WebServlet("/alipay_mobile_web_return.otr")
public class AlipayMobileWebReturnServlet extends BasePayCallSevlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		boolean verify_result = AlipayNotify.verifyReturn(params);
		if(!verify_result){
			params = new HashMap<String,String>();
			for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			}
			verify_result = AlipayNotify.verifyReturn(params);
		}
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String result = new String(request.getParameter("result").getBytes("ISO-8859-1"),"UTF-8");

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			
			//计算得出通知验证结果
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			String returnUrl = "";
			if("success".equals(result)){
				PayReturnObj payreturn = new PayReturnObj(out_trade_no,trade_no,System.currentTimeMillis()+"");
                this.payCallOrderServices.updateOrder(payreturn);
				returnUrl = this.payCallOrderServices.getReturnUrl();
				returnUrl = returnUrl+((returnUrl.indexOf("?")==-1)?"?":"&")+"uuid="+out_trade_no;
				response.sendRedirect(returnUrl);
			}
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			//该页面可做页面美工编辑
			out.println("验证失败");
		}
	}

}
