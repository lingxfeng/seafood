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
import com.eastinno.otransos.seafood.usercenter.domain.IntegralHistory;
import com.eastinno.otransos.seafood.usercenter.query.IntegralHistoryQuery;
import com.eastinno.otransos.seafood.usercenter.service.IIntegralHistoryService;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;

/**
 * IntegralHistoryAction
 * 会员-》积分记录的action类
 * @author 
 */
@Action
public class IntegralHistoryAction extends AbstractPageCmdAction {
    @Inject
    private IIntegralHistoryService service;
    @Inject
    private IShopMemberService shopMemberService;
    
	public Page doInit(WebForm form){
    	return go("list");
    }
    
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	
    	String userId = CommUtil.null2String(form.get("userId"));
    	IntegralHistoryQuery qo = form.toPo(IntegralHistoryQuery.class);
    	String currentPageStr = CommUtil.null2String(form.get("currentPage"));
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
    	if(currentPageStr.equals("")){
    		qo.setCurrentPage(1);
    	}else{
    		int currentPage = Integer.parseInt(currentPageStr);
    		qo.setCurrentPage(currentPage);
    	}    	
        IPageList pageList = this.service.getIntegralHistoryBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);
		form.addResult("userId", userId);
        return new Page("/bcd/member/integralhistory/IntegralHistoryList.html");
    }
    /**
     * 后台赠送积分记录
     */
    public Page doSendIntegral(WebForm form){
    	IntegralHistoryQuery qo = form.toPo(IntegralHistoryQuery.class);
    	qo.addQuery("obj.type",4, "=");
    	qo.setOrderBy("createDate");
    	qo.setOrderType("desc");
        IPageList pageList = this.service.getIntegralHistoryBy(qo);
        CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);
        return new Page("/bcd/member/integralhistory/SendList.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        IntegralHistory entry = form.toPo(IntegralHistory.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addIntegralHistory(entry);
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
        IntegralHistory entry = this.service.getIntegralHistory(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateIntegralHistory(id,entry);
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
        this.service.delIntegralHistory(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    /**
     * 添加几分记录
     * 
     * @param form
     */
    public Page doAddIntegralHistory(WebForm form) {
        Integer integral=CommUtil.null2Int(form.get("integral"));
        String description=CommUtil.null2String(form.get("description"));
        this.service.saveIntegralHistory((long)integral,null,description,1);
        return pageForExtForm(form);
    }
    
    public void setService(IIntegralHistoryService service) {
        this.service = service;
    }
    public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}

	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
}