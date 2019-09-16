package com.eastinno.otransos.cms.mvc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.service.ILinkImgTypeService;
import com.eastinno.otransos.cms.service.INewsDirService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.IHtmlGeneratorService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 栏目管理action
 * 
 * @author nsz
 */
@Action
public class NewsDirAction extends SaaSCMSBaseAction {
    @Inject
    private INewsDirService service;
    @Inject
    private ILinkImgTypeService bannerPPTService;
    @Inject
    private IHtmlGeneratorService htmlGenerator;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        String parentId = CommUtil.null2String(form.get("parentId"));// 父级栏目
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        if (StringUtils.hasText(parentId)) {
            qo.addQuery("obj.parent.id", new Long(parentId), "=");
        } else {
            qo.addQuery("obj.parent is EMPTY");
        }
        qo.addQuery("obj.sequence", new Integer(0), ">");
        qo.setOrderBy("sequence");
        IPageList pl = service.getNewsDirBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    /**
     * 新增
     * 
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        NewsDir newsDir = new NewsDir();
        form.toPo(newsDir, true, true);
        if (!hasErrors()) {
            NewsDir parent = newsDir.getParent();
            QueryObject qo = new QueryObject();
            if (parent != null) {
                qo.addQuery("obj.parent", parent, "=");
            } else {
                qo.addQuery("obj.parent is EMPTY");
            }
            qo.setOrderBy("sequence");
            qo.setOrderType("desc");
            IPageList pl = service.getNewsDirBy(qo);
            List<?> newsDirs = pl.getResult();
            if (newsDirs != null && newsDirs.size() > 0) {
                newsDir.setSequence(((NewsDir) newsDirs.get(0)).getSequence() + 1);
            }
            ITenantObject co = null;
            if (UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject) {
                co = (ITenantObject) UserContext.getUser();
            }
            String path = FileUtil.uploadFile(form, "bannerImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + co.getTenant().getCode() + "/"
                    + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
            if (!"".equals(path)) {
                newsDir.setBannerImg(path);
            }
            Long ret = this.service.addNewsDir(newsDir);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            } else {
                deleFile(path);
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 更新
     * 
     * @param form
     * @return
     */
    public Page doUpdate(WebForm form) {
        Long id = (Long) new BeanWrapperImpl().convertIfNecessary(form.get("id"), Long.class);
        String parentIdNew = CommUtil.null2String(form.get("parentId"));
        NewsDir obj = service.getNewsDir(id);
        String parentIdOld = obj.getParent() == null ? "" : obj.getParent().getId().toString();
        String pathOld = obj.getBannerImg();
        form.toPo(obj, false, true);
        if (!parentIdNew.equals(parentIdOld)) {
            QueryObject qo = new QueryObject();
            if (parentIdNew.equals("")) {
                qo.addQuery("obj.parent is EMPTY");
            } else {
                qo.addQuery("obj.parent.id", Long.parseLong(parentIdNew), "=");
            }
            qo.addQuery("obj.id", obj.getId(), "<>");
            qo.setOrderBy("sequence");
            qo.setOrderType("desc");
            IPageList pl = service.getNewsDirBy(qo);
            List<?> newsDirs = pl.getResult();
            if (newsDirs != null && newsDirs.size() > 0) {
                obj.setSequence(((NewsDir) newsDirs.get(0)).getSequence() + 1);
            }
        }
        ITenantObject co = null;
        if (UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject) {
            co = (ITenantObject) UserContext.getUser();
        }
        String path = FileUtil.uploadFile(form, "bannerImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + co.getTenant().getCode() + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(path)) {
            obj.setBannerImg(path);
        }
        if (!hasErrors()) {
            service.updateNewsDir(id, obj);
            if (StringUtils.hasText(path)) {
                deleFile(pathOld);
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
        NewsDir newsDir = service.getNewsDir(id);
        String pathb = newsDir.getBannerImg();
        this.service.delNewsDir(id);
        deleFile(pathb);
        return pageForExtForm(form);
    }

    /**
     * 获取栏目树信息
     * 
     * @param form
     * @return
     */
    public Page doGetNewDirTree(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        String all = CommUtil.null2String(form.get("all"));
        QueryObject qo = new QueryObject();
        if (!"".equals(id)) {
            NewsDir parent = service.getNewsDir(Long.parseLong(id));
            qo.addQuery("obj.parent", parent, "=");
        } else {
            qo.addQuery("obj.parent is EMPTY", null);
        }
        if (!"true".equals(all)) {
            qo.addQuery("obj.types in(0,1)");
        }
        qo.setPageSize(-1);
        qo.setOrderBy("sequence");
        IPageList pageList = this.service.getNewsDirBy(qo);
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                NewsDir category = (NewsDir) pageList.getResult().get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", category.getId() + "");
                map.put("text", category.getName());
                map.put("types", category.getTypes());
                map.put("dirPath", category.getDirPath());
                map.put("leaf", category.getChildren().size() < 1);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "无栏目");
            map.put("id", "0");
            map.put("types", 0);
            map.put("dirPath", "");
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
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String pid = CommUtil.null2String(form.get("parentId"));
        if (!StringUtils.hasText(pid)) {
            qo.addQuery("obj.parent is EMPTY");
        } else {
            qo.addQuery("obj.parent.id", new Long(pid), "=");
        }
        qo.setPageSize(-1);
        qo.setOrderType("asc");
        qo.setOrderBy("sequence");
        List<?> list = this.service.getNewsDirBy(qo).getResult();
        int sq = CommUtil.null2Int(form.get("sq"));
        String down = CommUtil.null2String(form.get("down"));
        if ((sq - 1 < 1 && !StringUtils.hasText(down)) || (("true".equals(down)) && (sq >= list.size()))) {
            addError("msg", "未满足移动排序规则");
        } else {
            NewsDir currently = (NewsDir) list.get(sq - 1);
            Integer sequence = currently.getSequence();
            NewsDir other = (NewsDir) list.get("true".equals(down) ? sq : sq - 2);
            currently.setSequence(other.getSequence());
            other.setSequence(sequence);
            this.service.updateNewsDir(currently.getId(), currently);
            this.service.updateNewsDir(other.getId(), other);
        }
        return pageForExtForm(form);
    }

    public void setService(INewsDirService service) {
        this.service = service;
    }

    public void setBannerPPTService(ILinkImgTypeService bannerPPTService) {
        this.bannerPPTService = bannerPPTService;
    }

    private void deleFile(String... fileNames) {
        for (String filename : fileNames) {
            File file = new File(Globals.APP_BASE_DIR + filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 页面静态化
     * 
     * @param form
     * @return
     */
    public Page doStatic(WebForm form) {
        String type = CommUtil.null2String(form.get("type"));
        int count = 0;
        StringBuffer noMsgb = new StringBuffer();
        if ("mulit".equals(type)) {
            QueryObject qo = new QueryObject();
            qo.addQuery("obj.status", 1, "=");
            qo.setPageSize(-1);
            IPageList newsDirsL = service.getNewsDirBy(qo);
            List<?> newsDirs = newsDirsL.getResult();
            for (Object newsDir : newsDirs) {
                this.htmlGenerator.process((NewsDir) newsDir);
                count++;
            }
        } else {
            String mulitId = CommUtil.null2String(form.get("mulitId"));
            String[] ids = mulitId.split(",");
            for (String id : ids) {
                NewsDir newsDir = service.getNewsDir(Long.parseLong(id));
                if (newsDir.getStatus().equals(1)) {
                    this.htmlGenerator.process(newsDir);
                    count++;
                } else {
                    noMsgb.append("栏目名称为：'" + newsDir.getName() + "'的栏目，状态为'停用'，无法静态化!!!；<br/>");
                }
            }
        }
        form.jsonResult("成功生成" + count + "条记录！<br/>" + noMsgb.toString());
        return pageForExtForm(form);
    }

    public void setHtmlGenerator(IHtmlGeneratorService htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }

}