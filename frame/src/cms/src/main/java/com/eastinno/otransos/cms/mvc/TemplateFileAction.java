package com.eastinno.otransos.cms.mvc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.TemplateDir;
import com.eastinno.otransos.cms.domain.TemplateFile;
import com.eastinno.otransos.cms.service.ITemplateDirService;
import com.eastinno.otransos.cms.service.ITemplateFileService;
import com.eastinno.otransos.cms.utils.CmsConstant;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.service.ITenantObject;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * @intro 模版管理action
 * @verion 1.0
 * @author nsz
 * @since 2014年5月7日 上午10:07:46
 */
@Action
public class TemplateFileAction extends SaaSCMSBaseAction {
    @Inject
    private ITemplateFileService service;

    @Inject
    private ITemplateDirService templateDirservice;

    /**
     * 查询模版管理列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        String dirId = CommUtil.null2String(form.get("dirId"));
        TemplateDir templateDir = null;
        if (!"".equals(dirId)) {
            templateDir = templateDirservice.getTemplateDir(Long.parseLong(dirId));
        }
        qo.setOrderBy("sequence");
        qo.addQuery("obj.dir", templateDir, "=");
        IPageList pl = service.getTemplateFileBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    public String getDocPath(String code) {
        return CmsConstant.FILE_UPLOAD_PATH_TEM + "/" + code;
    }

    /**
     * 添加模版管理
     * 
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        Tenant tenant = null;
        if (UserContext.getUser() != null && UserContext.getUser() instanceof ITenantObject) {
            ITenantObject co = (ITenantObject) UserContext.getUser();
            tenant = co.getTenant();
        }
        QueryObject qo = new QueryObject();
        TemplateFile templateFile = form.toPo(TemplateFile.class);
        TemplateDir templateDir = templateFile.getDir();
        if (templateDir != null) {
            qo.addQuery("obj.dir", templateDir, "=");
        } else {
            qo.addQuery("obj.dir is EMPTY");
        }
        qo.addQuery("obj.sequence", new Integer(0), ">");
        qo.setOrderBy("sequence");
        qo.setOrderType("desc");
        IPageList pl = service.getTemplateFileBy(qo);
        List<?> templateFiles = pl.getResult();
        if (templateFiles != null && templateFiles.size() > 0) {
            templateFile.setSequence(((TemplateFile) templateFiles.get(0)).getSequence() + 1);
        }
        String path = FileUtil.uploadFile(form, "path", getDocPath(tenant.getCode()), "html;htm;shtml");
        // String path = FileUtil.uploadFile(form, "path", CmsConstant.FILE_UPLOAD_PATH_TEM, "html;htm;shtml");
        if (!hasErrors() && StringUtils.hasText(path)) {
            templateFile.setPath(path);
            Long ret = this.service.addTemplateFile(templateFile);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            } else {
                File oldPathFile = new File(Globals.APP_BASE_DIR + path);
                if (oldPathFile.exists()) {
                    oldPathFile.delete();
                }
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 修改模版管理
     * 
     * @param form
     * @return
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        TemplateFile templateFile = service.getTemplateFile(id);
        form.toPo(templateFile, true);
        TemplateDir templateDir = templateFile.getDir();
        templateFile.setDirPath(templateDir.getPath());
        String oldPath = templateFile.getPath();
        String path = FileUtil.uploadFile(form, "path", CmsConstant.FILE_UPLOAD_PATH_TEM, "html;htm;shtml");
        if (!"".equals(path)) {
            templateFile.setPath(path);
        }
        if (!hasErrors()) {
            boolean ret = service.updateTemplateFile(id, templateFile);
            if (ret) {
                if (StringUtils.hasText(path)) {
                    deleFile(oldPath);
                }
                form.addResult("msg", "修改成功");
            }
        } else {
            deleFile(path);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 删除模版管理
     * 
     * @param form
     * @return
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delTemplateFile(id);
        return pageForExtForm(form);
    }

    /**
     * 获取栏目列表
     * 
     * @param form
     * @return
     */
    public Page getTemplateFileTree(WebForm form) {
        String dir = CommUtil.null2String(form.get("dir"));
        QueryObject qo = new QueryObject();
        if (!"".equals(dir)) {
            QueryObject qodir = new QueryObject();
            qodir.setPageSize(-1);
            qodir.addQuery("obj.sn", dir, "=");
            IPageList iPageList = templateDirservice.getTemplateDirBy(qodir);
            List<?> templateDirs = iPageList.getResult();
            TemplateDir templateDir = null;
            if (templateDirs != null && templateDirs.size() > 0) {
                templateDir = (TemplateDir) templateDirs.get(0);
            }
            qo.addQuery("obj.dir", templateDir, "=");
        }
        qo.setPageSize(-1);
        qo.setOrderBy("sequence");
        IPageList pageList = this.service.getTemplateFileBy(qo);
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (pageList.getRowCount() > 0) {
            for (int i = 0; i < pageList.getResult().size(); i++) {
                TemplateFile templateFile = (TemplateFile) pageList.getResult().get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", templateFile.getId() + "");
                map.put("text", templateFile.getTitle());
                map.put("leaf", true);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", "无模版");
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
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        String id = CommUtil.null2String(form.get("id"));
        TemplateFile templateFileCur = service.getTemplateFile(Long.parseLong(id));
        TemplateDir dir = templateFileCur.getDir();
        if (dir != null) {
            qo.addQuery("obj.dir", dir, "=");
        } else {
            qo.addQuery("obj.dir is EMPTY");
        }
        qo.setPageSize(-1);
        qo.setOrderType("asc");
        qo.setOrderBy("sequence");
        List<?> list = this.service.getTemplateFileBy(qo).getResult();
        int sq = CommUtil.null2Int(form.get("sq"));
        String down = CommUtil.null2String(form.get("down"));
        if ((sq - 1 < 1 && !StringUtils.hasText(down)) || (("true".equals(down)) && (sq >= list.size()))) {
            addError("msg", "未满足移动排序规则");
        } else {
            TemplateFile other = (TemplateFile) list.get("true".equals(down) ? sq : sq - 2);
            Integer sq1 = templateFileCur.getSequence();
            Integer sq2 = other.getSequence();
            if (sq1 == sq2) {
                sq2 = "true".equals(down) ? (sq2 += 1) : (sq2 -= 1);
            }
            templateFileCur.setSequence(sq2);
            other.setSequence(sq1);
            this.service.updateTemplateFile(Long.parseLong(id), templateFileCur);
            this.service.updateTemplateFile(other.getId(), other);
        }
        return pageForExtForm(form);
    }

    public void setService(ITemplateFileService service) {
        this.service = service;
    }

    public void setTemplateDirservice(ITemplateDirService templateDirservice) {
        this.templateDirservice = templateDirservice;
    }

    /**
     * 删除文件
     * 
     * @param fileNames
     */
    private void deleFile(String... fileNames) {
        for (String filename : fileNames) {
            File file = new File(Globals.APP_BASE_DIR + filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}