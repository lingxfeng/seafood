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

import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.union.app.service.UnionService;

/**
 * 银联支付支付app端前台通知
 */
@WebServlet("/unicon_app_return.otr")
public class UnionAppReturnServlet extends BasePayCallSevlet {
    private static final long serialVersionUID = 1L;
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        if (UnionService.verifySignature(params)) {// 服务器签名验证成功
            // 请在这里加上商户的业务逻辑程序代码
            // 获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
            String transStatus = request.getParameter("transStatus");// 交易状态
            if (null != transStatus && transStatus.equals("00")) {
                // 交易处理成功
                String out_trade_no = params.get("orderNumber");
                out_trade_no = out_trade_no.substring(3);
                String trade_no = params.get("qn");
                PayReturnObj payreturn = new PayReturnObj(out_trade_no,trade_no,System.currentTimeMillis()+"");
                this.payCallOrderServices.updateOrder(payreturn);
                String returnUrl = "";
                returnUrl = this.payCallOrderServices.getReturnUrl();
                returnUrl = returnUrl+((returnUrl.indexOf("?")==-1)?"?":"&")+"uuid="+out_trade_no;
                response.sendRedirect(returnUrl);
            } else {
                out.println("支付失败");
            }
        } else {// 服务器签名验证失败
            out.println("支付失败");
        }
    }

}
