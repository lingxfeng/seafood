package com.eastinno.otransos.cms.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * PPtTypeAction
 * 
 * @author ksmwly@gmail.com
 */
@Action
public class LinkImgTypeAction extends SaaSCMSBaseAction {
    @Inject
    private ILinkImgTypeService service;

    @SuppressWarnings("unchecked")
    public Page doGetPPt(WebForm form) {
        IQueryObject queryObj = new QueryObject();
        queryObj.setPageSize(-1);
        IPageList pageList = service.getPPtTypeBy(queryObj);
        if (pageList != null) {
            List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
            List<LinkImgType> ppttypes = pageList.getResult();
            if (ppttypes != null)
                for (LinkImgType sdd : ppttypes) {
                    Map<String, Object> map = sdd2treemap(sdd, form);
                    nodes.add(map);
                }
            form.jsonResult(nodes);
        }
        return Page.JSONPage;
    }

    public Page doList(WebForm form) {
        IQueryObject qo = form.toPo(QueryObject.class);
        IPageList pl = service.getPPtTypeBy(qo);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    private Map<String, Object> sdd2treemap(LinkImgType sdd, WebForm form) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", sdd.getId() + "");
        map.put("text", sdd.getTitle());
        map.put("leaf", true);
        return map;
    }

    public Page doSave(WebForm form) {
        LinkImgType ppt = form.toPo(LinkImgType.class);
        if (!hasErrors()) {
            service.addPPtType(ppt);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        LinkImgType entity = service.getPPtType(Long.valueOf(id));
        form.toPo(entity);
        if (!this.hasErrors()) {
            service.updatePPtType(Long.valueOf(id), entity);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doRemove(WebForm form) {
        String mulitId = CommUtil.null2String((form.get("mulitId")));
        if (StringUtils.hasText(mulitId)) {
            String[] ids = mulitId.split(",");
            for (String id : ids) {
                service.delPPtType(Long.parseLong(id));
            }
        } else {
            Long id = Long.valueOf((String) form.get("id"));
            service.delPPtType(id);
        }
        return pageForExtForm(form);
    }

    /**
     * 获取栏目树信息
     * 
     * @param form
     * @return
     */
    public Page doGetPPTTypeTree(WebForm form) {
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);
        IPageList pageList = this.service.getPPtTypeBy(qo);
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                LinkImgType pPtType = (LinkImgType) pageList.getResult().get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", pPtType.getId() + "");
                map.put("text", pPtType.getTitle());
                map.put("leaf", true);
                nodes.add(map);
            }
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    public ILinkImgTypeService getService() {
        return service;
    }

    public void setService(ILinkImgTypeService service) {
        this.service = service;
    }

}