package com.eastinno.otransos.platform.weixin.mvc;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.platform.weixin.domain.Menu;
import com.eastinno.otransos.platform.weixin.domain.NewsItem;
import com.eastinno.otransos.platform.weixin.domain.Template;
import com.eastinno.otransos.platform.weixin.service.INewsItemService;
import com.eastinno.otransos.platform.weixin.util.RequestWxUtils;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

public class NewsItemAction extends AbstractPageCmdAction {
    @Inject
    private INewsItemService service;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        String tplId = CommUtil.null2String(form.get("tplId"));
        if (!"".equals(tplId)) {
            qo.addQuery("obj.tpl.id", Long.parseLong(tplId), "=");
        } else {
            qo.addQuery("obj.tpl is EMPTY");
        }
        Tenant tenant = TenantContext.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        qo.setOrderBy("sequence");
        IPageList pl = service.getNewsItemBy(qo);
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
        NewsItem newsItem = form.toPo(NewsItem.class);
        Tenant tenant = TenantContext.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
            newsItem.setTenant(tenant);
        }
        String path = FileUtil.uploadFile(form, "imagePath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + newsItem.getTenant().getCode()
                + "/" + newsItem.getTpl().getAccount().getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(path)) {
            newsItem.setImagePath(path);
            Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(newsItem.getTpl().getAccount(), "image", newsItem.getTenant()
                    .getUrl() + path);
            newsItem.setMediaId(mapreturn.get("media_id") + "");
            newsItem.setMediaCreateAt(new Date().getTime());
        }
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.sequence", Integer.parseInt("0"), ">");
        qo.addQuery("obj.tpl", newsItem.getTpl(), "=");
        qo.setPageSize(1);
        qo.setOrderBy("sequence desc");
        List<NewsItem> list = this.service.getNewsItemBy(qo).getResult();
        if (list != null && list.size() > 0) {
            newsItem.setSequence(list.get(0).getSequence() + 1);
        }
        if (!hasErrors()) {
            Long ret = this.service.addNewsItem(newsItem);
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
        String imgpath = CommUtil.null2String(form.get("imagePath"));
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        NewsItem newsItem = service.getNewsItem(id);
        String oldimgpath = newsItem.getImagePath();
        form.toPo(newsItem, true);
        boolean isupdateImg = false;
        if (!imgpath.equals(oldimgpath)) {
            String path = FileUtil.uploadFile(form, "imagePath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + newsItem.getTenant().getCode()
                    + "/" + newsItem.getTpl().getAccount().getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
            if (!"".equals(path)) {
                newsItem.setImagePath(path);
                isupdateImg = true;
                Map<String, Object> mapreturn = RequestWxUtils.uploadMedia(newsItem.getTpl().getAccount(), "image", newsItem.getTenant()
                        .getUrl() + path);
                newsItem.setMediaId(mapreturn.get("media_id") + "");
                newsItem.setMediaCreateAt(new Date().getTime());
            }
        }
        if (!hasErrors()) {
            service.updateNewsItem(id, newsItem);
            if (isupdateImg) {
                deleFile(oldimgpath);
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
        this.service.delNewsItem(id);
        return pageForExtForm(form);
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
        NewsItem curObject = this.service.getNewsItem(Long.parseLong(id));
        int curse = curObject.getSequence();
        qo.addQuery("obj.tpl", curObject.getTpl(), "=");
        if (!"".equals(down)) {
            qo.addQuery("obj.sequence", curObject.getSequence(), ">");
            qo.setOrderBy("sequence");
        } else {
            qo.addQuery("obj.sequence", curObject.getSequence(), "<");
            qo.setOrderBy("sequence desc");
        }
        qo.setPageSize(1);
        List<NewsItem> list = this.service.getNewsItemBy(qo).getResult();
        if (list != null && list.size() > 0) {
            NewsItem otherObject = list.get(0);
            curObject.setSequence(otherObject.getSequence());
            otherObject.setSequence(curse);
            this.service.updateNewsItem(curObject.getId(), curObject);
            this.service.updateNewsItem(otherObject.getId(), otherObject);
        }
        return pageForExtForm(form);
    }

    public void setService(INewsItemService service) {
        this.service = service;
    }

    public Page doGetContent(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        NewsItem newsItem = this.service.getNewsItem(id);
        form.jsonResult(newsItem.getContent());
        return Page.JSONPage;
    }

    private void deleFile(String... fileNames) {
        for (String filename : fileNames) {
            File file = new File(Globals.APP_BASE_DIR + filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
