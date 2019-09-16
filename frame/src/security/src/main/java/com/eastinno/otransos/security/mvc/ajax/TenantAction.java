package com.eastinno.otransos.security.mvc.ajax;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.core.util.FileUtil;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.Tenant;
import com.eastinno.otransos.security.query.TenantQuery;
import com.eastinno.otransos.security.service.IRoleService;
import com.eastinno.otransos.security.service.ITenantService;
import com.eastinno.otransos.security.service.IUserService;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.util.UploadFileConstant;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;
import com.google.inject.internal.Maps;

/**
 * TenantAction
 */
@Action
public class TenantAction extends AbstractPageCmdAction {

    @Inject
    protected ITenantService service;

    @Inject
    protected IUserService userService;
    @Inject
    private IRoleService roleService;

    public void setService(ITenantService service) {
        this.service = service;
    }

    public void setuserService(IUserService userService) {
        this.userService = userService;
    }

    public Page doList(WebForm form) {
        QueryObject qo = form.toPo(TenantQuery.class);
        IPageList pageList = service.getTenantBy(qo);
        AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }

    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        Tenant c = service.getTenant(id);
        c.setStatus(-1);
        service.updateTenant(c.getId(), c);
        return pageForExtForm(form);
    }

    /**
     * @param form
     * @return
     */
    public Page doSave(WebForm form) {
        Tenant t = form.toPo(Tenant.class);
        String path = UploadFileConstant.FILE_UPLOAD_PATH + "/" + t.getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE;
        String logoUrl = FileUtil.uploadFile(form, "logo", path, "jpg;png");
        if (!"".equals(logoUrl)) {
            t.setLogo(logoUrl);
        }
        if (!hasErrors()) {
        	Tenant parent = ShiroUtils.getTenant();
        	t.setParent(parent);
            this.service.addTenant(t);
            this.userService.addUserByTenant(t);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public Page doUpdate(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        Tenant t = service.getTenant(id);
        String oldUrl = t.getLogo();
        String path = UploadFileConstant.FILE_UPLOAD_PATH + "/" + t.getCode() + "/" + UploadFileConstant.FILE_UPLOAD_TYPE_IMAGE;
        String logoUrl = FileUtil.uploadFile(form, "logo", path, "jpg;png");
        if (!"".equals(logoUrl)) {
            t.setLogo(logoUrl);
            deleFile(oldUrl);
        }
        form.toPo(t, true);
        if (!hasErrors()) {
            service.updateTenant(id, t);
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 保存角色
     * 
     * @param form
     * @return
     */
    public Page doDisRole(WebForm form) {
        String isAdd = CommUtil.null2String(form.get("isAdd"));
        String tenantId = CommUtil.null2String(form.get("tenantId"));
        String roleId = CommUtil.null2String(form.get("roleId"));
        Role tenantRole = this.roleService.getRole(Long.parseLong(roleId));
        Tenant tenant = this.service.getTenant(Long.parseLong(tenantId));
        if (!"".equals(isAdd)) {
            tenant.addTenantRole(tenantRole);
        } else {
            tenant.delTenantRole(tenantRole);
        }
        this.service.updateTenant(Long.parseLong(tenantId), tenant);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    public void setroleService(IRoleService roleService) {
        this.roleService = roleService;
    }
    public Page doGetTree(WebForm form){
    	QueryObject qo = form.toPo(TenantQuery.class);
    	List<Tenant> list = this.service.getTenantBy(qo).getResult();
    	List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
    	if(list!=null && list.size()>0){
    		for(Tenant te:list){
    			Map<String,Object> map = Maps.newHashMap();
    			map.put("id", te.getId() + "");
                map.put("text", te.getTitle());
                qo = new QueryObject();
                qo.addQuery("obj.parent",te,"=");
                qo.setPageSize(1);
                List<Tenant> chilren = this.service.getTenantBy(qo).getResult();
                if(chilren==null || chilren.size()==0){
                	map.put("leaf", true);
                }else{
                	map.put("leaf", false);
                }
               nodes.add(map);
    		}
    	}else{
    		Map<String,Object> map = Maps.newHashMap();
    		map.put("text", "无租户");
    		map.put("id", "0");
    		map.put("leaf", true);
    		nodes.add(map);
    	}
    	form.jsonResult(nodes);
        return Page.JSONPage;
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