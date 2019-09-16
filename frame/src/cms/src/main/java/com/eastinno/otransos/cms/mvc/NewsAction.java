package com.eastinno.otransos.cms.mvc;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.domain.SinglePageNews;
import com.eastinno.otransos.cms.query.NewsDirQuery;
import com.eastinno.otransos.cms.query.NewsDocQuery;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class NewsAction extends CMSBaseAction {

    @Inject
    private ITenantService service;

    public void setService(ITenantService service) {
        this.service = service;
    }

    public Page doForward(WebForm f) {
        String path = CommUtil.null2String(f.get("path"));
        return new Page("forward", "/" + TenantContext.getTenant().getCode() + "/news" + "/" + path + ".html", "template");
    }

    @Override
    public Page doInit(WebForm form, Module module) {
        String page = CommUtil.null2String(form.get("p"));// p参数用来加载美工手动加入的资讯类动态页面模板的名称
        String sCode = CommUtil.null2String(form.get("sCode"));// 根据单页新闻code获取单页新闻
        String sId = CommUtil.null2String(form.get("sId"));// 根据单页新闻ID获取单页新闻
        if (StringUtils.hasText(sId)) {
            SinglePageNews pageNews = this.getSinglePageNewsService().getSinglePageNews(Long.parseLong(sId));
            form.addPo(pageNews);
            return new Page(StringUtils.hasText(pageNews.getTpl().getPath()) ? pageNews.getTpl().getPath() : "");
        } else if (StringUtils.hasText(page)) {
            return new Page("singlePage_" + page, "/singlePage/" + page + (page.endsWith(".html") ? "" : ".html"));// 自动把.html加为扩展名
        } else if (StringUtils.hasText(sCode)) {
            SinglePageNews pageNews = getNewsUtil().singlePageNews(sCode);
            form.addPo(pageNews);
            return pageNews.getShowTemplate(pageNews);
        } else {
            return doIndex(form);
        }
    }

    public Page doShow(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        NewsDoc doc = getNewsDocService().getNewsDoc(Long.valueOf(id));
        if (doc == null) {
            return Page.nullPage;
        }
        form.addResult("news", doc);
        return doc.getDocTemplate();
    }

    public Page doDir(WebForm form) {
        NewsDir dir = (NewsDir) form.toPo(NewsDir.class);
        FrameworkEngine.getValidateManager().cleanErrors();
        QueryObject qodir = form.toPo(NewsDirQuery.class);
        IPageList plDir = getNewsDirService().getNewsDirBy(qodir);
        if (plDir.getRowCount() > 0) {
            dir = (NewsDir) plDir.getResult().get(0);
            form.addResult("dir", dir);
            NewsDocQuery ndq = form.toPo(NewsDocQuery.class);
            ndq.setDir(dir);
            ndq.addQuery("obj.status ", 2, "=");// 已发表的状态
            IPageList pl = getNewsDocService().getNewsDocBy(ndq);
            form.addResult("dirDocs", pl.getResult());
            CommUtil.saveIPageList2WebForm(pl, form);
        }
        return dir.getDirTemplate();
    }

    public Page doIndex(WebForm form) {
        NewsDirQuery qodir = new NewsDirQuery();
        Tenant t = TenantContext.getTenant();
        if (t == null) {
            qodir.setCode("index");
        } else {
            qodir.setCode(t.getCode() + "_index");
        }
        IPageList pldir = getNewsDirService().getNewsDirBy(qodir);
        form.addResult("dir", (NewsDir) pldir.getResult().get(0));
        return new Page("/" + TenantContext.getTenant().getCode() + "/news/" + "/index.html");
    }
}
