package com.eastinno.otransos.seafood.promotions.action;

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
import com.eastinno.otransos.seafood.promotions.domain.IntegralBuyRecord;
import com.eastinno.otransos.seafood.promotions.query.IntegralBuyRecordQuery;
import com.eastinno.otransos.seafood.promotions.service.IIntegralBuyRecordService;

/**
 * IntegralBuyRecordAction
 * @author 
 */
@Action
public class IntegralBuyRecordAction extends AbstractPageCmdAction {
    @Inject
    private IIntegralBuyRecordService service;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
    	IntegralBuyRecordQuery qo = (IntegralBuyRecordQuery) form.toPo(IntegralBuyRecordQuery.class);
    	qo.setOrderBy("order.ceateDate");
    	qo.setOrderType("DESC");
        IPageList pageList = this.service.getIntegralBuyRecordBy(qo);
		CommUtil.saveIPageList2WebForm(pageList, form);
		form.addResult("pl", pageList);
        return new Page("/bcd/promotions/integral/integralRecordList.html");
    }
        
    public void setService(IIntegralBuyRecordService service) {
        this.service = service;
    }
}