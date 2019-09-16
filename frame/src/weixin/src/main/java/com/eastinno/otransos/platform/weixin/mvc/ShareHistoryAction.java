package com.eastinno.otransos.platform.weixin.mvc;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.service.IShareHistoryService;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 分享记录action
 * 
 * @author nsz
 */
public class ShareHistoryAction extends AbstractPageCmdAction {
    @Inject
    private IShareHistoryService shareHistoryService;

    public void setShareHistoryService(IShareHistoryService shareHistoryService) {
        this.shareHistoryService = shareHistoryService;
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        String followerId = CommUtil.null2String(form.get("followerId"));
        if (!"".equals(followerId)) {
            qo.addQuery("obj.follower.id", Long.parseLong(followerId), "=");
        }
        Tenant tenant = TenantContext.getTenant();
        qo.addQuery("obj.tenant", tenant, "=");
        IPageList pl = shareHistoryService.getShareHistoryBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }
}
