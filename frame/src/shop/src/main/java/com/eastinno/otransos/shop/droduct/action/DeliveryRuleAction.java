package com.eastinno.otransos.shop.droduct.action;

import java.util.ArrayList;
import java.util.List;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.shop.droduct.domain.DeliveryRule;
import com.eastinno.otransos.shop.droduct.service.IDeliveryRuleService;
import com.eastinno.otransos.shop.droduct.service.IRemoteRegionService;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.shop.trade.service.IShopOrderInfoService;
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
		DeliveryRule defaultFreepostStart = this.deliveryService.getDefaultNormalFreepostStart();
		DeliveryRule defaultNormalPostCost = this.deliveryService.getDefaultNormalPostCost();
		DeliveryRule defaultRemotePostCost = this.deliveryService.getDefaultRemoteRegionPostCost();
		form.addResult("defaultFreepostStart", defaultFreepostStart.getRuleValue());
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
		Double defaultFreepostStart = Double.parseDouble(form.get("defaultFreepostStart").toString());
		Double defaultNormalPostCost = Double.parseDouble(form.get("defaultNormalPostCost").toString());
		Double defaultRemotePostCost = Double.parseDouble(form.get("defaultRemotePostCost").toString());
		this.deliveryService.setDefaultNormalFreepostStart(defaultFreepostStart);
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
