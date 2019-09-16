package com.eastinno.otransos.seafood.statistics.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.seafood.statistics.domain.VisitorHistory;
import com.eastinno.otransos.seafood.statistics.service.IVisitorHistoryService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.web.tools.IPageList;

/**
 * VisitorHistoryAction
 * @author 
 */
@Action
public class VisitorHistoryAction extends AbstractPageCmdAction {
    @Inject
    private IVisitorHistoryService service;
    @Autowired
    private IShopOrderInfoService shopOrderInfoService;
    @Autowired
    private IDeliveryRuleService deliveryService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pageList = this.service.getVisitorHistoryBy(qo);
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
        VisitorHistory entry = (VisitorHistory)form.toPo(VisitorHistory.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addVisitorHistory(entry);
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
        VisitorHistory entry = this.service.getVisitorHistory(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateVisitorHistory(id,entry);
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
        this.service.delVisitorHistory(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public void setService(IVisitorHistoryService service) {
        this.service = service;
    }
    public Page doTest(){
		ShopOrderInfo order = this.shopOrderInfoService.getShopOrderInfo(1703936L);
		Double freight = this.deliveryService.getDeliveryCost(order);
		return null;
	}

	public IShopOrderInfoService getShopOrderInfoService() {
		return shopOrderInfoService;
	}

	public void setShopOrderInfoService(IShopOrderInfoService shopOrderInfoService) {
		this.shopOrderInfoService = shopOrderInfoService;
	}

	public IDeliveryRuleService getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(IDeliveryRuleService deliveryService) {
		this.deliveryService = deliveryService;
	}
    
}