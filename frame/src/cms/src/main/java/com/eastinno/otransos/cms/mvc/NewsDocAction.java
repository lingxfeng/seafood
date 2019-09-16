package com.eastinno.otransos.cms.mvc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.application.core.domain.Attachment;
import com.eastinno.otransos.application.core.service.IAttachmentService;
import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.cms.domain.NewsDoc;
import com.eastinno.otransos.cms.query.NewsDocQuery;
import com.eastinno.otransos.cms.service.INewsDocService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.service.IHtmlGeneratorService;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 文章
 */
@Action
public class NewsDocAction extends SaaSCMSBaseAction {
    @Inject
    private INewsDocService service;
    @Inject
    private IAttachmentService attachmentService;
    @Inject
    private IHtmlGeneratorService htmlGenerator;

    /**
     * 阅读次数+1
     * 
     * @param form
     */
    public Page doPlus(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        if (id != null && !id.equals("")) {
            NewsDoc news = service.getNewsDoc(Long.valueOf(id));
            if (news != null) {
                if (news.getReadtime() == null) {
                    news.setReadtime(0);
                }
                news.setReadtime(news.getReadtime() + 1);
                String text = "{\"readtime\":" + news.getReadtime() + "}";
                ActionContext.getContext().getResponse().setCharacterEncoding("UTF-8");
                try {
                    ActionContext.getContext().getResponse().getWriter().write(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // form.addResult("readTime", news.getReadtime());
            }
        }
        return Page.nullPage;
    }

    public Page doSave(WebForm form) {
        NewsDoc doc = form.toPo(NewsDoc.class);
        NewsDir newsDir = doc.getDir();
        QueryObject qo = new QueryObject();
        if (newsDir != null) {
            qo.addQuery("obj.dir", newsDir, "=");
        } else {
            qo.addQuery("obj.dir is EMPTY");
        }
        qo.setOrderBy("sequence");
        qo.setOrderType("desc");
        IPageList newsDocsL = service.getNewsDocBy(qo);
        List<?> newsDocs = newsDocsL.getResult();
        if (newsDocs != null && newsDocs.size() > 0) {
            doc.setSequence(((NewsDoc) newsDocs.get(0)).getSequence() + 1);
        }
        Map<String, FileItem> map = form.getFileElement();

        FileItem item = (FileItem) map.get("iconPath");
        if (item != null) {
            String fileName = item.getName();
            if (StringUtils.hasText(fileName)) {
                String path = this.parseUploadFileItem(form, item);
                doc.setIconPath(path);
            }
        }
        if (!hasErrors()) {
            service.addNewsDoc(doc);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 上移下移
     * 
     * @param form
     * @return
     */
    public Page doSwapSequence(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String id = CommUtil.null2String(form.get("id"));
        NewsDoc newsDocCur = service.getNewsDoc(Long.parseLong(id));
        int cursequence = newsDocCur.getSequence();
        String down = CommUtil.null2String(form.get("down"));
        NewsDir dir = newsDocCur.getDir();
        if (dir != null) {
            qo.addQuery("obj.dir", dir, "=");
        } else {
            qo.addQuery("obj.dir is EMPTY");
        }
        if(!"".equals(down)){
        	qo.setOrderBy("sequence desc");
        	qo.addQuery("obj.sequence",cursequence,"<");
        }else{
        	qo.setOrderBy("sequence");
        	qo.addQuery("obj.sequence",cursequence,">");
        }
        qo.setPageSize(1);
        List<?> list = this.service.getNewsDocBy(qo).getResult();
        if(list !=null && list.size()>0){
        	NewsDoc otherObj = (NewsDoc) list.get(0);
        	newsDocCur.setSequence(otherObj.getSequence());
        	this.service.updateNewsDoc(newsDocCur.getId(), newsDocCur);
        	otherObj.setSequence(cursequence);
        	this.service.updateNewsDoc(otherObj.getId(), otherObj);
        }
        
        return pageForExtForm(form);
    }

    public void setAttachmentService(IAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public Page doGetContent(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        NewsDoc newsdoc = this.service.getNewsDoc(id);
        form.jsonResult(newsdoc.getContent());
        return Page.JSONPage;
    }

    /**
     * 获取需文章排序的文章列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        NewsDocQuery qo = form.toPo(NewsDocQuery.class);
        qo.setOrderBy("sequence");
        qo.setOrderType("desc");
        IPageList pl = service.getNewsDocBy(qo);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    public Page doUpdate(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        NewsDoc entity = service.getNewsDoc(Long.valueOf(id));
        Map<String, FileItem> map = form.getFileElement();
        FileItem item = (FileItem) map.get("iconPath");
        entity = (NewsDoc) form.toPo(entity, false);
        if (item != null) {
            String fileName = item.getName();
            if (!StringUtils.hasText(fileName)) {
            } else {
                String path = this.parseUploadFileItem(form, item);
                entity.setIconPath(path);
            }
        }
        if (!this.hasErrors()) {
            service.updateNewsDoc(Long.valueOf(id), entity);
        }

        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 批量上线
     * 
     * @param form
     * @return
     */
    public Page doUp(WebForm form) {
        String mulitId = CommUtil.null2String((form.get("mulitId")));
        String[] ids = mulitId.split(",");
        int count = 0;
        for (String id : ids) {
            service.updateNewsDocStatus(Long.parseLong(id), 2);
            NewsDoc newsDoc = service.getNewsDoc(Long.parseLong(id));
            this.htmlGenerator.process(newsDoc);
            count++;
        }
        form.jsonResult("成功生成" + count + "条记录！");
        return pageForExtForm(form);
    }

    /**
     * 批量下线
     * 
     * @param form
     * @return
     */
    public Page doDown(WebForm form) {
        String mulitId = CommUtil.null2String((form.get("mulitId")));
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            service.updateNewsDocStatus(Long.parseLong(id), 1);
            NewsDoc newsDoc = service.getNewsDoc(Long.parseLong(id));
            File nDStaticf = new File(Globals.APP_BASE_DIR + newsDoc.getStaticUrl());
            if (nDStaticf.exists()) {
                nDStaticf.delete();
            }
        }
        return pageForExtForm(form);
    }

    public Page doRemove(WebForm form) {
        String mulitId = CommUtil.null2String((form.get("mulitId")));
        if (StringUtils.hasText(mulitId)) {
            String[] ids = mulitId.split(",");
            for (String id : ids) {
                service.delNewsDoc(Long.parseLong(id));
            }
        } else {
            Long id = Long.valueOf((String) form.get("id"));
            service.delNewsDoc(id);
        }
        return pageForExtForm(form);
    }

    public Page doDelete(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        service.delNewsDoc(Long.valueOf(id));
        NewsDoc newsDoc = service.getNewsDoc(Long.parseLong(id));
        /**
         * 删除静态文件
         */
        File nDStaticf = new File(Globals.APP_BASE_DIR + newsDoc.getStaticUrl());
        if (nDStaticf.exists()) {
            nDStaticf.delete();
        }
        form.jsonResult("ok");
        return Page.JSONPage;
    }

    public Page doGetArticle(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        NewsDoc nd = service.getNewsDoc(Long.valueOf(id));
        form.jsonResult(nd);
        return Page.JSONPage;
    }

    public NewsDoc doShow(WebForm form) {
        String id = CommUtil.null2String(form.get("id"));
        NewsDoc newsDoc = service.getNewsDoc(Long.valueOf(id));
        return newsDoc;
    }

    // ==非业务的=
    public INewsDocService getService() {
        return service;
    }

    public void setService(INewsDocService service) {
        this.service = service;
    }

    public void setHtmlGenerator(IHtmlGeneratorService htmlGenerator) {
        this.htmlGenerator = htmlGenerator;
    }

    // =========一下为FileUploadAction 抄来的
    private void addAttachment(WebForm form, String path, Integer fileType) {
        Map<String, FileItem> map = form.getFileElement();
        FileItem item = (FileItem) map.get("iconPath");
        Long fileSize = item.getSize();
        String fileName = item.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().replace(".", "");

        Attachment file = new Attachment();
        file.setPath(path);
        file.setFileName(path.substring(path.lastIndexOf("/") + 1));
        file.setCreateUser(UserContext.getUser());
        file.setExt(suffix);
        file.setLength(fileSize);
        file.setTypes(fileType);
        file.setOldName(fileName);
        this.attachmentService.addAttachment(file);
    }

    /**
     * 对上传文件进行一系列处理
     * 
     * @param form
     * @param item
     * @return
     */
    private String parseUploadFileItem(WebForm form, FileItem item) {
        Integer fileType = UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE_NM;// 1附件 2媒体文件 3FLASH 4图片
        // 图片
        String ext = FileUtil.IMAGE_FILE_EXT;
        String fieldName = "iconPath";
        ITenantObject co = null;
        if (UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject) {
            co = (ITenantObject) UserContext.getUser();
        }
        String path = FileUtil.uploadFile(form, fieldName, UploadFileConstant.FILE_UPLOAD_PATH + "/" + co.getTenant().getCode(), ext);
        if (!hasErrors() && StringUtils.hasText(path)) {
            // 保存上传文件相关信息到数据库
            this.addAttachment(form, path, fileType);
        }
        return path;
    }

    /**
     * 文章静态化处理
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
            qo.addQuery("obj.status", 2, "=");
            qo.setPageSize(-1);
            IPageList newsDocsL = service.getNewsDocBy(qo);
            List<?> newsDocs = newsDocsL.getResult();
            if (newsDocs != null) {
                for (Object newsDoc : newsDocs) {
                    this.htmlGenerator.process((NewsDoc) newsDoc);
                    count++;
                }
            }
        } else {
            String mulitId = CommUtil.null2String(form.get("mulitId"));
            String[] ids = mulitId.split(",");
            for (String id : ids) {
                NewsDoc newsDoc = service.getNewsDoc(Long.parseLong(id));
                if (newsDoc.getStatus() == 1) {
                    noMsgb.append("主题为：'" + newsDoc.getTitle() + "'的文章，状态为'待审核'，无法静态化!!!；<br/>");
                } else {
                    this.htmlGenerator.process(newsDoc);
                    count++;
                }

            }
        }
        form.jsonResult("成功生成" + count + "条记录！<br/>" + noMsgb.toString());
        return pageForExtForm(form);
    }
}