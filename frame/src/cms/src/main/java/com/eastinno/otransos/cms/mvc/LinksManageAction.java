package com.eastinno.otransos.cms.mvc;

import java.io.File;
import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.LinksManage;
import com.eastinno.otransos.cms.service.ILinksManageService;
import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.tools.IPageList;

@Action
public class LinksManageAction extends SaaSCMSBaseAction {
    @Inject
    private ILinksManageService service;

    /**
     * 查询友情链接列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        qo.setOrderBy("sequence");
        IPageList pl = service.getLinksManageBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    /**
     * 显示友情链接
     */
    public Page doShow(WebForm form) {
        form.addResult("friendLink", service.getShowLinksManage());
        return new Page("common/friendLink.shtml");
    }

    /**
     * 添加友情链接
     * 
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        LinksManage linksManage = new LinksManage();
        form.toPo(linksManage, true, false);
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.sequence", new Integer(0), ">");
        qo.setOrderBy("sequence");
        qo.setOrderType("desc");
        IPageList pl = service.getLinksManageBy(qo);
        List<?> linksManages = pl.getResult();
        if (linksManages != null && linksManages.size() > 0) {
            linksManage.setSequence(((LinksManage) linksManages.get(0)).getSequence() + 1);
        }
        String logoImgPath = FileUtil.uploadFile(form, "logoImg", UploadFileConstant.FILE_UPLOAD_PATH + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(logoImgPath)) {
            linksManage.setLogoImg(logoImgPath);
        }
        if (!hasErrors()) {
            Long ret = this.service.addLinksManage(linksManage);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 修改友情链接
     * 
     * @param form
     * @return
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        LinksManage linksManage = service.getLinksManage(id);
        String logoImgPathOld = linksManage.getLogoImg();
        form.toPo(linksManage, true);
        String logoImgPath = FileUtil.uploadFile(form, "logoImg", UploadFileConstant.FILE_UPLOAD_PATH + "/"
                + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(logoImgPath)) {
            linksManage.setLogoImg(logoImgPath);
        }
        if (!hasErrors()) {
            boolean ret = service.updateLinksManage(id, linksManage);
            if (ret) {
                if (StringUtils.hasText(logoImgPath)) {
                    deleFile(logoImgPathOld);
                }
                form.addResult("msg", "修改成功");
            } else {
                deleFile(logoImgPath);
            }
        } else {
            deleFile(logoImgPath);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 删除友情链接
     * 
     * @param form
     * @return
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        LinksManage linksManage = service.getLinksManage(id);
        String logoImgPath = linksManage.getLogoImg();
        this.service.delLinksManage(id);
        deleFile(logoImgPath);
        return pageForExtForm(form);
    }

    /**
     * 上移下移
     * 
     * @param form
     * @return
     */
    public Page doSwapSequence(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setPageSize(-1);
        qo.setOrderType("asc");
        qo.setOrderBy("sequence");
        List<?> list = this.service.getLinksManageBy(qo).getResult();
        int sq = CommUtil.null2Int(form.get("sq"));
        String down = CommUtil.null2String(form.get("down"));
        if ((sq - 1 < 1 && !StringUtils.hasText(down)) || (("true".equals(down)) && (sq >= list.size()))) {
            addError("msg", "未满足移动排序规则");
        } else {
            LinksManage currently = (LinksManage) list.get(sq - 1);
            Integer sequence = currently.getSequence();
            LinksManage other = (LinksManage) list.get("true".equals(down) ? sq : sq - 2);
            currently.setSequence(other.getSequence());
            other.setSequence(sequence);
            this.service.updateLinksManage(currently.getId(), currently);
            this.service.updateLinksManage(other.getId(), other);
        }
        return pageForExtForm(form);
    }

    public void setService(ILinksManageService service) {
        this.service = service;
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