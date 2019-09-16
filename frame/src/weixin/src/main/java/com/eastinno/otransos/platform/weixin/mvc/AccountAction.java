package com.eastinno.otransos.platform.weixin.mvc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
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
 * @intro 微信平台action
 * @verion 1.0
 * @author nsz
 * @since 2014年5月7日 上午10:07:46
 */
@Action
public class AccountAction extends AbstractPageCmdAction {
    @Inject
    private IAccountService service;

    /**
     * 查询列表
     * 
     * @param form
     * @return
     */
    public Page doList(WebForm form) {
        QueryObject qo = new QueryObject();
        form.toPo(qo);
        String sUserId = CommUtil.null2String(form.get("sUserId"));
        if (!"".equals(sUserId)) {
            qo.addQuery("obj.sUser.id", Long.parseLong(sUserId), "=");
        }
        Tenant tenant = TenantContext.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        IPageList pl = service.getAccountBy(qo);
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
        Account account = form.toPo(Account.class);
        Tenant tenant = TenantContext.getTenant();
        if (tenant == null) {
            this.addError("msg", "无法获取到当前的租户！！！");
        } else {
            account.setTenant(tenant);
        }
        String path = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + account.getTenant().getCode() + "/"
                + account.getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        String qrcodeImg = FileUtil.uploadFile(form, "qrcodeImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + account.getTenant().getCode() + "/"
                + account.getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(path)) {
            account.setImgPath(path);
        }
        if(!"".equals(qrcodeImg)){
        	account.setQrcodeImg(qrcodeImg);
        }
        if (!hasErrors()) {
            Long ret = this.service.addAccount(account);
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
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        Account account = service.getAccount(id);
        String oldPath = account.getImgPath();
        form.toPo(account, true);
        String path = FileUtil.uploadFile(form, "imgPath", UploadFileConstant.FILE_UPLOAD_PATH + "/" + account.getTenant().getCode() + "/"
                + account.getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if (!"".equals(path)) {
            account.setImgPath(path);
            deleFile(oldPath);
        }
        String qrcodeImgOld = account.getQrcodeImg();
        String qrcodeImg = FileUtil.uploadFile(form, "qrcodeImg", UploadFileConstant.FILE_UPLOAD_PATH + "/" + account.getTenant().getCode() + "/"
                + account.getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE, "jpg;png");
        if(!"".equals(qrcodeImg)){
        	account.setQrcodeImg(qrcodeImg);
        	deleFile(qrcodeImgOld);
        }
        if (!hasErrors()) {
            boolean ret = service.updateAccount(id, account);
            if (ret) {
                form.addResult("msg", "修改成功");
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
        Account account = this.service.getAccount(id);
        String path = account.getImgPath();
        deleFile(path);
        this.service.delAccount(id);
        return pageForExtForm(form);
    }

    /**
     * 获得用户树
     * 
     * @param form
     * @return
     */
    public Page doGetTree(WebForm form) {
        QueryObject qo = new QueryObject();
        qo.setPageSize(-1);
        Tenant tenant = TenantContext.getTenant();
        if (tenant != null) {
            qo.addQuery("obj.tenant", tenant, "=");
        }
        List<?> list = this.service.getAccountBy(qo).getResult();
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Account object = (Account) list.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", object.getId() + "");
                map.put("text", object.getName());
                map.put("leaf", true);
                nodes.add(map);
            }
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", "0");
            map.put("text", "无微信帐号");
            map.put("leaf", true);
            nodes.add(map);
        }
        form.jsonResult(nodes);
        return Page.JSONPage;
    }

    public void setService(IAccountService service) {
        this.service = service;
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