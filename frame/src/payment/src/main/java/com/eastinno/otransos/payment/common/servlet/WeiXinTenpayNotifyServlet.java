package com.eastinno.otransos.payment.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.payment.common.domain.PayReturnObj;
import com.eastinno.otransos.payment.tencent.weixin.RequestHandler;
import com.eastinno.otransos.payment.tencent.weixin.ResponseHandler;
import com.eastinno.otransos.payment.tencent.weixin.client.ClientResponseHandler;
import com.eastinno.otransos.payment.tencent.weixin.client.TenpayHttpClient;
import com.eastinno.otransos.payment.tencent.weixin.util.ConstantUtil;

/**
 * 微信支付商户异步通知返回
 */
@WebServlet("/weixin_tenpay_notify.otr")
public class WeiXinTenpayNotifyServlet extends BasePayCallSevlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=GBK");
		//---------------------------------------------------------
		//财付通支付通知（后台通知）示例，商户按照此文档进行开发即可
		//---------------------------------------------------------
		//商户号
		String partner = ConstantUtil.PARTNER;

		//密钥
		String key = ConstantUtil.PARTNER_KEY;

		//创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(key);

		//判断签名
		if(resHandler.isTenpaySign()) {
			
			//通知id
			String notify_id = resHandler.getParameter("notify_id");
			
			//创建请求对象
			RequestHandler queryReq = new RequestHandler(null, null);
			//通信对象
			TenpayHttpClient httpClient = new TenpayHttpClient();
			//应答对象
			ClientResponseHandler queryRes = new ClientResponseHandler();
			
			//通过通知ID查询，确保通知来至财付通
			queryReq.init();
			queryReq.setKey(key);
			queryReq.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
			queryReq.setParameter("partner", partner);
			queryReq.setParameter("notify_id", notify_id);
			
			//通信对象
			httpClient.setTimeOut(5);
			//设置请求内容
			httpClient.setReqContent(queryReq.getRequestURL());
			System.out.println("queryReq:" + queryReq.getRequestURL());
			//后台调用
			if(httpClient.call()) {
				//设置结果参数
				try {
					queryRes.setContent(httpClient.getResContent());
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("queryRes:" + httpClient.getResContent());
				queryRes.setKey(key);
					
					
				//获取返回参数
				String retcode = queryRes.getParameter("retcode");
				String trade_state = queryRes.getParameter("trade_state");
			
				String trade_mode = queryRes.getParameter("trade_mode");
					
				//判断签名及结果
				if(queryRes.isTenpaySign()&& "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
					String out_trade_no = CommUtil.null2String(queryRes.getParameter("out_trade_no"));
					String trade_no = CommUtil.null2String(queryRes.getParameter("bank_billno"));
					PayReturnObj payreturn = new PayReturnObj(out_trade_no,trade_no,System.currentTimeMillis()+"");
                    this.payCallOrderServices.updateOrder(payreturn);
					System.out.println("订单查询成功");
					//取结果参数做业务处理				
					System.out.println("out_trade_no:" + queryRes.getParameter("out_trade_no")+
							" transaction_id:" + queryRes.getParameter("transaction_id"));
					System.out.println("trade_state:" + queryRes.getParameter("trade_state")+
							" total_fee:" + queryRes.getParameter("total_fee"));
				        //如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
					System.out.println("discount:" + queryRes.getParameter("discount")+
							" time_end:" + queryRes.getParameter("time_end"));
					//------------------------------
					//处理业务开始
					//------------------------------
					
					//处理数据库逻辑
					//注意交易单不要重复处理
					//注意判断返回金额
					
					//------------------------------
					//处理业务完毕
					//------------------------------
					resHandler.sendToCFT("Success");
				}
				else{
						//错误时，返回结果未签名，记录retcode、retmsg看失败详情。
						System.out.println("查询验证签名失败或业务错误");
						System.out.println("retcode:" + queryRes.getParameter("retcode")+
								" retmsg:" + queryRes.getParameter("retmsg"));
				}
			
			} else {

				System.out.println("后台调用通信失败");
					
				System.out.println(httpClient.getResponseCode());
				System.out.println(httpClient.getErrInfo());
				//有可能因为网络原因，请求已经处理，但未收到应答。
			}
		}
		else{
			System.out.println("通知签名验证失败");
		}
	}

}
