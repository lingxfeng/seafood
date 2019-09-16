package com.eastinno.otransos.shop.spokesman.action;

import java.util.List;

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
import com.eastinno.otransos.shop.droduct.domain.ShopProduct;
import com.eastinno.otransos.shop.spokesman.domain.SpecialAllowance;
import com.eastinno.otransos.shop.spokesman.domain.SpokesmanProduct;
import com.eastinno.otransos.shop.spokesman.service.ISpecialAllowanceService;

/**
 * SpecialAllowanceAction
 * @author 
 */
@Action
public class SpecialAllowanceAction extends AbstractPageCmdAction {
    @Inject
    private ISpecialAllowanceService service;
    /**
     * 查询库数据
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        List<SpecialAllowance> pageList = this.service.getSpecialAllowanceBy(qo).getResult();
        if(pageList != null && pageList.size()!=0){
        	SpecialAllowance entry = pageList.get(0);
        	form.addResult("entry",entry);
        }
        return new Page("/bcd/spokesman/specialallowance/specialAllowanceEdit.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
    	SpecialAllowance entry = (SpecialAllowance)form.toPo(SpecialAllowance.class);
         form.toPo(entry);
         if (!hasErrors()) {
             Long id = this.service.addSpecialAllowance(entry);
             if (id != null) {
                 form.addResult("msg", "添加成功");
             }
         }
         return go("list");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
    	Long id = new Long(CommUtil.null2String(form.get("id")));
        SpecialAllowance entry = this.service.getSpecialAllowance(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateSpecialAllowance(id,entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delSpecialAllowance(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(ISpecialAllowanceService service) {
        this.service = service;
    }
}