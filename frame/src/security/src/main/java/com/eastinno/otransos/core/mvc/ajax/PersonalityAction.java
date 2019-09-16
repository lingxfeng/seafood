package com.eastinno.otransos.core.mvc.ajax;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.Personality;
import com.eastinno.otransos.core.service.IPersonalityService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class PersonalityAction extends AbstractPageCmdAction {

    @Inject
    private IPersonalityService service;

    public void setService(IPersonalityService service) {
        this.service = service;
    }

    public Page doIndex(WebForm f, Module m) {
        return forward("list");
    }

    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getPersonalityBy(qo);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delPersonality(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        Personality object = (Personality) form.toPo(Personality.class);
        if (!hasErrors())
            this.service.addPersonality(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        Personality object = this.service.getPersonality(id);
        form.toPo(object, true);
        if (!hasErrors())
            this.service.updatePersonality(id, object);
        return pageForExtForm(form);
    }
}
