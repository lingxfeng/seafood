package com.eastinno.otransos.core.mvc.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.core.service.ExcelImport;
import com.eastinno.otransos.core.service.ExcelReport;
import com.eastinno.otransos.core.service.ISystemRegionService;
import com.eastinno.otransos.core.support.query.BaseQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.tools.IPageList;

@Action(view = "core", autoToken = true)
public class SystemRegionAction extends AbstractPageCmdAction {
    private ISystemRegionService service;

    public void setService(ISystemRegionService service) {
        this.service = service;
    }

    public Page doIndex(WebForm f, Module m) {
        return forward("list");
    }

    public Page doList(WebForm form, Module module) {
        String parentId = CommUtil.null2String(form.get("parentId"));
        BaseQueryObject ndqo = (BaseQueryObject) form.toPo(BaseQueryObject.class);
        if ("".equals(parentId))
            ndqo.addQuery("parent is null", null);
        else {
            ndqo.addQuery("parent.id", new Long(parentId), "=");
        }
        IPageList pageList = this.service.querySystemRegion(ndqo);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSPage;
    }

    public Page doRemove(WebForm form, Module module) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.removeSystemRegion(id);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form, Module module) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SystemRegion nd = this.service.getSystemRegion(id);
        form.toPo(nd, true, false);
        if (!hasErrors())
            this.service.updateSystemRegion(nd);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form, Module module) {
        SystemRegion cmd = (SystemRegion) form.toPo(SystemRegion.class);
        if (!hasErrors()) {
            this.service.addSystemRegion(cmd);
        }
        form.jsonResult(cmd.getId());
        return pageForExtForm(form);
    }

    public Page doGetSystemRegion(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        QueryObject query = new QueryObject();
        query.setPageSize(Integer.valueOf(-1));
        if (!"".equals(id)) {
            query.addQuery("obj.parent.id", new Long(id), "=");
        } else {
            query.addQuery("obj.parent is EMPTY", null);
        }
        IPageList pageList = this.service.querySystemRegion(query);
        String treeData = CommUtil.null2String(form.get("treeData"));
        if ("".equals(treeData)) {
            if (pageList.getRowCount() > 0) {
                for (int i = 0; i < pageList.getResult().size(); i++) {
                    SystemRegion sr = (SystemRegion) pageList.getResult().get(i);
                    pageList.getResult().set(i, new SystemRegion(sr.getId(), sr.getSn(), sr.getTitle()));
                }
            }
            form.jsonResult(pageList.getResult());
        } else {
            List<Node> nodes = new ArrayList<>();
            if (pageList.getRowCount() > 0) {
                for (int i = 0; i < pageList.getResult().size(); i++) {
                    SystemRegion sr = (SystemRegion) pageList.getResult().get(i);
                    nodes.add(new Node(sr));
                }
            } else {
                SystemRegion systemRegion = new SystemRegion();
                systemRegion.setTitle("无下级");
                systemRegion.setId(new Long(0L));
                nodes.add(new Node(systemRegion));
            }
            form.jsonResult(nodes);
        }
        return Page.JSPage;
    }

    public Page doImport(WebForm form) throws Exception {
        FileItem item = (FileItem) form.getFileElement().get("file");
        String[] fields = {"sn", "title", "englishName", "spell", "shortSpell", "parentSn", "lev", "sequence", "inputTime",
                "inputUserName", "intro", "status"};
        ExcelImport im = new ExcelImport(item.getInputStream(), new ExcelImport.ImportService() {
            public void doImport(Map obj) {
                String sn = (String) obj.get("sn");
                String parentSn = (String) obj.get("parentSn");
                SystemRegion a = SystemRegionAction.this.service.getSystemRegionBySn(sn);
                if (a == null) {
                    a = new SystemRegion();
                }
                FrameworkEngine.form2Obj(obj, a, false, true);
                if (parentSn != null) {
                    a.setParent(SystemRegionAction.this.service.getSystemRegionBySn(parentSn));
                }
                if (!SystemRegionAction.this.hasErrors())
                    if (a.getId() == null)
                        SystemRegionAction.this.service.addSystemRegion(a);
                    else
                        SystemRegionAction.this.service.updateSystemRegion(a);
            }
        }, fields);
        try {
            im.run();
        } catch (Exception e) {
            addError("msg", "数据进入出错，请检查所选择的文件格式是否正确！");
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doExport(WebForm form) throws Exception {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setPageSize(Integer.valueOf(-1));
        String[] lables = {"编号", "名称", "英文名", "拼音", "拼音简写", "父级编码", "级别", "显示顺序", "录入时间", "录入人", "简介", "状态"};
        String[] fields = {"sn", "title", "englishName", "spell", "shortSpell", "parent.sn", "lev", "sequence", "inputTime",
                "inputUser.name", "intro", "status"};
        IPageList pageList = this.service.querySystemRegion(qo);
        ExcelReport report = new ExcelReport("地区信息", lables, fields, pageList.getResult());
        report.export();
        return Page.nullPage;
    }

    public Page doSwapSequence(WebForm form, Module module) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String pid = CommUtil.null2String(form.get("parentId"));
        if (!"".equals(pid)) {
            qo.addQuery("obj.parent.id", new Long(pid), "=");
        }
        qo.setOrderBy("sequence");
        qo.setOrderType("asc");
        List list = this.service.querySystemRegion(qo).getResult();
        int sq = CommUtil.null2Int(form.get("sq"));
        String op = CommUtil.null2String(form.get("down"));
        if ((sq - 1 < 0) || (("true".equals(op)) && (sq >= list.size()))) {
            addError("msg", "参数不正确！");
        } else {
            SystemRegion obj1 = (SystemRegion) list.get(sq - 1);
            Integer sequence = obj1.getSequence();
            SystemRegion obj2 = (SystemRegion) list.get("true".equals(op) ? sq : sq - 2);
            obj1.setSequence(obj2.getSequence());
            obj2.setSequence(sequence);
            this.service.updateSystemRegion(obj1);
            this.service.updateSystemRegion(obj2);
        }
        return pageForExtForm(form);
    }

    public Page doGetSystemRegionByParentSn(WebForm form) {
        String parentSn = CommUtil.null2String(form.get("parentSn"));
        List<?> list = this.service.getGetSystemRegionByParentSn(parentSn);

        List<SystemRegion> nodes = new ArrayList<SystemRegion>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                SystemRegion sysRegion = new SystemRegion((Long) obj[0], obj[2].toString(), obj[1].toString());
                nodes.add(sysRegion);
            }
        }
        form.jsonResult(nodes);
        return Page.JSPage;
    }

    private class Node implements IJsonObject{
        private SystemRegion systemRegion;

        public Node(SystemRegion systemRegion) {
            this.systemRegion = systemRegion;
        }

		@Override
		public Object toJSonObject() {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", systemRegion.getId());
			map.put("sn", systemRegion.getSn());
			map.put("text", systemRegion.getTitle());
			map.put("leaf", systemRegion.getChildren().size()< 1);
			return map;
		}
    }
}
