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
import com.eastinno.otransos.payment.common.domain.PayTypeE;
import com.eastinno.otransos.payment.common.domain.PaymentConfig;
import com.eastinno.otransos.payment.common.service.IPaymentConfigService;
import com.eastinno.otransos.seafood.droduct.domain.ProductType;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.promotions.domain.IntegralRechargeRecord;
import com.eastinno.otransos.seafood.promotions.query.IntegralRechargeRecordQuery;
import com.eastinno.otransos.seafood.promotions.service.IIntegralRechargeRecordService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopPayMentService;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.service.IShopMemberService;

/**
 * IntegralRechargeRecordAction
 * @author 
 */
@Action
public class IntegralRechargeRecordAction extends AbstractPageCmdAction {
    @Inject
    private IIntegralRechargeRecordService service;
    @Inject
    private IPaymentConfigService paymentConfigService;
    @Inject
    private IShopPayMentService shopPayMentService;
    @Inject
    private IShopMemberService shopMemberService;
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(IntegralRechargeRecordQuery.class);
        qo.setOrderBy("createDate");
        qo.setOrderType("DESC");
        IPageList pl = this.service.getIntegralRechargeRecordBy(qo);      
        form.addResult("pl", pl);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/bcd/promotions/integral/chargeList.html");
    }
    /**
     * 定位会员查询
     */
    public Page doSingleList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopMember member = this.shopMemberService.getShopMember(id);
        qo.addQuery("obj.member.id", id, "=");
        IPageList pl = this.service.getIntegralRechargeRecordBy(qo);      
        form.addResult("pl", pl);
        form.addResult("id", id);
        CommUtil.saveIPageList2WebForm(pl, form);
        return new Page("/bcd/promotions/integral/chargeList.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        IntegralRechargeRecord entry = (IntegralRechargeRecord)form.toPo(IntegralRechargeRecord.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addIntegralRechargeRecord(entry);
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
        IntegralRechargeRecord entry = this.service.getIntegralRechargeRecord(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateIntegralRechargeRecord(id,entry);
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
        this.service.delIntegralRechargeRecord(id);
        Page page = pageForExtForm(form);
        page.setContentType("html");
        return page;
    }
    
    public Page doToIndex(WebForm form){
    	Map<String,String> map = new HashMap<String,String>();
    	long id=(long)CommUtil.null2Int(form.get("id"));
    	IntegralRechargeRecord integralRechargeRecord = this.service.getIntegralRechargeRecord(id);
    	if(integralRechargeRecord.getStatus()==1){
    		map.put("status", "1");
    	}
    	form.jsonResult(map);
    	return Page.JSONPage;
    }
    
    public void setService(IIntegralRechargeRecordService service) {
        this.service = service;
    }

	public IPaymentConfigService getPaymentConfigService() {
		return paymentConfigService;
	}

	public void setPaymentConfigService(IPaymentConfigService paymentConfigService) {
		this.paymentConfigService = paymentConfigService;
	}

	public IShopPayMentService getShopPayMentService() {
		return shopPayMentService;
	}

	public void setShopPayMentService(IShopPayMentService shopPayMentService) {
		this.shopPayMentService = shopPayMentService;
	}

	public IIntegralRechargeRecordService getService() {
		return service;
	}
	public IShopMemberService getShopMemberService() {
		return shopMemberService;
	}
	public void setShopMemberService(IShopMemberService shopMemberService) {
		this.shopMemberService = shopMemberService;
	}
    
}