package com.eastinno.otransos.platform.weixin.mvc;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.service.IPrizeHistoryService;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 获奖记录action
 * 
 * @author nsz
 */
public class PrizeHistoryAction extends AbstractPageCmdAction {
    @Inject
    private IPrizeHistoryService prizeHistoryService;

    public void setPrizeHistoryService(IPrizeHistoryService prizeHistoryService) {
        this.prizeHistoryService = prizeHistoryService;
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        String followerId = CommUtil.null2String(form.get("followerId"));
        if (!"".equals(followerId)) {
            qo.addQuery("obj.follower.id", Long.parseLong(followerId), "=");
        }
        Tenant tenant = TenantContext.getTenant();
        qo.addQuery("obj.tenant", tenant, "=");
        IPageList pl = prizeHistoryService.getPrizeHistoryBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }
}
