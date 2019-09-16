package com.eastinno.otransos.application.core.mvc;

import com.eastinno.otransos.application.core.domain.Attachment;
import com.eastinno.otransos.application.core.service.IAttachmentService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 
 */
@Action
public class AttachmentAction extends AbstractPageCmdAction {
    @Inject
    private IAttachmentService attachmentService;

    public Page doList(WebForm form) {
        String type = (String) form.get("type");
        QueryObject qo = form.toPo(QueryObject.class);
        if (type == null || !type.equals("0")) {
            qo.addQuery("obj.types", Integer.parseInt(type), "=");
        }
        IPageList pageList = attachmentService.getAttachmentBy(qo);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        String mulitId = CommUtil.null2String((form.get("mulitId")));
        if (mulitId != null && !mulitId.equals("")) {
            String[] ids = mulitId.split(",");
            for (String id : ids) {
                attachmentService.delAttachment(Long.parseLong(id));
            }
        } else {
            Long id = Long.valueOf((String) form.get("id"));
            attachmentService.delAttachment(id);
        }

        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        Attachment object = form.toPo(Attachment.class);
        if (!hasErrors())
            attachmentService.addAttachment(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf((String) form.get("id"));
        Attachment object = attachmentService.getAttachment(id);
        form.toPo(object, true);
        if (!hasErrors())
            attachmentService.updateAttachment(id, object);
        return pageForExtForm(form);
    }

    public IAttachmentService getAttachmentService() {
        return attachmentService;
    }

    public void setAttachmentService(IAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

}