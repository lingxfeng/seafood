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
import com.eastinno.otransos.seafood.trade.domain.LogisticsCompany;
import com.eastinno.otransos.seafood.trade.service.ILogisticsCompanyService;

/**
 * LogisticsCompanyAction
 * @author 
 */
@Action
public class LogisticsCompanyAction extends AbstractPageCmdAction {
    @Inject
    private ILogisticsCompanyService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setPageSize(-1);
        IPageList pageList = this.service.getLogisticsCompanyBy(qo);
        form.addResult("pl", pageList);
        return new Page("/shopmanage/trade/logistic/LogisticsCompanyList.html");
    }
    /**
     * 保存数据跳转
     * 
     * @param form
     */
    public Page doToSave(WebForm form) {
        return new Page("/shopmanage/trade/logistic/LogisticsCompanyEdit.html");
    }
    /**
     * 修改数据数据跳转
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
    	Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        LogisticsCompany entry = this.service.getLogisticsCompany(id);
    	form.addResult("entry", entry);
        return new Page("/shopmanage/trade/logistic/LogisticsCompanyEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        LogisticsCompany entry = (LogisticsCompany)form.toPo(LogisticsCompany.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addLogisticsCompany(entry);
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
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        LogisticsCompany entry = this.service.getLogisticsCompany(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateLogisticsCompany(id,entry);
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
        this.service.delLogisticsCompany(id);
        return go("list");
    }
    /**
     * 更改状态
     * 
     * @param form
     */
    public Page doChange(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        LogisticsCompany entry = this.service.getLogisticsCompany(id);
        if(entry.getStatus()==0){
        	entry.setStatus(Short.parseShort("1"));
        }else if(entry.getStatus()==1){
        	entry.setStatus(Short.parseShort("0"));
        }
        this.service.updateLogisticsCompany(entry.getId(), entry);
        return go("list");
    }
    
    public void setService(ILogisticsCompanyService service) {
        this.service = service;
    }
}