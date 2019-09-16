package com.eastinno.otransos.cms.mvc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.SinglePageNews;
import com.eastinno.otransos.cms.query.SinglePageQuery;
import com.eastinno.otransos.cms.service.ISinglePageNewsService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.IHtmlGeneratorService;
import com.eastinno.otransos.core.support.ActionUtil;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * SinglePageNewsAction
 */
@Action
public class SinglePageNewsAction extends SaaSCMSBaseAction {
    @Inject
    private ISinglePageNewsService service;
    private IHtmlGeneratorService htmlGenerator;

    /*
     * set the current service return service
     */
    public void setService(ISinglePageNewsService service) {
        this.service = service;
    }

    public void setHtmlGenerator(IHtmlGeneratorService htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }

    public Page doGenerator(WebForm form, Module module) {
        List<Serializable> ids = ActionUtil.processIds(form);

        String parentId = CommUtil.null2String(form.get("parentId"));

        if (ids != null && ids.size() > 0) {
            for (int i = 0; i < ids.size(); i++) {
                SinglePageNews dsp = this.service.getSinglePageNews((Long) ids.get(i));
                this.htmlGenerator.process(dsp);
            }
            form.getTextElement().clear();
        }
        return go("singlePageNews.list&parentId=" + parentId);
    }

    /**
     * 交换顺序
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doSwapSequence(WebForm form, Module module) {
        SinglePageQuery ndqo = new SinglePageQuery();
        form.toPo(ndqo);
        ndqo.setDel(true);
        List list = service.getSinglePageNewsBy(ndqo).getResult();
        int sq = CommUtil.null2Int(form.get("sq"));
        String op = CommUtil.null2String(form.get("down"));
        if (sq - 2 < 0 || ("true".equals(op) && sq >= list.size())) {
            form.addResult("msg", "参数不正确！");
        } else {
            SinglePageNews dir1 = (SinglePageNews) list.get(sq - 1);
            Integer sequence = dir1.getSequence();
            SinglePageNews dir2 = (SinglePageNews) list.get("true".equals(op) ? sq : sq - 2);
            dir1.setSequence(dir2.getSequence());
            dir2.setSequence(sequence);
            this.service.updateSinglePageNews(dir1.getId(), dir1);
            this.service.updateSinglePageNews(dir2.getId(), dir2);
        }
        return forward("list");
    }

    public Page doIndex(WebForm f, Module m) {
        return forward("list");
    }

    public Page doList(WebForm form) {
        String title = CommUtil.null2String(form.get("title"));
        SinglePageQuery singlePageQuery = new SinglePageQuery();
        if (StringUtils.hasText(title)) {
            singlePageQuery.addQuery("obj.title", "%" + title + "%", "like");
        }
        form.toPo(singlePageQuery);
        IPageList pageList = this.service.getSinglePageNewsBy(singlePageQuery);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        service.delSinglePageNews(id);
        return pageForExtForm(form);
    }

    public Page doSave(WebForm form) {
        SinglePageNews object = form.toPo(SinglePageNews.class);
        if (!hasErrors())
            service.addSinglePageNews(object);
        return pageForExtForm(form);
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        SinglePageNews object = service.getSinglePageNews(id);
        form.toPo(object, true);
        if (!hasErrors())
            service.updateSinglePageNews(id, object);
        return pageForExtForm(form);
    }

    public Page doGetChildren(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        String code = CommUtil.null2String(form.get("code"));
        SinglePageQuery singlePageQuery = new SinglePageQuery();
        if (!id.equals("")) {
            singlePageQuery.addQuery("obj.parent.id", new Long(id), null);
        } else if (StringUtils.hasText(code)) {
            SinglePageNews parent = this.service.getSinglePageNewsByCode(code);
            if (parent != null) {
                singlePageQuery.addQuery("obj.parent", parent, null);
            } else {
                singlePageQuery.addQuery("obj.parent is EMPTY", null);
            }
        } else {
            singlePageQuery.addQuery("obj.parent is EMPTY", null);
        }

        IPageList pageList = service.getSinglePageNewsBy(singlePageQuery);
        String checked = CommUtil.null2String(form.get("checked"));
        String treeData = CommUtil.null2String(form.get("treeData"));

        if ("".equals(treeData)) {

        } else {
            List<Map> nodes = new java.util.ArrayList<Map>();
            if (pageList.getRowCount() > 0) {
                for (int i = 0; i < pageList.getResult().size(); i++) {
                    SinglePageNews singlePageNews = (SinglePageNews) pageList.getResult().get(i);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", singlePageNews.getId());
                    map.put("text", singlePageNews.getCode());
                    map.put("leaf", singlePageNews.getChildren().size() < 1);
                    if (!"".equals(checked)) {
                        map.put("checked", false);
                    }
                    map.put("icon", "images/menuPanel/tag_blue3.gif");
                    nodes.add(map);
                }
            } else {
                Map map = new HashMap();
                map.put("text", "无分类");
                map.put("id", 0);
                map.put("leaf", true);
                map.put("icon", "images/menuPanel/tag_blue3.gif");
                nodes.add(map);
            }
            form.addResult("json", AjaxUtil.getJSON(nodes));
        }

        return Page.JSONPage;
    }

    /**
     * 移动栏目
     * 
     * @param form
     * @param module
     * @return
     */
    public Page doMove(WebForm form, Module module) {
        String currentId = CommUtil.null2String(form.get("currentId"));
        String moveTo = CommUtil.null2String(form.get("moveTo"));
        boolean ret = this.service.moveDir(currentId, moveTo);
        if (ret) {
            form.addResult("msg", "移动成功!");
        } else {
            form.addResult("msg", "移动失败!");
        }
        return new Page("blank", "classpath:com/easyjf/core/views/blank.html");
    }

}