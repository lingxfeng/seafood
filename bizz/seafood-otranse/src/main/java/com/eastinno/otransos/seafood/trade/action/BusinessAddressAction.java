package com.eastinno.otransos.seafood.trade.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.trade.domain.BusinessAddress;
import com.eastinno.otransos.seafood.trade.service.IBusinessAddressService;

/**
 * BusinessAddressAction
 * @author 
 */
@Action
public class BusinessAddressAction extends AbstractPageCmdAction {
    @Inject
    private IBusinessAddressService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getBusinessAddressBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        BusinessAddress entry = (BusinessAddress)form.toPo(BusinessAddress.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addBusinessAddress(entry);
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
        BusinessAddress entry = this.service.getBusinessAddress(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateBusinessAddress(id,entry);
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
        this.service.delBusinessAddress(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(IBusinessAddressService service) {
        this.service = service;
    }
}