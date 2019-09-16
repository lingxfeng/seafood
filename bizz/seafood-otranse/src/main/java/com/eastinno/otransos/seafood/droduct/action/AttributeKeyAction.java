package com.eastinno.otransos.seafood.droduct.action;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.AttributeKey;
import com.eastinno.otransos.seafood.droduct.service.IAttributeKeyService;

/**
 * AttributeKeyAction
 * @author 
 */
@Action
public class AttributeKeyAction extends AbstractPageCmdAction {
    @Inject
    private IAttributeKeyService service;
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("list");
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pl = this.service.getAttributeKeyBy(qo);
        AjaxUtil.convertEntityToJson(pl);
        form.jsonResult(pl);
        return Page.JSONPage;
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        AttributeKey entry = (AttributeKey)form.toPo(AttributeKey.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addAttributeKey(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    /**
     * 导入添加页面
     */
    public Page doAdd() {
        return go("edit");
    }
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public void doEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            AttributeKey entry = this.service.getAttributeKey(id);
            form.addPo(entry);
        }
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        AttributeKey entry = this.service.getAttributeKey(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateAttributeKey(id, entry);
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
        this.service.delAttributeKey(id);
        return pageForExtForm(form);
    }
    
    public void setService(IAttributeKeyService service) {
        this.service = service;
    }
}