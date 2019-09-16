package com.eastinno.otransos.seafood.droduct.action;

import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.seafood.droduct.domain.DeliveryRule;
import com.eastinno.otransos.seafood.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.seafood.droduct.service.IRemoteRegionService;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderInfoService;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

@Action
public class DeliveryRuleAction extends AbstractPageCmdAction{
	@Inject
	private IDeliveryRuleService deliveryService;

	/**
	 * 运费相关的默认信息修改页
	 * @param form
	 * @return
	 */
	public Page doToDefaultDelivery(WebForm form){
		DeliveryRule defaultOrderFreepostStart = this.deliveryService.getOrderFreepostStart();
		DeliveryRule defaultFreepostStart = this.deliveryService.getDefaultNormalFreepostStart();
		DeliveryRule defaultNearPostCost = this.deliveryService.getDefaultNearRegionPostCost();
		DeliveryRule defaultNormalPostCost = this.deliveryService.getDefaultNormalPostCost();
		DeliveryRule defaultRemotePostCost = this.deliveryService.getDefaultRemoteRegionPostCost();
		form.addResult("defaultOrderFreepostStart", defaultOrderFreepostStart.getRuleValue());
		form.addResult("defaultFreepostStart", defaultFreepostStart.getRuleValue());
		form.addResult("defaultNearPostCost", defaultNearPostCost.getRuleValue());
		form.addResult("defaultNormalPostCost", defaultNormalPostCost.getRuleValue());
		form.addResult("defaultRemotePostCost", defaultRemotePostCost.getRuleValue());		
		return new Page("/bcd/system/remote_region/deliveryDefaultSettings.html");
	}
	
	/**
	 * 更行运费相关的默认设置
	 * @param form
	 * @return
	 */
	public void doDefaultDelivery(WebForm form){
		
		Double defaultOrderFreepostStart = Double.parseDouble(form.get("defaultOrderFreepostStart").toString());
		Double defaultFreepostStart = Double.parseDouble(form.get("defaultFreepostStart").toString());
		Double defaultNearPostCost = Double.parseDouble(form.get("defaultNearPostCost").toString());
		Double defaultNormalPostCost = Double.parseDouble(form.get("defaultNormalPostCost").toString());
		Double defaultRemotePostCost = Double.parseDouble(form.get("defaultRemotePostCost").toString());
		this.deliveryService.setOrderFreepostStart(defaultOrderFreepostStart);
		this.deliveryService.setDefaultNormalFreepostStart(defaultFreepostStart);
		this.deliveryService.setDefaultNearRegionPostCost(defaultNearPostCost);
		this.deliveryService.setDefaultNormalPostCost(defaultNormalPostCost);
		this.deliveryService.setDefaultRemoteRegionPostCost(defaultRemotePostCost);
		form.addResult("result", "success");
		form.addResult("message", "设置成功！");
		this.go("toDefaultDelivery");
	}
		
	public IDeliveryRuleService getDeliveryService() {
		return deliveryService;
	}
	public void setDeliveryService(IDeliveryRuleService deliveryService) {
		this.deliveryService = deliveryService;
	}
	
	
}
