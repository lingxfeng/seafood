package com.eastinno.otransos.shop.trade.action;

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
import com.eastinno.otransos.shop.trade.domain.DispatchTemplate;
import com.eastinno.otransos.shop.trade.service.IDispatchTemplateService;

/**
 * DispatchTemplateAction
 * @author 
 */
@Action
public class DispatchTemplateAction extends AbstractPageCmdAction {
    @Inject
    private IDispatchTemplateService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getDispatchTemplateBy(qo);
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
        DispatchTemplate entry = (DispatchTemplate)form.toPo(DispatchTemplate.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addDispatchTemplate(entry);
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
        DispatchTemplate entry = this.service.getDispatchTemplate(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateDispatchTemplate(id,entry);
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
        this.service.delDispatchTemplate(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(IDispatchTemplateService service) {
        this.service = service;
    }
}