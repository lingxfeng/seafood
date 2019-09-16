package com.eastinno.otransos.cms.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.cms.domain.TemplateDir;
import com.eastinno.otransos.cms.service.ITemplateDirService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * TemplateDirAction
 * 
 * @author nsz
 */
@Action
public class TemplateDirAction extends SaaSCMSBaseAction {
    @Inject
    private ITemplateDirService service;

    /**
     * 查询模版栏目分类列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        qo.setOrderBy("sequence");
        String parentId = CommUtil.null2String(form.get("parentId"));
        TemplateDir parent = null;
        if (!"".equals(parentId)) {
            parent = service.getTemplateDir(Long.parseLong(parentId));
        }
        qo.addQuery("obj.parent", parent, "=");
        IPageList pl = service.getTemplateDirBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    /**
     * 添加模版栏目分类
     * 
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        TemplateDir templateDir = new TemplateDir();
        form.toPo(templateDir, true, false);
        if (!hasErrors()) {
            TemplateDir parent = templateDir.getParent();
            QueryObject qo = new QueryObject();
            if (parent != null) {
                qo.addQuery("obj.parent", parent, "=");
            } else {
                qo.addQuery("obj.parent is EMPTY");
            }
            qo.addQuery("obj.sequence", new Integer(0), ">");
            qo.setOrderBy("sequence");
            qo.setOrderType("desc");
            IPageList pl = service.getTemplateDirBy(qo);
            List<?> templateDirs = pl.getResult();
            if (templateDirs != null && templateDirs.size() > 0) {
                templateDir.setSequence(((TemplateDir) templateDirs.get(0)).getSequence() + 1);

            }
            Long ret = this.service.addTemplateDir(templateDir);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return pageForExtForm(form);
    }

    /**
     * 修改模版栏目分类
     * 
     * @param form
     * @return
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        TemplateDir templateDir = service.getTemplateDir(id);
        form.toPo(templateDir, true);
        boolean ret = service.updateTemplateDir(id, templateDir);
        if (!hasErrors()) {
            if (ret) {
                form.addResult("msg", "修改成功");
            }
        }
        return pageForExtForm(form);
    }

    /**
     * 删除模版栏目分类
     * 
     * @param form
     * @return
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delTemplateDir(id);
        return pageForExtForm(form);
    }

    /**
     * 获取模版管理树
     * 
     * @param form
     * @return
     */
    public Page getTemplateDirTree(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        QueryObject qo = new QueryObject();
        if (!"".equals(id)) {
            TemplateDir parent = service.getTemplateDir(Long.parseLong(id));
            qo.addQuery("obj.parent", parent, "=");
        } else {
            qo.addQuery("obj.parent is EMPTY", null);
        }
        qo.setPageSize(-1);
        qo.setOrderBy("sequence");
        IPageList pageList = this.service.getTemplateDirBy(qo);
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                TemplateDir templateDir = (TemplateDir) pageList.getResult().get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", templateDir.getId() + "");
                map.put("text", templateDir.getTitle());
                map.put("leaf", templateDir.getChildrens().size() < 1);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "无模版栏目");
            map.put("id", "0");
            map.put("leaf", true);
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    /**
     * 上移下移
     * 
     * @param form
     * @return
     */
    public Page doSwapSequence(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        TemplateDir templateDirCur = service.getTemplateDir(id);
        TemplateDir parent = templateDirCur.getParent();
        if (templateDirCur.getSequence() != null) {
            int sequence = templateDirCur.getSequence();
            String down = CommUtil.null2String(form.get("down"));
            QueryObject qo = new QueryObject();
            qo.addQuery("obj.parent", parent, "=");
            if ("".equals(down)) {
                qo.addQuery("obj.sequence", sequence, "<");
                qo.setOrderBy("sequence");
                qo.setOrderType("desc");
            } else {
                qo.addQuery("obj.sequence", sequence, ">");
                qo.setOrderBy("sequence");
            }
            IPageList pl = service.getTemplateDirBy(qo);
            List<?> templateDirs = pl.getResult();
            if (templateDirs != null && templateDirs.size() > 0) {
                TemplateDir templateDirOrder = (TemplateDir) templateDirs.get(0);
                templateDirCur.setSequence(templateDirOrder.getSequence());
                templateDirOrder.setSequence(sequence);
                boolean ret = service.updateTemplateDir(id, templateDirCur);
                if (!hasErrors() && ret) {
                    service.updateTemplateDir(templateDirOrder.getId(), templateDirOrder);
                }
            }
        }
        return pageForExtForm(form);
    }

    public void setService(ITemplateDirService service) {
        this.service = service;
    }
}