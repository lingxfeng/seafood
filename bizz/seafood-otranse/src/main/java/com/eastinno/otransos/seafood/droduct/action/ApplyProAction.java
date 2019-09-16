package com.eastinno.otransos.seafood.droduct.action;

import java.util.Date;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.dbo.util.StringUtils;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.shiro.security.core.ShiroUtils;
import com.eastinno.otransos.seafood.droduct.domain.ApplyPro;
import com.eastinno.otransos.seafood.droduct.service.IApplyProService;

/**
 * ApplyProAction
 * @author 
 */
@Action
public class ApplyProAction extends AbstractPageCmdAction {
    @Inject
    private IApplyProService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	String myShopName=CommUtil.null2String(form.get("myShopName"));
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        if(StringUtils.hasText(myShopName)){
        	qo.addQuery("obj.distributor.myShopName", myShopName, "=");
        }
        qo.setOrderBy("createDate");
        qo.setOrderType("desc");
        IPageList pageList = this.service.getApplyProBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("applyProList", pageList.getResult());
        return new Page("/shopmanage/product/ShopProduct/applyProductList.html");
    }     
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ApplyPro entry = (ApplyPro)form.toPo(ApplyPro.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addApplyPro(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ApplyPro entry = this.service.getApplyPro(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateApplyPro(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delApplyPro(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 审核申请商品
     * 
     * @param form
     */
    public Page doAuditApplyPro(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        String status=CommUtil.null2String(form.get("type"));
        ApplyPro applyPro=this.service.getApplyPro(id);
        applyPro.setStatus(Integer.valueOf(status));
        applyPro.setAuditUser(ShiroUtils.getUser());
        applyPro.setAuditTime(new Date());
        this.service.updateApplyPro(applyPro.getId(), applyPro);
        return pageForExtForm(form);
    }
    
    public void setService(IApplyProService service) {
        this.service = service;
    }
}