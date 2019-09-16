package com.eastinno.otransos.platform.weixin.mvc;

import java.io.File;
import java.util.List;

import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.platform.weixin.domain.PrizePro;
import com.eastinno.otransos.platform.weixin.service.IPrizeProService;
import com.eastinno.otransos.security.TenantContext;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * 奖品管理action
 * 
 * @author nsz
 */
public class PrizeProAction extends AbstractPageCmdAction {
    @Inject
    private IPrizeProService prizeProService;

    public void setPrizeProService(IPrizeProService prizeProService) {
        this.prizeProService = prizeProService;
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(QueryObject.class);
        qo.setOrderBy("sequence");
        Tenant tenant = TenantContext.getTenant();
        String accountId = CommUtil.null2String(form.get("accountId"));
        if (!"".equals(accountId)) {
            qo.addQuery("obj.account.id", Long.parseLong(accountId), "=");
        }
        qo.addQuery("obj.tenant", tenant, "=");
        IPageList pl = prizeProService.getPrizeProBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }

    public Page doSave(WebForm form) {
        PrizePro entity = form.toPo(PrizePro.class);
        Tenant tenant = TenantContext.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
            entity.setTenant(tenant);
        }
        String path = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + entity.getTenant().getCode() + "/"
                + entity.getAccount().getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(path)) {
            entity.setImgPath(path);
        }
        QueryObject qo = new QueryObject();
        qo.addQuery("obj.account", entity.getAccount(), "=");
        qo.setOrderBy("sequence desc");
        qo.setPageSize(1);
        List<PrizePro> list = this.prizeProService.getPrizeProBy(qo).getResult();
        if (list != null && list.size() > 0) {
            entity.setSequence(list.get(0).getSequence() + 1);
        }
        if (!hasErrors()) {
            Long ret = this.prizeProService.addPrizePro(entity);
            if (ret != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        PrizePro entity = this.prizeProService.getPrizePro(id);
        String pathOld = entity.getImgPath();
        form.toPo(entity, true);
        String path = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + entity.getTenant().getCode() + "/"
                + entity.getAccount().getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(path)) {
            entity.setImgPath(path);
            deleFile(pathOld);
        }
        if (!hasErrors()) {
            boolean ret = this.prizeProService.updatePrizePro(id, entity);
            if (ret) {
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.prizeProService.delPrizePro(id);
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
        PrizePro curObject = this.prizeProService.getPrizePro(Long.parseLong(id));
        int curse = curObject.getSequence();
        qo.addQuery("obj.account", curObject.getAccount(), "=");
        if (!"".equals(down)) {
            qo.addQuery("obj.sequence", curObject.getSequence(), ">");
            qo.setOrderBy("sequence");
        } else {
            qo.addQuery("obj.sequence", curObject.getSequence(), "<");
            qo.setOrderBy("sequence desc");
        }
        qo.setPageSize(1);
        List<PrizePro> list = this.prizeProService.getPrizeProBy(qo).getResult();
        if (list != null && list.size() > 0) {
            PrizePro otherObject = list.get(0);
            curObject.setSequence(otherObject.getSequence());
            otherObject.setSequence(curse);
            this.prizeProService.updatePrizePro(curObject.getId(), curObject);
            this.prizeProService.updatePrizePro(otherObject.getId(), otherObject);
        }
        return pageForExtForm(form);
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
