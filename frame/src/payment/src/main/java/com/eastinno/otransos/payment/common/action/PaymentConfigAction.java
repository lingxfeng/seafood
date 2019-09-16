package com.eastinno.otransos.payment.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.dbo.beans.BeanUtils;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 支付平台配置管理
 * 
 * @author Administrator
 */
@Action
public class PaymentConfigAction extends AbstractPageCmdAction {

    @Inject
    private IPaymentConfigService service;

    public void setService(IPaymentConfigService service) {
        this.service = service;
    }

    public Page doIndex(WebForm f, Module m) {
        return page("list");
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        TenantObjectUtil.addQuery(qo);
        IPageList pageList = service.getPaymentConfigBy(qo);
        form.jsonResult(pageList);
        return Page.JSPage;
    }

    public Page doRemove(WebForm form) {
        Long id = BeanUtils.convertType(form.get("id"), Long.class);
        service.delPaymentConfig(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doSave(WebForm form) {
        PaymentConfig object = form.toPo(PaymentConfig.class);
        String logo = FileUtil.uploadFile(form, "logo", UploadFileConstant.FILE_UPLOAD_PATH + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(logo)) {
            object.setLogo(logo);
        }
        if (!hasErrors())
            service.addPaymentConfig(object);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        Long id = (Long) BeanUtils.convertType(form.get("id"), Long.class);
        PaymentConfig object = service.getPaymentConfig(id);
        String logoOld = object.getLogo();
        String logo = FileUtil.uploadFile(form, "logo", UploadFileConstant.FILE_UPLOAD_PATH + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(logo)) {
            object.setLogo(logo);
            FileUtil.delFile(Globals.APP_BASE_DIR + logoOld);
        }
        form.toPo(object, true);
        if (!hasErrors())
            service.updatePaymentConfig(id, object);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doGetPayType(WebForm form) {
        List<Map<String, String>> nodes = new ArrayList<Map<String, String>>();
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.status", new Integer(1), "=");
        List<?> list = this.service.getPaymentConfigBy(qo).getResult();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                PaymentConfig paymentConfig = (PaymentConfig) obj;
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", paymentConfig.getId() + "");
                map.put("name", paymentConfig.getName());
                map.put("logo", paymentConfig.getLogo());
                map.put("intro", paymentConfig.getIntro());
                nodes.add(map);
            }
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "0");
            map.put("name", "无支付方式");
            map.put("logo", "");
            map.put("intro", "");
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }
}