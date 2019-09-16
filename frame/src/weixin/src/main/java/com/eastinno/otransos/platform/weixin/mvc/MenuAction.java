package com.eastinno.otransos.platform.weixin.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Menu;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.platform.weixin.service.IMenuService;
import com.eastinno.otransos.platform.weixin.util.RequestWxUtils;
import com.eastinno.otransos.platform.weixin.util.WeixinUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
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
public class MenuAction extends AbstractPageCmdAction {
    @Inject
    private IMenuService service;
    @Inject
    private IAccountService accountService;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        qo.setOrderBy("sequence");
        String accountId = CommUtil.null2String(form.get("accountId"));
        String parentId = CommUtil.null2String(form.get("parentId"));
        if (!"".equals(accountId)) {
            qo.addQuery("obj.account.id", Long.parseLong(accountId), "=");
        }
        if (!"".equals(parentId)) {
            qo.addQuery("obj.parent.id", Long.parseLong(parentId), "=");
        }else{
        	qo.addQuery("obj.parent is EMPTY");
        }
        Tenant tenant = ShiroUtils.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }else{
        	qo.addQuery("obj.tenant is EMPTY");
        }
        IPageList pl = service.getMenuBy(qo);
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
        Menu entity = form.toPo(Menu.class);
        Tenant tenant = ShiroUtils.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
            entity.setTenant(tenant);
        }
        QueryObject qo = new QueryObject();
        Menu parent = entity.getParent();
        if (parent == null) {
            qo.addQuery("obj.parent is EMPTY");
        } else {
            qo.addQuery("obj.parent", parent, "=");
        }
        qo.setOrderBy("sequence desc");
        qo.setPageSize(1);
        List<Menu> list = this.service.getMenuBy(qo).getResult();
        if (list != null && list.size() > 0) {
            entity.setSequence(list.get(0).getSequence() + 1);
        }
        qo = new QueryObject();
        qo.addQuery("obj.account", entity.getAccount(), "=");
        qo.addQuery("obj.menuKey", entity.getMenuKey(), "=");
        List<?> keylist = this.service.getMenuBy(qo).getResult();
        if (keylist != null && keylist.size() > 0) {
            this.addError("msg", "菜单编码不能重复！！！");
        }
        if (!hasErrors()) {
            Long ret = this.service.addMenu(entity);
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
        Menu entity = service.getMenu(id);
        form.toPo(entity, false, true);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.account", entity.getAccount(), "=");
        qo.addQuery("obj.menuKey", entity.getMenuKey(), "=");
        qo.addQuery("obj.id", id, "<>");
        qo.setPageSize(1);
        List<?> list = this.service.getMenuBy(qo).getResult();
        if (list != null && list.size() > 0) {
            this.addError("msg", "菜单编码不能重复！！！");
        }
        if (!hasErrors()) {
            boolean ret = service.updateMenu(id, entity);
            if (ret) {
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 获得模版树
     * 
     * @param form
     * @return
     */
    public Page doGetTree(WebForm form) {
        String parentId = CommUtil.null2String(form.get("id"));
        String accountId = CommUtil.null2String(form.get("accountId"));
        QueryObject qo = new QueryObject();
        if (!"".equals(parentId)) {
            qo.addQuery("obj.parent.id", Long.parseLong(parentId), "=");
        } else {
            qo.addQuery("obj.parent is EMPTY");
        }
        if (!"".equals(accountId)) {
            qo.addQuery("obj.account.id", Long.parseLong(accountId), "=");
        }
        Tenant tenant = ShiroUtils.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        List<?> list = this.service.getMenuBy(qo).getResult();
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Menu object = (Menu) list.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", object.getId() + "");
                map.put("text", object.getName());
                map.put("leaf", object.getChildren().size() < 1);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "无菜单");
            map.put("id", "0");
            map.put("leaf", true);
            nodes.add(map);
        }

        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    /**
     * 删除
     * 
     * @param form
     * @return
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delMenu(id);
        return pageForExtForm(form);
    }

    /**
     * 发布菜单到微信平台
     * 
     * @param form
     * @return
     */
    public Page doCreateMenuToweixin(WebForm form) {
        Long accountId = new Long(CommUtil.null2String(form.get("accountId")));
        Account account = this.accountService.getAccount(accountId);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.account", account, "=");
        qo.addQuery("obj.parent is EMPTY");
        qo.setOrderBy("sequence");
        List<Menu> menus = this.service.getMenuBy(qo).getResult();
        Map<String, Object> map = new HashMap<String, Object>();
        map = WeixinUtils.menuToMap(menus, map, account, true);
        JSONObject jo = new JSONObject(map);
        String jsonmenu = jo.toJSONString();
        JSONObject result = RequestWxUtils.createMenu(account, jsonmenu);
        form.jsonResult(result);
        return Page.JSONPage;
    }

    /**
     * 上移下移
     * 
     * @param form
     * @return
     */
    public Page doSwapSequence(WebForm form) {
        QueryObject qo = new QueryObject();
        String id = CommUtil.null2String(form.get("id"));
        String down = CommUtil.null2String(form.get("down"));
        Menu curObject = this.service.getMenu(Long.parseLong(id));
        int curse = curObject.getSequence();
        if (curObject.getParent() == null) {
            qo.addQuery("obj.parent is EMPTY");
        } else {
            qo.addQuery("obj.parent", curObject.getParent(), "=");
        }
        if (!"".equals(down)) {
            qo.addQuery("obj.sequence", curObject.getSequence(), ">");
            qo.setOrderBy("sequence");
        } else {
            qo.addQuery("obj.sequence", curObject.getSequence(), "<");
            qo.setOrderBy("sequence desc");
        }
        qo.setPageSize(1);
        List<Menu> list = this.service.getMenuBy(qo).getResult();
        if (list != null && list.size() > 0) {
            Menu otherObject = list.get(0);
            curObject.setSequence(otherObject.getSequence());
            otherObject.setSequence(curse);
            this.service.updateMenu(curObject.getId(), curObject);
            this.service.updateMenu(otherObject.getId(), otherObject);
        }
        return pageForExtForm(form);
    }

    public void setService(IMenuService service) {
        this.service = service;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

}
