package com.eastinno.otransos.cms.mvc;

import com.eastinno.otransos.cms.domain.GuestBook;
import com.eastinno.otransos.cms.service.IGuestBookService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.dbo.beans.BeanUtils;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class GuestBookAction extends SaaSCMSBaseAction {

    @Inject
    private IGuestBookService service;

    public void setService(IGuestBookService service) {
        this.service = service;
    }

    public Page doIndex(WebForm f, Module m) {
        return page("list");
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        IPageList pageList = service.getGuestBookBy(qo);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = BeanUtils.convertType(form.get("id"), Long.class);
        service.delGuestBook(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        GuestBook object = form.toPo(GuestBook.class);
        if (!hasErrors())
            service.addGuestBook(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = (Long) BeanUtils.convertType(form.get("id"), Long.class);
        GuestBook object = service.getGuestBook(id);
        form.toPo(object, true);
        if (!hasErrors())
            service.updateGuestBook(id, object);
        return pageForExtForm(form);
    }
}