package com.eastinno.otransos.security.mvc.ajax;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.query.DomainLogQuery;
import com.eastinno.otransos.security.service.IDomainOperationService;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;
import com.eastinno.otransos.web.core.AbstractCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class DomainLogAction extends AbstractCmdAction {
    private IDomainOperationService service;

    public void setService(IDomainOperationService service) {
        this.service = service;
    }

    public Page doInit(WebForm form, Module module) {
        if (UserContext.getUser() != null) {
            DomainLogQuery qo = (DomainLogQuery) form.toPo(DomainLogQuery.class);
            if ((!"".equals(qo.getEntity())) && (!"".equals(qo.getId()))) {
                IPageList pageList = this.service.getDomainOperationLogBy(qo);
                AjaxUtil.convertEntityToJson(pageList);
                form.jsonResult(pageList);
            }
        }
        return Page.JSONPage;
    }

    public Page doList(WebForm form, Module module) {
        if (UserContext.getUser() != null) {
            QueryObject qo = (QueryObject) form.toPo(DomainLogQuery.class);
            qo.setPageSize(Integer.valueOf(20));
            IPageList pageList = this.service.getDomainOperationLogBy(qo);
            AjaxUtil.convertEntityToJson(pageList);
            form.jsonResult(pageList);
        }
        return Page.JSONPage;
    }

    public Page doLoad(WebForm form, Module module) throws Exception {
        DomainLogQuery qo = (DomainLogQuery) form.toPo(DomainLogQuery.class);
        Object obj = this.service.getObject(Class.forName(qo.getEntity()), qo.getId(), qo.getVer());
        if ((obj != null) && ((obj instanceof IJsonObject))) {
            obj = ((IJsonObject) obj).toJSonObject();
        }
        form.jsonResult(obj);
        return Page.JSONPage;
    }

    public Page doRevert(WebForm form, Module module) throws Exception {
        DomainLogQuery qo = (DomainLogQuery) form.toPo(DomainLogQuery.class);
        if ((!"".equals(qo.getEntity())) && (!"".equals(qo.getId()))) {
            this.service.revert(Class.forName(qo.getEntity()), qo.getId(), qo.getVer());
        }
        return pageForExtForm(form);
    }
}
