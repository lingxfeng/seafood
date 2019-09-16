package com.eastinno.otransos.platform.weixin.mvc;

import java.util.List;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.AutoResponse;
import com.eastinno.otransos.platform.weixin.service.IAutoResponseService;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 模版管理
 * 
 * @author nsz
 */
public class AutoResponseAction extends AbstractPageCmdAction {
    @Inject
    private IAutoResponseService service;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        String accountId = CommUtil.null2String(form.get("accountId"));
        if (!"".equals(accountId)) {
            qo.addQuery("obj.account.id", Long.parseLong(accountId), "=");
        }
        Tenant tenant = TenantContext.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        IPageList pl = service.getAutoResponseBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    /**
     * 添加
     * 
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        AutoResponse entity = form.toPo(AutoResponse.class);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.keyWord", entity.getKeyWord(), "=");
        qo.addQuery("obj.account", entity.getAccount(), "=");
        List<?> list = this.service.getAutoResponseBy(qo).getResult();
        if (list != null && list.size() > 0) {
            this.addError("msg", "该关键字已经存在！！！");
        }
        Tenant tenant = TenantContext.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
            entity.setTenant(tenant);
        }
        if (!hasErrors()) {
            Long ret = this.service.addAutoResponse(entity);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 修改
     * 
     * @param form
     * @return
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        AutoResponse entity = service.getAutoResponse(id);
        form.toPo(entity, true);
        if (!hasErrors()) {
            boolean ret = service.updateAutoResponse(id, entity);
            if (ret) {
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 删除
     * 
     * @param form
     * @return
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delAutoResponse(id);
        return pageForExtForm(form);
    }

    public void setService(IAutoResponseService service) {
        this.service = service;
    }

}
