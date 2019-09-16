package com.eastinno.otransos.core.mvc.ajax;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.DynamicStaticPair;
import com.eastinno.otransos.core.service.IDynamicStaticPairService;
import com.eastinno.otransos.core.service.IHtmlGeneratorService;
import com.eastinno.otransos.core.support.ActionUtil;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.service.impl.TenantObjectUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.AutoChangeLink;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * @intro DynamicStaticPairAction
 * @version v0.1
 * @author maowei
 * @since 2013年5月6日 上午11:22:58
 */
@Action(path = "publish")
public class DynamicStaticPairAction extends AbstractPageCmdAction {
    @Inject
    private IDynamicStaticPairService service;
    @Inject
    private IHtmlGeneratorService htmlGenerator;

    public void setService(IDynamicStaticPairService service) {
        this.service = service;
    }

    public void setHtmlGenerator(IHtmlGeneratorService htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }

    public Page doGenerator(WebForm form) {
        List<Serializable> ids = ActionUtil.processIds(form);
        int count = 0;
        if (ids != null && ids.size() > 0) {
            for (int i = 0; i < ids.size(); i++) {
                DynamicStaticPair dsp = this.service.getDynamicStaticPair((Long) ids.get(i));
                AutoChangeLink[] auto = dsp.getAutoChangeLinks();
                for (int j = 0; j < auto.length; j++) {
                    this.htmlGenerator.process(auto[j]);
                    count++;
                }
                dsp.setVdate(new Date());
            }
            form.getTextElement().clear();
            form.jsonResult("成功生成" + count + "条记录！");
        }
        return pageForExtForm(form);
    }

    public Page doIndex(WebForm f) {
        return forward("list");
    }

    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        TenantObjectUtil.addQuery(qo);
        IPageList pageList = this.service.getDynamicStaticPairBy(qo);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        service.delDynamicStaticPair(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        DynamicStaticPair object = form.toPo(DynamicStaticPair.class);
        if (!hasErrors()) {
            service.addDynamicStaticPair(object);
        }
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        DynamicStaticPair object = service.getDynamicStaticPair(id);
        form.toPo(object, true);
        if (!hasErrors())
            service.updateDynamicStaticPair(id, object);
        return pageForExtForm(form);
    }
}
