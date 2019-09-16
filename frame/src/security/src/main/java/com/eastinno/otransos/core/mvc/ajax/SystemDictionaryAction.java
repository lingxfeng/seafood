package com.eastinno.otransos.core.mvc.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.domain.SystemDictionary;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.service.ISystemDictionaryService;
import com.eastinno.otransos.core.support.query.BaseQueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.web.tools.ListQuery;
import com.eastinno.otransos.web.tools.PageList;

@Action
public class SystemDictionaryAction extends AbstractPageCmdAction {
    private ISystemDictionaryService service;

    public void setService(ISystemDictionaryService service) {
        this.service = service;
    }

    public Page doList(WebForm form, Module module) {
        BaseQueryObject qo = new BaseQueryObject();
        form.toPo(qo);
        qo.setAll(true);
        String queryTitle = CommUtil.null2String(form.get("queryTitle"));
        if (!"".equals(queryTitle)) {
            qo.addQuery("obj.title", "%" + queryTitle + "%", "like");
        }
        qo.setPageSize(Integer.valueOf(-1));
        IPageList pageList = this.service.getSystemDictionaryBy(qo);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form, Module module) {
        String id = CommUtil.null2String(form.get("id"));
        this.service.delSystemDictionary(new Long(id));
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form, Module module) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SystemDictionary systemDictionary = this.service.getSystemDictionary(id);
        form.toPo(systemDictionary, true, false);
        SystemDictionary s = this.service.getBySn(systemDictionary.getSn());
        if ((s != null) && (!s.getId().equals(systemDictionary.getId()))) {
            addError("sn", "该标识已存在");
        }

        if (!hasErrors()) {
            this.service.updateSystemDictionary(systemDictionary);
        }
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form, Module module) {
        SystemDictionary cmd = new SystemDictionary();
        form.toPo(cmd, true, false);
        SystemDictionary s = this.service.getBySn(cmd.getSn());
        if (s != null) {
            addError("sn", "该标识已存在");
        }
        if (!hasErrors()) {
            this.service.addSystemDictionary(cmd);
        }
        return pageForExtForm(form);
    }

    public Page doGetDictionaryBySn(WebForm form) {
        String sn = CommUtil.null2String(form.get("sn"));
        SystemDictionary sd = this.service.getBySn(sn);
        List list = new ArrayList();
        if (sd != null) {
            for (SystemDictionaryDetail detail : sd.getChildren()) {
                Map map = new HashMap();
                map.put("id", detail.getId());
                map.put("title", detail.getTitle());
                map.put("tvalue", detail.getTvalue());
                list.add(map);
            }
        }
        IPageList pageList = new PageList(new ListQuery(list));
        pageList.doList(-1, 1, "", "");
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
}
