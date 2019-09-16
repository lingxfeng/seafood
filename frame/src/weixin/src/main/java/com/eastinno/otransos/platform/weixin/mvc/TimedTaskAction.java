package com.eastinno.otransos.platform.weixin.mvc;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.platform.weixin.domain.TimedTask;
import com.eastinno.otransos.platform.weixin.service.ITimedTaskService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * TimedTaskAction
 * @author 
 */
@Action
public class TimedTaskAction extends AbstractPageCmdAction {
    @Inject
    private ITimedTaskService service;
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getTimedTaskBy(qo);
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
        TimedTask entry = (TimedTask)form.toPo(TimedTask.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addTimedTask(entry);
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
        TimedTask entry = this.service.getTimedTask(id);
        form.toPo(entry);
        if (!hasErrors()) {
        	String state=CommUtil.null2String(form.get("state"));
        	if("1".equals(state) || "3".equals(state)){
        		entry.setFollowerGroup(null);
        	}
            boolean ret = service.updateTimedTask(id,entry);
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
        this.service.delTimedTask(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
	public void setService(ITimedTaskService service) {
        this.service = service;
    }
	
}