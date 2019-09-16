package com.eastinno.otransos.payment.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.union.pc.util.QuickPayConf;
import com.eastinno.otransos.payment.union.pc.util.QuickPayUtils;

/**
 * 银联支付同步通知
 */
@WebServlet("/union_pc_return.otr")
public class UnionPCReturnServlet extends BasePayCallSevlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(QuickPayConf.charset);
		String[] resArr = new String[QuickPayConf.notifyVo.length]; 
		for(int i=0;i<QuickPayConf.notifyVo.length;i++){
			resArr[i] = request.getParameter(QuickPayConf.notifyVo[i]);
		}
		String signature = request.getParameter(QuickPayConf.signature);
		String signMethod = request.getParameter(QuickPayConf.signMethod);
		
		response.setContentType("text/html;charset="+QuickPayConf.charset);   
		response.setCharacterEncoding(QuickPayConf.charset);
		PrintWriter out = response.getWriter();
		Boolean signatureCheck = new QuickPayUtils().checkSign(resArr, signMethod, signature);
		if(signatureCheck){
			if("00".equals(resArr[10])){
				String out_trade_no = request.getParameter("orderNumber");
				out_trade_no = out_trade_no.substring(3);
				String trade_no = request.getParameter("qn");
                PayReturnObj payreturn = new PayReturnObj(out_trade_no,trade_no,System.currentTimeMillis()+"");
                this.payCallOrderServices.updateOrder(payreturn);
				String returnUrl = this.payCallOrderServices.getReturnUrl();
				returnUrl = returnUrl+((returnUrl.indexOf("?")==-1)?"?":"&")+"uuid="+out_trade_no;
                response.sendRedirect(returnUrl);
			}else{
				out.println("支付失败");
			}
		}else{
			out.println("验证失败");
		}
		
	}
}
