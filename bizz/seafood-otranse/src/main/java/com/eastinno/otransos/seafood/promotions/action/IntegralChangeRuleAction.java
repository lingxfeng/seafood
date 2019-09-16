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
import com.eastinno.otransos.seafood.promotions.domain.IntegralChangeRule;
import com.eastinno.otransos.seafood.promotions.service.IIntegralChangeRuleService;

/**
 * IntegralChangeRuleAction
 * @author 
 */
@Action
public class IntegralChangeRuleAction extends AbstractPageCmdAction {
    @Inject
    private IIntegralChangeRuleService service;
        
    /**
     * 更新页
     * @param form
     * @return
     */
    public Page doToUpdateIntegralRule(WebForm form){
    	form.addResult("ruleRegister", this.service.getIntegralRuleByRegister());
    	form.addResult("ruleRate", this.service.getIntegralCashRate());
    	return new Page("/bcd/promotions/integral/integralRuleEdit.html");
    }
    
    /**
     * 更新规则
     * @param form
     * @return
     */
    public Page doUpdateIntegralRule(WebForm form){
    	String ruleRegister = CommUtil.null2String(form.get("ruleRegister"));
    	String ruleRate = CommUtil.null2String(form.get("ruleRate"));
    	if(!ruleRegister.equals("")){
    		if(!this.service.setIntegralRuleByRegister(Long.parseLong(ruleRegister))){
    			form.addResult("msg", "注册增积分规则更新失败！");
    		}
    	}
    	if(!ruleRate.equals("")){
    		if(!this.service.setIntegralCashRate(Long.parseLong(ruleRate))){
    			form.addResult("msg", "积分与现金兑换规则更新失败！");
    		}
    	}
    	if(form.getDiscoResult().get("msg") == null){
    		form.addResult("msg", "更新成功！");
    	}
    	return new Page("/bcd/promotions/integral/integralRuleEdit.html");
    }
    
    public void setService(IIntegralChangeRuleService service) {
        this.service = service;
    }
}