package com.eastinno.otransos.core.mvc.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.domain.BaseUIConfig;
import com.eastinno.otransos.core.service.IBaseUIConfigService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class BaseUIConfigAction extends AbstractPageCmdAction {

    @Inject
    private IBaseUIConfigService service;

    public void setService(IBaseUIConfigService service) {
        this.service = service;
    }

    public Page doIndex(WebForm f, Module m) {
        return page("list");
    }

    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String parentId = CommUtil.null2String(form.get("parentId"));
        String all = CommUtil.null2String(form.get("all"));
        if (parentId.equals("")) {
            if (!"true".equals(all))
                qo.addQuery("obj.parent is NULL", null);
        } else {
            qo.addQuery("obj.parent.id", Long.valueOf(Long.parseLong(parentId)), "=");
        }
        IPageList pageList = this.service.getBaseUIConfigBy(qo);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delBaseUIConfig(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        BaseUIConfig object = (BaseUIConfig) form.toPo(BaseUIConfig.class);
        if (!hasErrors())
            this.service.addBaseUIConfig(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        BaseUIConfig object = this.service.getBaseUIConfig(id);
        form.toPo(object, true);
        if (!hasErrors())
            this.service.updateBaseUIConfig(id, object);
        return pageForExtForm(form);
    }

    private Map obj2treemap(BaseUIConfig category, boolean checked, boolean deep) {
        Map map = new HashMap();
        map.put("id", category.getId());
        map.put("text", category.getTitle());
        map.put("appClass", category.getAppClass());
        map.put("scripts", category.getScripts());
        map.put("leaf", Boolean.valueOf(category.getChildren().size() < 1));
        if (checked)
            map.put("checked", Boolean.valueOf(false));
        map.put("icon", "images/menuPanel/tag_blue3.gif");
        if ((deep) && (category.getChildren().size() > 0)) {
            List list = new ArrayList();
            for (BaseUIConfig d : category.getChildren())
                list.add(obj2treemap(d, checked, deep));
            map.put("children", list);
        }
        return map;
    }

    public Page doGetTree(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        String prefix = CommUtil.null2String(form.get("prefix"));
        if (!"".equals(prefix)) {
            id = id.replaceAll(prefix, "");
        }
        QueryObject query = new QueryObject();
        query.setPageSize(Integer.valueOf(-1));
        if (!"".equals(id)) {
            BaseUIConfig parent = this.service.getBaseUIConfig(new Long(id));
            query.addQuery("obj.parent", parent, "=");
        } else {
            query.addQuery("obj.parent is EMPTY", null);
        }
        IPageList pageList = this.service.getBaseUIConfigBy(query);
        String checked = CommUtil.null2String(form.get("checked"));
        String all = CommUtil.null2String(form.get("all"));
        List nodes = new ArrayList();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                BaseUIConfig category = (BaseUIConfig) pageList.getResult().get(i);
                Map map = obj2treemap(category, !"".equals(checked), "true".equals(all));
                nodes.add(map);
            }
        } else {
            Map map = new HashMap();
            map.put("text", "无分类");
            map.put("id", "0");
            map.put("leaf", Boolean.valueOf(true));
            map.put("icon", "images/menuPanel/tag_blue3.gif");
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }
}
