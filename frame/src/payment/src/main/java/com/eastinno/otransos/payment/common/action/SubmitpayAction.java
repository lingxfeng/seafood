package com.eastinno.otransos.payment.common.action;

import java.util.HashMap;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.payment.common.domain.PayParamsObj;
import com.eastinno.otransos.payment.common.util.PaymentUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

/**
 * 支付系统app端入口
 */
@Action
public class SubmitpayAction extends AbstractPageCmdAction {
    @Override
    public Page doInit(WebForm form, Module module) {
        String orderId = CommUtil.null2String(form.get("orderId"));
        String orderName = CommUtil.null2String(form.get("orderName"));
        String orderDesc = CommUtil.null2String(form.get("orderDesc"));
        String payType = CommUtil.null2String(form.get("payType"));
        String total_fee = CommUtil.null2String(form.get("total_fee"));
        PayParamsObj payParams = new PayParamsObj();
        payParams.setOrderDesc(orderDesc);
        payParams.setOrderId(orderId);
        payParams.setOrderName(orderName);
        payParams.setTotal_fee(total_fee);
        String msg = "对不起，支付失败";
        try {
            msg = PaymentUtil.paystr(payParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        form.jsonResult(msg);
        return Page.JSONPage;
    }

}
