package com.eastinno.otransos.core.mvc.ajax;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.domain.SystemDictionary;
import com.eastinno.otransos.core.domain.SystemDictionaryDetail;
import com.eastinno.otransos.core.service.ISystemDictionaryDetailService;
import com.eastinno.otransos.core.service.ISystemDictionaryService;
import com.eastinno.otransos.core.service.SingleTransactionTask;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class SystemDictionaryDetailAction extends AbstractPageCmdAction {
    private ISystemDictionaryDetailService service;
    private ISystemDictionaryService dicservice;

    public void setService(ISystemDictionaryDetailService service) {
        this.service = service;
    }

    public void setDicservice(ISystemDictionaryService service) {
        this.dicservice = service;
    }

    public Page doList(WebForm form, Module module) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String pid = CommUtil.null2String(form.get("parentId"));
        String sn = CommUtil.null2String(form.get("sn"));
        String searchKey = CommUtil.null2String(form.get("searchKey"));
        SystemDictionary dictionary = null;
        if (!"".equals(pid)) {
            dictionary = this.dicservice.getSystemDictionary(new Long(pid));
        } else if (!"".equals(sn)) {
            dictionary = this.dicservice.getBySn(sn);
            form.addResult("parent", dictionary);
            qo.addQuery("obj.parent", dictionary, "=");
        }
        if (dictionary != null) {
            form.addResult("parent", dictionary);
            qo.addQuery("obj.parent", dictionary, "=");
            if ((qo.getOrderBy() == null) || ("".equals(qo.getOrderBy()))) {
                qo.setOrderBy("sequence");
                qo.setOrderType("asc");
            }
            if (StringUtils.hasLength(searchKey)) {
                qo.addQuery("(obj.tvalue=? or obj.title=?)", new Object[] {searchKey, searchKey});
            }
            IPageList pageList = this.service.getSystemDictionaryDetailBy(qo);
            AjaxUtil.convertEntityToJson(pageList);
            form.jsonResult(pageList);
        }
        return Page.JSONPage;
    }

    public Page doUpdate(WebForm form, Module module) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SystemDictionaryDetail systemDictionaryDetail = this.service.getSystemDictionaryDetail(id);
        form.toPo(systemDictionaryDetail, true, false);
        String sn = systemDictionaryDetail.getParent().getSn();
        List<SystemDictionaryDetail> ls = this.service.getDetailsByDictionarySn(sn);
        for (SystemDictionaryDetail s : ls) {
            if ((s.getTvalue().equals(systemDictionaryDetail.getTvalue())) && (!s.getId().equals(systemDictionaryDetail.getId()))) {
                addError("tvalue", I18n.get("valueExist"));
            }
        }
        if (!hasErrors()) {
            this.service.updateSystemDictionaryDetail(systemDictionaryDetail);
        }
        return pageForExtForm(form);
    }

    public Page doRemove(WebForm form, Module module) {
        String id = CommUtil.null2String(form.get("id"));
        boolean ret = this.service.delSystemDictionaryDetail(new Long(id));
        if (ret) {
            form.addResult("msg", "操作成功");
        }
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form, Module module) {
        SystemDictionaryDetail cmd = new SystemDictionaryDetail();
        form.toPo(cmd, true, true);
        if (!hasErrors()) {
            final SystemDictionary a;
            if (cmd.getParent() == null) {
                String sn = CommUtil.null2String(form.get("parentSn"));
                if (!"".equals(sn)) {
                    SystemDictionary parent = this.dicservice.getBySn(sn);
                    if (parent == null) {
                        parent = new SystemDictionary();
                        parent.setSn(sn);
                        parent.setTitle(sn);
                        a = parent;
                        new SingleTransactionTask() {
                            public void execute() {
                                SystemDictionaryDetailAction.this.dicservice.addSystemDictionary(a);
                            }
                        }.run();
                    }
                    cmd.setParent(parent);
                }
            }
            if (cmd.getParent() == null) {
                addError("msg", "字典编码不能为空！");
            }
            List<SystemDictionaryDetail> ls = new ArrayList(cmd.getParent().getChildren());
            for (SystemDictionaryDetail s : ls) {
                if (s.getTvalue().equals(cmd.getTvalue())) {
                    addError("tvalue", I18n.get("valueExist"));
                }
            }
            if (!hasErrors()) {
                this.service.addSystemDictionaryDetail(cmd);
            }
        }
        return pageForExtForm(form);
    }

    public Page doSwapSequence(WebForm form, Module module) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String pid = CommUtil.null2String(form.get("parentId"));
        if (!"".equals(pid)) {
            SystemDictionary dictionary = this.dicservice.getSystemDictionary(new Long(pid));
            form.addResult("parent", dictionary);
            qo.addQuery("parent.id", new Long(pid), "=");
        }
        qo.setOrderBy("sequence");
        qo.setOrderType("asc");
        List list = this.service.getSystemDictionaryDetailBy(qo).getResult();
        int sq = CommUtil.null2Int(form.get("sq"));
        String op = CommUtil.null2String(form.get("down"));
        if ((!"true".equals(op)) && (sq < 2)) {
            addError("msg", "已经移到第一条,不能再往上移！");
        } else if (("true".equals(op)) && (sq >= list.size())) {
            addError("msg", "已经到达最末一条,不能再往下移！");
        } else {
            SystemDictionaryDetail obj1 = (SystemDictionaryDetail) list.get(sq - 1);
            Integer sequence = obj1.getSequence();
            SystemDictionaryDetail obj2 = (SystemDictionaryDetail) list.get("true".equals(op) ? sq : sq - 2);
            obj1.setSequence(obj2.getSequence());
            obj2.setSequence(sequence);
            this.service.updateSystemDictionaryDetail(obj1);
            this.service.updateSystemDictionaryDetail(obj2);
        }
        return pageForExtForm(form);
    }
}
