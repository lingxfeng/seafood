package com.eastinno.otransos.platform.weixin.mvc;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.dbo.beans.BeanUtils;
import com.eastinno.otransos.platform.weixin.domain.FollowerMessage;
import com.eastinno.otransos.platform.weixin.service.IFollowerMessageService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * FollowerMessageAction
 * 
 * @author nsz
 */
@Action
public class FollowerMessageAction extends AbstractPageCmdAction {

    @Inject
    private IFollowerMessageService service;
    @Inject
    private IFollowerService followerService;

    /**
     * 查询48小时消息列表
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        qo.setOrderBy("createDate desc");
        String accountId = CommUtil.null2String(form.get("accountId"));
        if (!"".equals(accountId)) {
            qo.addQuery("obj.account.id", Long.parseLong(accountId), "=");
        }
        String followerId = CommUtil.null2String(form.get("followerId"));
        if (!"".equals(followerId)) {
            qo.addQuery("obj.follower.id", Long.parseLong(followerId), "=");
        }
        IPageList pageList = this.service.getFollowerMessageBy(qo);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = BeanUtils.convertType(form.get("id"), Long.class);
        service.delFollowerMessage(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        FollowerMessage object = form.toPo(FollowerMessage.class);
        if (!hasErrors())
            service.addFollowerMessage(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = (Long) BeanUtils.convertType(form.get("id"), Long.class);
        FollowerMessage object = service.getFollowerMessage(id);
        form.toPo(object, true);
        if (!hasErrors())
            service.updateFollowerMessage(id, object);
        return pageForExtForm(form);
    }

    public void setService(IFollowerMessageService service) {
        this.service = service;
    }

    public void setFollowerService(IFollowerService followerService) {
        this.followerService = followerService;
    }

}