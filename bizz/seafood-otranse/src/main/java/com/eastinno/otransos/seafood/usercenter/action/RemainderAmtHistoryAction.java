package com.eastinno.otransos.seafood.usercenter.action;

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
import com.eastinno.otransos.seafood.usercenter.domain.RemainderAmtHistory;
import com.eastinno.otransos.seafood.usercenter.query.RemainderAmtHistoryQuery;
import com.eastinno.otransos.seafood.usercenter.service.IRemainderAmtHistoryService;
import com.eastinno.otransos.seafood.util.formatUtil;

/**
 * RemainderAmtHistoryAction
 * 会员-》余额记录页的action类
 * @author 
 */
@Action
public class RemainderAmtHistoryAction extends AbstractPageCmdAction {
    @Inject
    private IRemainderAmtHistoryService service;
    
    public Page doInit(WebForm form){
    	return go("list");
    }
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	RemainderAmtHistoryQuery qo = form.toPo(RemainderAmtHistoryQuery.class);
    	Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("userId"))));
    	qo.addQuery("obj.user.id", id,"=");
    	qo.setOrderBy("createDate desc");
        IPageList pageList = this.service.getRemainderAmtHistoryBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
        form.addResult("pl", pageList);
        form.addResult("id", id);
        form.addResult("fu", formatUtil.fu);
        return new Page("/bcd/member/remainderamthistory/RemainderAmtHistoryList.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        RemainderAmtHistory entry = form.toPo(RemainderAmtHistory.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addRemainderAmtHistory(entry);
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
        RemainderAmtHistory entry = this.service.getRemainderAmtHistory(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateRemainderAmtHistory(id,entry);
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
        this.service.delRemainderAmtHistory(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(IRemainderAmtHistoryService service) {
        this.service = service;
    }
}