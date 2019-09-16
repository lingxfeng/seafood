package com.eastinno.otransos.platform.weixin.mvc;

import java.util.Date;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IFollowerService;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 粉丝
 * 
 * @author nsz
 */
public class FollowerAction extends AbstractPageCmdAction {
    @Inject
    private IFollowerService service;
    @Inject
    private IAccountService accountService;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        String searchKey = CommUtil.null2String(form.get("searchKey"));
        if (!"".equals(searchKey)) {
            qo.addQuery("obj.nickname", "%" + searchKey + "%", "like");
        }
        String accountId = CommUtil.null2String(form.get("accountId"));
        if (!"".equals(accountId)) {
            qo.addQuery("obj.account.id", Long.parseLong(accountId), "=");
        }
        Tenant tenant = TenantContext.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        String ismsg = CommUtil.null2String(form.get("ismsg"));
        if (!"".equals(ismsg)) {
            qo.addQuery("obj.lastTimesend", (new Date().getTime() - 48 * 60 * 60 * 1000), ">");
        }
        IPageList pl = service.getFollowerBy(qo);
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
        Follower follower = form.toPo(Follower.class);
        Tenant tenant = TenantContext.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
            follower.setTenant(tenant);
        }
        if (!hasErrors()) {
            Long ret = this.service.addFollower(follower);
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
        Follower follower = service.getFollower(id);
        form.toPo(follower, true);
        if (!hasErrors()) {
            boolean ret = service.updateFollower(id, follower);
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
        this.service.delFollower(id);
        return pageForExtForm(form);
    }

    /**
     * 同步微信服务器粉丝
     * 
     * @param form
     * @return
     */
    public Page doUpdateWxFollower(WebForm form) {
        String accountId = CommUtil.null2String(form.get("accountId"));
        Account account = this.accountService.getAccount(Long.parseLong(accountId));
        this.service.updateWxUser(account, null);
        return Page.JSONPage;
    }

    public void setService(IFollowerService service) {
        this.service = service;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }
}
