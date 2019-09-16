package com.eastinno.otransos.seafood.promotions.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.eastinno.otransos.seafood.core.action.WxShopBaseAction;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakeRegular;
import com.eastinno.otransos.seafood.promotions.domain.SweepstakesRecord;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakeRegularService;
import com.eastinno.otransos.seafood.promotions.service.ISweepstakesRecordService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;

/**
 * SweepstakesRecordAction
 * @author 
 */
@Action
public class SweepstakesRecordAction extends WxShopBaseAction {
    @Inject
    private ISweepstakesRecordService service;
    @Inject
    private ISweepstakeRegularService sweepstakeRegularservice;
    /**
     * 后台抽奖记录列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        qo.setOrderBy("createTime");
        qo.setOrderType("desc");
        IPageList pl = this.service.getSweepstakesRecordBy(qo);
        CommUtil.saveIPageList2WebForm(pl, form);
        form.addResult("pl", pl);
        return new Page("/bcd/promotions/sweepstakes/sweepstakeRecord.html");
    }
    
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doToDispatch(WebForm form) {
    	String id= CommUtil.null2String(form.get("id"));
    	SweepstakesRecord sr = this.service.getSweepstakesRecord(Long.parseLong(id));
    	sr.setIsDispatch(Short.parseShort("1"));
    	this.service.updateSweepstakesRecord(sr.getId(), sr);
        return go("list");
    }
   
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        SweepstakesRecord entry = this.service.getSweepstakesRecord(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateSweepstakesRecord(id,entry);
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
        this.service.delSweepstakesRecord(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }

    /**
     * 抽奖记录列表页面
     * 
     * @param form
     */
    public Page doSweepstakeRecord(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getSweepstakesRecordBy(qo);
		AjaxUtil.convertEntityToJson(pageList);
        form.jsonResult(pageList);
        return Page.JSONPage;
    }
    public void setService(ISweepstakesRecordService service) {
        this.service = service;
    }

	public ISweepstakeRegularService getSweepstakeRegularservice() {
		return sweepstakeRegularservice;
	}

	public void setSweepstakeRegularservice(
			ISweepstakeRegularService sweepstakeRegularservice) {
		this.sweepstakeRegularservice = sweepstakeRegularservice;
	}
    
}