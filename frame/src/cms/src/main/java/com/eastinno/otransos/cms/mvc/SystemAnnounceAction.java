package com.eastinno.otransos.cms.mvc;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.SystemAnnounce;
import com.eastinno.otransos.cms.service.ISystemAnnounceService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * SystemAnnounceAction
 */
@Action
public class SystemAnnounceAction extends SaaSCMSBaseAction {

    @Inject
    private ISystemAnnounceService service;

    public void setService(ISystemAnnounceService service) {
        this.service = service;
    }

    public Page doIndex(WebForm f, Module m) {
        return forward("list");
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        if (StringUtils.hasLength(qo.getOrderBy())) {
            qo.setOrderBy("displayTime");
            qo.setOrderType("desc");
        }
        IPageList pageList = service.getSystemAnnounceBy(qo);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        service.delSystemAnnounce(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        SystemAnnounce object = form.toPo(SystemAnnounce.class);
        if (!hasErrors())
            service.addSystemAnnounce(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        SystemAnnounce object = service.getSystemAnnounce(id);
        form.toPo(object, true);
        if (!hasErrors())
            service.updateSystemAnnounce(id, object);
        return pageForExtForm(form);
    }
}