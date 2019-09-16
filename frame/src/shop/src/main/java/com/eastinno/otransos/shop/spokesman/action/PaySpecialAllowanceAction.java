package com.eastinno.otransos.shop.spokesman.action;

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
import com.eastinno.otransos.shop.spokesman.domain.PaySpecialAllowance;
import com.eastinno.otransos.shop.spokesman.service.IPaySpecialAllowanceService;

/**
 * PaySpecialAllowanceAction
 * @author 
 */
@Action
public class PaySpecialAllowanceAction extends AbstractPageCmdAction {
    @Inject
    private IPaySpecialAllowanceService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pageList = this.service.getPaySpecialAllowanceBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        return new Page("/bcd/spokesman/specialallowance/specialAllowanceDispatchRecord.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        PaySpecialAllowance entry = (PaySpecialAllowance)form.toPo(PaySpecialAllowance.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addPaySpecialAllowance(entry);
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
        PaySpecialAllowance entry = this.service.getPaySpecialAllowance(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updatePaySpecialAllowance(id,entry);
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
        this.service.delPaySpecialAllowance(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(IPaySpecialAllowanceService service) {
        this.service = service;
    }
}